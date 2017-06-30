package sample;

import course.Topic;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

import static sample.Element.*;


/**
 * Created by ghost on 14/6/17.
 */
public class EditPages implements Initializable{

    @FXML
    ListView pageList;
    @FXML
    ListView paneList;
    @FXML
    TextField scaleBox;
    @FXML
    ScrollPane pageWindow;
    @FXML
    TextField fontSizeBox;
    @FXML
    ComboBox fontFamilyBox;
    @FXML
    ComboBox textAlignBox;
    @FXML
    ColorPicker fontColorBox;
    @FXML
    TextField leftAnchorBox;
    @FXML
    TextField topAnchorBox;
    @FXML
    TextField widthBox;
    @FXML
    TextField heightBox;
    @FXML
    CheckBox aspectRatio;
    @FXML
    TitledPane textProperty;
    @FXML
    Button deleteElementBox;
    @FXML
    Button addTextAreaButton;
    @FXML
    MenuButton addPageButton;
    @FXML
    StackPane componentPropertyBox;

    static Region bottomright;

    private ChangeListener<String> widthListner;
    private ChangeListener<String> heightListner;
    private AnchorPane currentAnchorPane;
    private Rectangle pdfrec;

    private static BooleanProperty selectePaneChanged;
    public static Pane selectedPane;

    public static double orgSceneX;
    public static double orgSceneY;
    public static double orgLeftAnchor;
    public static double orgTopAnchor;
    public static File file;

    public static ArrayList<AnchorPane> pages;
    public int checkifpdf=0;
    public Document document;

    public static void setToFront(Node n){
        try {
            n.toFront();
            if (n.getClass() == TextField.class)
                n.getParent().toFront();
            while (n.getParent().getClass() != Layout.class) {
                n.toFront();
                n = n.getParent();
            }
            n.toFront();
            EditPages.setSelectedPane((Pane) n);
        }
        catch (Exception e){
            PopUp.display("Error!",e.toString());
            e.printStackTrace();
        }
    }

    public static void enableMovable(Node n){
        n.setOnMousePressed(e->{
            EditPages.orgSceneX=e.getSceneX();
            EditPages.orgSceneY=e.getSceneY();
            EditPages.orgLeftAnchor=AnchorPane.getLeftAnchor((Node) e.getSource());
            EditPages.orgTopAnchor=AnchorPane.getTopAnchor((Node) e.getSource());
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
            setToFront(n);

        });
        n.setOnMouseDragged(e->{
            double offsetX=(e.getSceneX()- EditPages.orgSceneX);
            double offsetY=(e.getSceneY()- EditPages.orgSceneY);
            AnchorPane.setLeftAnchor((Node) e.getSource(),EditPages.orgLeftAnchor+offsetX);
            AnchorPane.setTopAnchor((Node) e.getSource(),EditPages.orgTopAnchor+offsetY);
            selectePaneChanged.setValue(true);
            ((Node) e.getSource()).setCursor(Cursor.MOVE);
        });
        n.setOnMouseEntered(e->{
            ((Node)e.getSource()).setCursor(Cursor.OPEN_HAND);
        });
        n.setOnMouseReleased(e->{
            ((Node)e.getSource()).setCursor(Cursor.OPEN_HAND);
        });
    }
    public static void disableMovable(Node n){
        n.setOnMousePressed(anyEvent->{        });
        n.setOnMouseDragged(anyEvent->{        });
        n.setOnMouseClicked(anyEvent->{        });
        n.setOnMouseReleased(anyEvent->{        });
        n.setOnMouseEntered(anyEvent->{        });
        n.setOnMouseExited(anyEven->{        });
    }

    public static ArrayList<AnchorPane> makePagesEditable(ArrayList<AnchorPane> pages){
        for(AnchorPane each : pages){
            each=makePageEditable(each);
        }
        return pages;
    }
    public static ArrayList<AnchorPane> makePagesUneditable(ArrayList<AnchorPane> pages){
        if(bottomright!=null && bottomright.getParent()!=null){
            ((AnchorPane) bottomright.getParent()).getChildren().removeAll(bottomright);
        }
        for(AnchorPane each : pages){
            each=makePageUneditable(each);
        }
        return pages;
    }

    private static AnchorPane makePageEditable(AnchorPane page){
        for(Node each : page.getChildren()){
            Node node= ((StackPane) each).getChildren().get(0);
            enableMovable(each);
            each.setStyle("-fx-background-color: transparent;-fx-border-width: 1px");
            //System.out.println(each+" "+node);
            if(node.getClass()==TextField.class){
                //System.out.println("making textfield editable");
                node=Element.enableEdit((TextField) node);
            }
            else if(node.getClass()==TextArea.class){
                //System.out.println("making textarea editable");
                node=Element.enableEdit((TextArea) node);
            }
            //System.out.println("enable ended");
        }
        return page;
    }
    private static AnchorPane makePageUneditable(AnchorPane page){
        for(Node each : page.getChildren()){
            if(each.getClass()==StackPane.class) {
                disableMovable(each);
                each.setStyle("-fx-background-color: transparent;" +
                        "-fx-border-width: 0px");
                Node node = ((StackPane) each).getChildren().get(0);
                //System.out.println(each+" "+node);
                if (node.getClass() == TextField.class) {
                    //System.out.println("making textfield uneditable");
                    node = Element.disableEdit((TextField) node);
                } else if (node.getClass() == TextArea.class) {
                    //System.out.println("making textarea uneditable");
                    node = Element.disableEdit((TextArea) node);
                }
                //System.out.println("disable ended");
            }
        }
        return page;
    }

    public static void removeAllPages(Topic topic){
        for( AnchorPane each : topic.pages){
            removeAllContents(each);
        }
        topic.pages.clear();
    }
    public static void removeAllContents(AnchorPane page){
        for(Node each :page.getChildren()){
            removeContent(each);
        }
        page.getChildren().clear();
    }
    public static void removeContent(Node node){
        Node content=((StackPane)node).getChildren().get(0);
        if(content.getClass()==TextField.class || content.getClass()==TextArea.class){
            content=null;
        }
        else if(content.getClass()==ImageView.class){
            ((ImageView)content).setImage(null);
        }
        else if(content.getClass()== MediaView.class){
            ((MediaView)content).getMediaPlayer().dispose();
            ((MediaView)content).setMediaPlayer(null);
        }
    }

    public void removeSelectedContent(){
        if(selectedPane!=null) {
            removeContent(selectedPane);
            ((AnchorPane) selectedPane.getParent()).getChildren().remove(selectedPane);
            setSelectedPane(null);
        }
    }
    public void deletePage(){
        display(-1);
        ArrayList<AnchorPane> tmp=new ArrayList<>();
        for(Integer each : (ObservableList<Integer>)pageList.getSelectionModel().getSelectedIndices()){
            tmp.add(pages.get(each));
        }
        pages.removeAll(tmp);
        pageList.getItems().removeAll(pageList.getSelectionModel().getSelectedItems());
    }

    public Region getNewRegion(){
        Region region=new Region();
        region.setStyle("-fx-background-color: blue");
        region.setMinSize(10,10);
        region.setMaxSize(10,10);
        region.setOnMousePressed(event -> {
            event.consume();
            EditPages.orgSceneX=event.getSceneX();
            EditPages.orgSceneY=event.getSceneY();
            EditPages.orgLeftAnchor=region.getTranslateX();
            EditPages.orgTopAnchor=region.getTranslateY();
        });
        region.setOnMouseDragged(event -> {
            double offsetX=event.getSceneX()-orgSceneX;
            double offsetY=event.getSceneY()-orgSceneY;
            double newTranslateX= orgLeftAnchor+offsetX;
            double newTranslateY= orgTopAnchor+offsetY;
            region.setTranslateX(newTranslateX);
            region.setTranslateY(newTranslateY);
            widthBox.fireEvent(new ActionEvent());
        });
        return region;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//<editor-fold desc="selectedPane Changed Listner">
        selectePaneChanged=new SimpleBooleanProperty(false);
        selectePaneChanged.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true){
                    selectePaneChanged.setValue(false);
                    try {
                        bindPropertiesToSelectedPane();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    regionstoSelectedPane();
                }
            }
        });
//</editor-fold>
        pageList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pageList.setOnMouseClicked(event -> {
            ((Button) pageList.getSelectionModel().getSelectedItem()).fire();
        });
        for(AnchorPane each : pages){
            Button indexButton=getIndexButton(pages.indexOf(each));
            pageList.getItems().add(indexButton);
        }
        try {
            display(0);
        }catch (Exception e){
            display(-1);
        }
        //<editor-fold desc="adding layouts">
        Field[] fields=Layout.class.getDeclaredFields();
        for(Field f : fields){
            if(Modifier.isStatic(f.getModifiers()) && f.getName().matches("[A-Z_]*")){
                MenuItem menuItem = new MenuItem(f.getName());
                menuItem.setOnAction(event -> {
                    try {
                        addPage(f.getInt(new Layout()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                addPageButton.getItems().add(menuItem);
            }
        }
        //</editor-fold>

        //<editor-fold desc="left property listeners (only read double of integer numbers)">
        //property box can only take numeric input
        leftAnchorBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                    if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                        //System.out.println(oldValue + " " + newValue);
                        leftAnchorBox.setText(oldValue);
                    } else {
                        //System.out.println(oldValue + " " + newValue);
                        leftAnchorBox.setText("0");
                    }
                }
                else {
                    leftAnchorBox.setText(newValue);
                }
            }
        });
        topAnchorBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                    if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                        topAnchorBox.setText(oldValue);
                    } else {
                        topAnchorBox.setText("0");
                    }
                }
                else {
                    topAnchorBox.setText(newValue);
                }
            }
        });
        widthBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                    if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                        widthBox.setText(oldValue);
                    } else {
                        widthBox.setText("0");
                    }
                }
                else {
                    widthBox.setText(newValue);
                }
            }
        });
        heightBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                    if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                        heightBox.setText(oldValue);
                    } else {
                        heightBox.setText("0");
                    }
                }
                else {
                    heightBox.setText(newValue);
                }
            }
        });
        fontSizeBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                    fontSizeBox.setText(oldValue);
                } else {
                    fontSizeBox.setText("0");
                }
            }
            else {
                fontSizeBox.setText(newValue);
            }
        });
        scaleBox.textProperty().addListener((observable,oldValue,newValue) -> {
            if(!newValue.matches("[0-9]+(\\.[0-9]+)?")){
                if (oldValue.matches("[0-9]+(\\.[0-9]+)?")) {
                    scaleBox.setText(oldValue);
                } else {
                    scaleBox.setText("100");
                }
            }
            else {
                scaleBox.setText(newValue);
            }
        });

        //set Action on loose focus
        final ActionEvent actionEvent=new ActionEvent();
        leftAnchorBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                leftAnchorBox.fireEvent(actionEvent);
            }
        });
        topAnchorBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                topAnchorBox.fireEvent(actionEvent);
            }
        });
        widthBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                widthBox.fireEvent(actionEvent);
            }
        });
        heightBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                heightBox.fireEvent(actionEvent);
            }
        });
        fontSizeBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                fontSizeBox.fireEvent(actionEvent);
            }
        });
        scaleBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                scaleBox.fireEvent(actionEvent);
            }
        });
        //</editor-fold>


        //<editor-fold desc="aspectratioListner">
        widthListner= (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Double oldHeight= Double.parseDouble(heightBox.getText());
            Double oldWidth= Double.parseDouble(oldValue);
            Double newWidth= Double.parseDouble(newValue);
            heightBox.textProperty().removeListener(heightListner);
            heightBox.setText(String.valueOf((oldHeight*newWidth)/oldWidth));
            heightBox.textProperty().addListener(heightListner);
        };
        heightListner=(observable, oldValue, newValue) -> {
            Double oldWidth= Double.parseDouble(widthBox.getText());
            Double oldHeight= Double.parseDouble(oldValue);
            Double newHeight= Double.parseDouble(newValue);
            widthBox.textProperty().removeListener(widthListner);
            widthBox.setText(String.valueOf((oldWidth*newHeight)/oldHeight));
            widthBox.textProperty().addListener(widthListner);
        };
        aspectRatio.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==false && newValue==true){
                widthBox.textProperty().addListener(widthListner);
                heightBox.textProperty().addListener(heightListner);
            }
            else if(oldValue==true && newValue==false){
                widthBox.textProperty().removeListener(widthListner);
                heightBox.textProperty().removeListener(heightListner);
            }
        });
        //</editor-fold>
        bottomright=getNewRegion();

        //<editor-fold desc="adding font-families">
        java.util.List<String> fontFamilies=Font.getFamilies();
        fontFamilyBox.getItems().addAll(fontFamilies);
        //</editor-fold>
    }

    public void addPage() {
        if (checkifpdf == 1) {
            for (int i = 1; i < (document.getNumberOfPages()); i++) {
                AnchorPane anchorPane = new Layout();
                pages.add(anchorPane);
                Button button = getIndexButton(pages.size() - 1);
                pageList.getItems().add(button);
                display(pages.size() - 1);



                //ImageView image=showpdf1(i);
                StackPane stackPane= getImageBox(showpdf1(i),pdfrec);


                currentAnchorPane.getChildren().add(stackPane);
                setSelectedPane(stackPane);


            }
            checkifpdf=0;
        }
        else {
            addPage(Layout.EMPTY);
        }
    }

    public void addPage(int idx){
        AnchorPane anchorPane = new Layout(idx);
        pages.add(anchorPane);

        Button button = getIndexButton(pages.size() - 1);
        pageList.getItems().add(button);
        pageList.getSelectionModel().clearSelection();
        pageList.getSelectionModel().select(button);
        display(pages.size() - 1);
    }

    private Button getIndexButton(Integer idx){
        Button button=new Button("Pages"+(idx+1));

        button.setOnAction(actionEvent->{
            pageList.getSelectionModel().clearSelection();
            pageList.getSelectionModel().select(actionEvent.getSource());
            display(idx);
        });

        return button;
    }

    private void display(Integer idx){
        if(pageWindow.getContent()!=null){
            pageWindow.getContent().requestFocus();
        }
        if(idx==-1){
            pageWindow.setContent(null);
            currentAnchorPane=null;
        }
        else {
//            System.out.println("display "+idx);
            currentAnchorPane= pages.get(idx);
            Group zoomGroup=new Group(currentAnchorPane);
            Group contentGroup=new Group(zoomGroup);
            pageWindow.setContent(contentGroup);
        }
    }


    //<editor-fold desc="Adding Components">
    @FXML public void addTextField(){
        addTextField(new Rectangle(100.0,300.0,412.0,468.0));
    }
    @FXML public void addTextField(Rectangle rec){

        StackPane box=getTextBox(rec);

        currentAnchorPane.getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addTitleField(){
        addTitleField(new Rectangle(100.0,100.0,412.0,100.0));
    }
    @FXML public void addTitleField(Rectangle rec){

        StackPane box=getTitleBox(rec);

        currentAnchorPane.getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addImageBox(){
        addImageBox(new Rectangle(0.0,0.0,0.0,0.0));
    }
    @FXML public void addImageBox(Rectangle rec){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image File","*.png","*.jpg","*.ico","*.jpeg","bmp"),
                new FileChooser.ExtensionFilter("All","*.*")
        );
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if(file!=null) {
            StackPane stackPane = getImageBox(new Image(file.toURI().toString()),rec);

            currentAnchorPane.getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addVideoPlayer(){
        addVideoPlayer(new Rectangle(0.0,0.0,0.0,0.0));
    }
    @FXML public void addVideoPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video File","*.mp4","*.flv"),
                new FileChooser.ExtensionFilter("All","*.*")
        );
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if (file != null) {
            StackPane stackPane = getVideoBox(new Media(file.toURI().toString()),rec);

            currentAnchorPane.getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addAudioPlayer(){
        addAudioPlayer(new Rectangle(0.0,0.0,400.0,40.0));
    }
    @FXML public void addAudioPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio File","*.mp3","*.wav"),
                new FileChooser.ExtensionFilter("All","*.*")
        );
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if (file != null) {
            StackPane stackPane = getAudioBox(new Media(file.toURI().toString()),rec);

            currentAnchorPane.getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addPdfViewer(){
        addPdfViewer(new Rectangle(0.0,0.0,0.0,0.0));
    }
    @FXML public void addPdfViewer(Rectangle rec) {
        pdfrec=rec;
        checkifpdf=1;
        Image pdf=showpdf(0);
        if(rec.getWidth()==0.0){
            rec.setWidth(pdf.getWidth());
            rec.setHeight(pdf.getHeight());
        }
        StackPane stackPane= getImageBox(pdf,rec);
        //((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
        //((ImageView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

        currentAnchorPane.getChildren().add(stackPane);
        setSelectedPane(stackPane);
        addPage();
    }

    @FXML public void addWevView(){
        addWebView(new Rectangle(0.0,0.0,1000.0,700.0));
    }
    @FXML public void addWebView(Rectangle rec){
        String url=PopUp.getUrl("enter Url");
        if(url!=null){
            StackPane stackPane=getBrowser(url,rec);
            if(stackPane!=null) {
                currentAnchorPane.getChildren().add(stackPane);
                setSelectedPane(stackPane);
            }
            else {
                PopUp.display("Error","not a url");
            }
        }
    }
//</editor-fold>

    public void changeTextColor(){
        if(selectedPane!=null){
            if(selectedPane.getChildren().get(0).getClass()==TextField.class){
                TextField textField= (TextField) selectedPane.getChildren().get(0);
                double red=fontColorBox.getValue().getRed()*255;
                double green=fontColorBox.getValue().getGreen()*255;
                double blue=fontColorBox.getValue().getBlue()*255;
                String style=textField.getStyle();
                if(style.contains("-fx-text-fill:"))
                    style=style.replaceFirst("-fx-text-fill:[^;]*;","-fx-text-fill: rgb("+red+","+green+","+blue+");");
                else
                    style=style.concat("-fx-text-fill: rgb("+red+","+green+","+blue+");");
                textField.setStyle(style);
            }
            else if(selectedPane.getChildren().get(0).getClass()==TextArea.class){
                TextArea textArea= (TextArea) selectedPane.getChildren().get(0);
                double red=fontColorBox.getValue().getRed()*255;
                double green=fontColorBox.getValue().getGreen()*255;
                double blue=fontColorBox.getValue().getBlue()*255;
                String style=textArea.getStyle();
                if(style.contains("-fx-text-fill:"))
                    style=style.replaceFirst("-fx-text-fill:[^;]*;","-fx-text-fill: rgb("+red+","+green+","+blue+");");
                else
                    style=style.concat("-fx-text-fill: rgb("+red+","+green+","+blue+");");
                textArea.setStyle(style);
            }
        }
    }
    public void changeTextSize(){
        if(selectedPane!=null){
            if(selectedPane.getChildren().get(0).getClass()==TextField.class){
                TextField textField= (TextField) selectedPane.getChildren().get(0);
                String style=textField.getStyle();
//                System.out.println("old: "+style);
                if(style.contains("-fx-font-size"))
                    style=style.replaceFirst("-fx-font-size:[^;]*;","-fx-font-size: "+fontSizeBox.getText()+";");
                else
                    style=style.concat("-fx-font-size: "+fontSizeBox.getText()+";");
//                System.out.println("new: "+style);
                textField.setStyle(style);
            }
            else if(selectedPane.getChildren().get(0).getClass()==TextArea.class){
                TextArea textArea= (TextArea) selectedPane.getChildren().get(0);
                String style=textArea.getStyle();
                if(style.contains("-fx-font-size"))
                    style=style.replaceFirst("-fx-font-size:[^;]*;","-fx-font-size: "+fontSizeBox.getText()+";");
                else
                    style=style.concat("-fx-font-size: "+fontSizeBox.getText()+";");
                textArea.setStyle(style);
            }
        }

    }
    public void changeTextFamily(){
        if(selectedPane!=null){
            String style2="-fx-font-family: \""+fontFamilyBox.getSelectionModel().getSelectedItem()+"\";";
            if(selectedPane.getChildren().get(0).getClass()==TextField.class){
                TextField textField= (TextField) selectedPane.getChildren().get(0);
                String style=textField.getStyle();
//                System.out.println("old: "+style);
                if(style.contains("-fx-font-family"))
                    style=style.replaceFirst("-fx-font-family:[^;]*;",style2);
                else
                    style=style.concat(style2);
//                System.out.println("new: "+style);
                textField.setStyle(style);
            }
            else if(selectedPane.getChildren().get(0).getClass()==TextArea.class){
                TextArea textArea= (TextArea) selectedPane.getChildren().get(0);
                String style=textArea.getStyle();
//                String style2="-fx-font-family: "+fontFamilyBox.getSelectionModel().getSelectedItem()+";";
//                System.out.println("old: "+style);
                if(style.contains("-fx-font-family"))
                    style=style.replaceFirst("-fx-font-family:[^;]*;",style2);
                else
                    style=style.concat(style2);
                textArea.setStyle(style);
            }
        }

    }
    public void changeTextAlignment(){
        if(selectedPane!=null){
            String style2="-fx-text-alignment: "+textAlignBox.getSelectionModel().getSelectedItem()+";";
            if(selectedPane.getChildren().get(0).getClass()==TextField.class){
                TextField textField= (TextField) selectedPane.getChildren().get(0);
                String style=textField.getStyle();
//                System.out.println("old: "+style);
                if(style.contains("-fx-text-alignment"))
                    style=style.replaceFirst("-fx-text-alignment:[^;]*;",style2);
                else
                    style=style.concat(style2);
//                System.out.println("new: "+style);
                textField.setStyle(style);
            }
            else if(selectedPane.getChildren().get(0).getClass()==TextArea.class){
                TextArea textArea= (TextArea) selectedPane.getChildren().get(0);
                String style=textArea.getStyle();
//                String style2="-fx-font-family: "+fontFamilyBox.getSelectionModel().getSelectedItem()+";";
//                System.out.println("old: "+style);
                if(style.contains("-fx-text-alignment: "))
                    style=style.replaceFirst("-fx-text-alignment: [^;]*;",style2);
                else
                    style=style.concat(style2);
                textArea.setStyle(style);
            }
        }
    }

    public void changeScale(){
        if(currentAnchorPane!=null) {
//                System.out.println("scaleBoxAction"+ scaleBox.getText());
            if (scaleBox.getText().equals("")) {
//                System.out.print("empty" + scaleBox.getText());
                scaleBox.setText("100");
                scaleBox.fireEvent(new ActionEvent());
            } else {
                Scale scale = new Scale();
                scale.setPivotX(0);
                scale.setPivotY(0);
                scale.setX(Double.parseDouble(scaleBox.getText()) / 100.0);
                scale.setY(Double.parseDouble(scaleBox.getText()) / 100.0);
                currentAnchorPane.getParent().getTransforms().clear();
                currentAnchorPane.getParent().getTransforms().add(scale);
            }
        }
    }


    public Document getDocument(){
        //playarea.contentProperty().unbind();
        //playarea.contentProperty().bind(image);
        Document document = new Document();
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF","*.pdf"),
                    new FileChooser.ExtensionFilter("All","*.*")
            );
            File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
            document.setFile(file.getAbsolutePath());
        } catch (PDFException | PDFSecurityException | IOException ex) {
            System.out.println(ex.toString());
        }
        //totalPages.setText("/"+String.valueOf(document.getNumberOfPages()));
        return document;
    }
    public  Image showpdf(Integer page) {
        try {

            float scale = 1.0f;
            float rotation = 0f;
            document=getDocument();
            // Paint each pages content to an image

            java.awt.Image image1 = document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN,200, rotation, scale);
            BufferedImage bufferedImage;
            if(image1 instanceof BufferedImage){
                bufferedImage= (BufferedImage) image1;
            }
            else{
                bufferedImage=new BufferedImage(image1.getWidth(null),image1.getHeight(null),BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D bGr=bufferedImage.createGraphics();
                bGr.drawImage(bufferedImage,0,0,null);
                bGr.dispose();
            }

            return SwingFXUtils.toFXImage(bufferedImage,null);

        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
            return null;

        }


    }
    public  Image showpdf1(Integer page) {
        try {

            float scale = 1.0f;
            float rotation = 0f;
            // document=getDocument();
            // Paint each pages content to an image

            java.awt.Image image1 = document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN,200, rotation, scale);
            BufferedImage bufferedImage;
            if(image1 instanceof BufferedImage){
                bufferedImage= (BufferedImage) image1;
            }
            else{
                bufferedImage=new BufferedImage(image1.getWidth(null),image1.getHeight(null),BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D bGr=bufferedImage.createGraphics();
                bGr.drawImage(bufferedImage,0,0,null);
                bGr.dispose();
            }

            return SwingFXUtils.toFXImage(bufferedImage,null);

        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
            return null;

        }


    }

    public static void setSelectedPane(Pane pane){
        //System.out.println("Selecting "+pane);
        if( selectedPane!=null) {
            selectedPane.setStyle("-fx-border-style: dashed");
        }
        selectedPane = pane;
        if(pane!=null) {
            selectedPane.setStyle("-fx-border-color: black");
        }
        selectePaneChanged.setValue(true);
    }
    private void regionstoSelectedPane(){
        if(bottomright.getParent()!=null) {
            ((AnchorPane) bottomright.getParent()).getChildren().removeAll(bottomright);
        }
        if(selectedPane!=null){
            AnchorPane.setTopAnchor(bottomright,AnchorPane.getTopAnchor(selectedPane));
            AnchorPane.setLeftAnchor(bottomright,AnchorPane.getLeftAnchor(selectedPane));
            //</editor-fold>

            //<editor-fold desc="binding bottomright region">
            bottomright.setTranslateX(Double.parseDouble(widthBox.getText()));
            bottomright.setTranslateY(Double.parseDouble(heightBox.getText()));
            StringProperty sp=widthBox.textProperty();
            DoubleProperty dp=bottomright.translateXProperty();
            StringConverter<Number> converter=new NumberStringConverter();
            Bindings.bindBidirectional(sp,dp,converter);
            sp=heightBox.textProperty();
            dp=bottomright.translateYProperty();
            Bindings.bindBidirectional(sp,dp,converter);
            //</editor-fold>

            //<editor-fold desc="binding rest regions">

            //</editor-fold>

            ((AnchorPane)selectedPane.getParent()).getChildren().addAll(bottomright);
        }
    }
    private void bindPropertiesToSelectedPane() throws InterruptedException {

        if(selectedPane==null){
            display(-1);
            leftAnchorBox.setText("");
            topAnchorBox.setText("");
            heightBox.setText("");
            widthBox.setText("");
            leftAnchorBox.setOnAction(null);
            topAnchorBox.setOnAction(null);
            heightBox.setOnAction(null);
            widthBox.setOnAction(null);
            leftAnchorBox.setDisable(true);
            topAnchorBox.setDisable(true);
            heightBox.setDisable(true);
            widthBox.setDisable(true);
            fontColorBox.setValue(Color.color(0,0,0,1));
            fontSizeBox.setText("18");
            fontFamilyBox.getSelectionModel().select("System");
            return;
        }
        else{
            leftAnchorBox.setDisable(false);
            topAnchorBox.setDisable(false);
            heightBox.setDisable(false);
            widthBox.setDisable(false);
        }
//<editor-fold desc="AnchorProperty">
        leftAnchorBox.setText(String.valueOf(AnchorPane.getLeftAnchor(selectedPane)));
        leftAnchorBox.setOnAction(event -> {
            AnchorPane.setLeftAnchor(selectedPane,Double.parseDouble(leftAnchorBox.getText()));
        });

        topAnchorBox.setText(String.valueOf(AnchorPane.getTopAnchor(selectedPane)));
        topAnchorBox.setOnAction(event -> {
            AnchorPane.setTopAnchor(selectedPane,Double.parseDouble(topAnchorBox.getText()));
        });
//</editor-fold >

        boolean flag=false;
        if(aspectRatio.isSelected()){
            flag=true;
            aspectRatio.setSelected(false);
        }

        Node child=selectedPane.getChildren().get(0);
        if(child.getClass()==TextField.class){
            componentPropertyBox.setVisible(true);
            //<editor-fold>
            TextField textField= (TextField) selectedPane.getChildren().get(0);
            widthBox.setText(String.valueOf(textField.getWidth()));
            widthBox.setOnAction(event -> {
                textField.setMaxWidth(Double.parseDouble(widthBox.getText()));
                textField.setMinWidth(Double.parseDouble(widthBox.getText()));
                textField.setMaxHeight(Double.parseDouble(heightBox.getText()));
                textField.setMinHeight(Double.parseDouble(heightBox.getText()));
            });
            heightBox.setText(String.valueOf(textField.getHeight()));
            heightBox.setOnAction(event -> {
                textField.setMaxWidth(Double.parseDouble(widthBox.getText()));
                textField.setMinWidth(Double.parseDouble(widthBox.getText()));
                textField.setMaxHeight(Double.parseDouble(heightBox.getText()));
                textField.setMinHeight(Double.parseDouble(heightBox.getText()));
            });
            String style=textField.getStyle();
            System.out.println("style= "+style);
            String[] styleParts=style.split(";");
            for(String each :styleParts){
                System.out.println("each= "+each);
                String[] pair=each.split(" ",2);
                for(String s:pair){
                    System.out.println("s= "+s);
                }
                switch (pair[0]){
                    case "-fx-font-family:":
                        fontFamilyBox.getSelectionModel().select(pair[1].substring(1,pair[1].length()-1));
                        break;
                    case "-fx-font-size:":
                        fontSizeBox.setText(pair[1]);
                        break;
                    case "-fx-text-fill:":
                        String[] rgb=pair[1].substring(4,pair[1].length()-1).split(",");
                        fontColorBox.setValue(new Color(Double.parseDouble(rgb[0])/255,Double.parseDouble(rgb[1])/255,Double.parseDouble(rgb[2])/255,1));
                        break;
                }
            }

//            fontSizeBox.setText(String.valueOf(textField.getFont().getSize()));
//            fontSizeBox.setOnAction(event -> {
//                if(fontSizeBox.getText().equals("")){
//                    fontSizeBox.setText(String.valueOf(textField.getFont().getSize()));
//                }
//                else {
//                    textField.setFont(javafx.scene.text.Font.font(
//                            textField.getFont().getFamily(),
//                            Double.parseDouble(fontSizeBox.getText())
//                    ));
//                }
//            });

//            if(fontFamilyBox.getItems().contains(textField.getFont().getFamily())) {
//                fontFamilyBox.getSelectionModel().select(textField.getFont().getFamily());
//            }
//            else{
//                fontFamilyBox.getSelectionModel().clearSelection();
//            }
//            fontFamilyBox.setOnAction(event -> {
//                textField.setFont(javafx.scene.text.Font.font(
//                        (String) fontFamilyBox.getSelectionModel().getSelectedItem(),
//                        textField.getFont().getSize()
//                ));
//            });
            //</editor-fold>
        }
        else if(child.getClass()==TextArea.class){
            componentPropertyBox.setVisible(true);
            //<editor-fold>
            TextArea textArea= (TextArea) selectedPane.getChildren().get(0);
            widthBox.setText(String.valueOf(textArea.getWidth()));
            widthBox.setOnAction(event -> {
                textArea.setMaxWidth(Double.parseDouble(widthBox.getText()));
                textArea.setMinWidth(Double.parseDouble(widthBox.getText()));
                textArea.setMaxHeight(Double.parseDouble(heightBox.getText()));
                textArea.setMinHeight(Double.parseDouble(heightBox.getText()));
            });
            heightBox.setText(String.valueOf(textArea.getHeight()));
            heightBox.setOnAction(event -> {
                textArea.setMaxWidth(Double.parseDouble(widthBox.getText()));
                textArea.setMinWidth(Double.parseDouble(widthBox.getText()));
                textArea.setMaxHeight(Double.parseDouble(heightBox.getText()));
                textArea.setMinHeight(Double.parseDouble(heightBox.getText()));
            });
            String style=textArea.getStyle();
            System.out.println("style= "+style);
            String[] styleParts=style.split(";");
            for(String each :styleParts){
                System.out.println("each= "+each);
                String[] pair=each.split(" ",2);
                for(String s:pair){
                    System.out.println("s= "+s);
                }
                switch (pair[0]){
                    case "-fx-font-family:":
                        fontFamilyBox.getSelectionModel().select(pair[1].substring(1,pair[1].length()-1));
                        break;
                    case "-fx-font-size:":
                        fontSizeBox.setText(pair[1]);
                        break;
                    case "-fx-text-fill:":
                        String[] rgb=pair[1].substring(4,pair[1].length()-1).split(",");
                        fontColorBox.setValue(new Color(Double.parseDouble(rgb[0])/255,Double.parseDouble(rgb[1])/255,Double.parseDouble(rgb[2])/255,1));
                        break;
                }
            }
//            fontSizeBox.setText(String.valueOf(textArea.getFont().getSize()));
//            fontSizeBox.setOnAction(event -> {
//                if(fontSizeBox.getText().equals("")){
//                    fontSizeBox.setText(String.valueOf(textArea.getFont().getSize()));
//                }
//                else {
//                    textArea.setFont(javafx.scene.text.Font.font(
//                            textArea.getFont().getFamily(),
//                            Double.parseDouble(fontSizeBox.getText())
//                    ));
//                }
//            });
//            if(fontFamilyBox.getItems().contains(textArea.getFont().getFamily())) {
//                fontFamilyBox.getSelectionModel().select(textArea.getFont().getFamily());
//            }
//            else{
//                fontFamilyBox.getSelectionModel().clearSelection();
//            }
//            fontFamilyBox.setOnAction(event -> {
//                textArea.setFont(javafx.scene.text.Font.font(
//                        (String) fontFamilyBox.getSelectionModel().getSelectedItem(),
//                        textArea.getFont().getSize()
//                ));
//            });
            //</editor-fold>
        }
        else if(child.getClass()==ImageView.class){
            componentPropertyBox.setVisible(false);
            //<editor-fold>
            ImageView imageView= (ImageView) selectedPane.getChildren().get(0);
//            System.out.println(" image "+imageView.getFitWidth()+" "+imageView.getFitHeight());
            widthBox.setText(String.valueOf(imageView.getFitWidth()));
            widthBox.setOnAction(event -> {
                imageView.setFitWidth(Double.parseDouble(widthBox.getText()));
                imageView.setFitHeight(Double.parseDouble(heightBox.getText()));
            });
            heightBox.setText(String.valueOf(imageView.getFitHeight()));
            heightBox.setOnAction(event -> {
                imageView.setFitWidth(Double.parseDouble(widthBox.getText()));
                imageView.setFitHeight(Double.parseDouble(heightBox.getText()));
            });
            //</editor-fold>
        }
        else if(child.getClass()==MediaView.class){
            componentPropertyBox.setVisible(false);
            //<editor-fold>
            MediaView mediaView= (MediaView) selectedPane.getChildren().get(0);
            widthBox.setText(String.valueOf(mediaView.getFitWidth()));
            widthBox.setOnAction(event -> {
                mediaView.setFitWidth(Double.parseDouble(widthBox.getText()));
                mediaView.setFitHeight(Double.parseDouble(heightBox.getText()));
            });
            heightBox.setText(String.valueOf(mediaView.getFitHeight()));
            heightBox.setOnAction(event -> {
                mediaView.setFitWidth(Double.parseDouble(widthBox.getText()));
                mediaView.setFitHeight(Double.parseDouble(heightBox.getText()));
            });
            //</editor-fold>
        }
        else if(child.getClass()== WebView.class){
            componentPropertyBox.setVisible(false);
            //<editor-fold>
            WebView webView= (WebView) selectedPane.getChildren().get(0);
            widthBox.setText(String.valueOf(webView.getWidth()));
            widthBox.setOnAction(event -> {
                webView.setMinWidth(Double.parseDouble(widthBox.getText()));
                webView.setMaxWidth(Double.parseDouble(widthBox.getText()));
                webView.setMinHeight(Double.parseDouble(heightBox.getText()));
                webView.setMaxHeight(Double.parseDouble(heightBox.getText()));
            });
            heightBox.setText(String.valueOf(webView.getHeight()));
            heightBox.setOnAction(event -> {
                webView.setMinWidth(Double.parseDouble(widthBox.getText()));
                webView.setMaxWidth(Double.parseDouble(widthBox.getText()));
                webView.setMinHeight(Double.parseDouble(heightBox.getText()));
                webView.setMaxHeight(Double.parseDouble(heightBox.getText()));
            });
            //</editor-fold>
        }

        if(flag){
            aspectRatio.setSelected(true);
        }
    }

    public void tempFunction(){
        fontColorBox.setOnAction(event -> {
            System.out.println(fontColorBox.getValue());
        });
        System.out.println(pages);
    }
}