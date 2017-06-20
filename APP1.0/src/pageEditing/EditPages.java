package pageEditing;

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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import sample.PopUp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static pageEditing.Element.*;


/**
 * Created by ghost on 14/6/17.
 */
public class EditPages implements Initializable{

    private static BooleanProperty selectePaneChanged;
    public static Pane selectedPane;

    public static double orgSceneX;
    public static double orgSceneY;
    public static double orgLeftAnchor;
    public static double orgTopAnchor;

    public static ArrayList<AnchorPane> pages;
    public int checkifpdf=0;
    public Document document;
    @FXML
    ListView pageList;
    @FXML
    ListView paneList;
    @FXML
    ScrollPane pageWindow;
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

    static Region topleft;
    static Region topcenter;
    static Region topright;
    static Region left;
    static Region right;
    static Region bottomleft;
    static Region bottomcenter;
    static Region bottomright;
    static Region rotation;


    private ChangeListener<String> widthListner;
    private ChangeListener<String> heightListner;

    public static void setToFront(Node n){
        try {
            n.toFront();
            if (n.getClass() == TextField.class)
                n.getParent().toFront();
            while (n.getParent().getClass() != AnchorPane.class) {
                n.toFront();
                n = n.getParent();
            }
            n.toFront();
            EditPages.setSelectedPane((Pane) n);
        }
        catch (Exception e){
            PopUp.display("Error!",e.toString());
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
            double offsetX=e.getSceneX()- EditPages.orgSceneX;
            double offsetY=e.getSceneY()- EditPages.orgSceneY;
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
        for(AnchorPane each : pages){
            each=makePageUneditable(each);
        }
        return pages;
    }

    private static AnchorPane makePageEditable(AnchorPane page){
        for(Node each : page.getChildren()){
            enableMovable(each);
            Node node= ((StackPane) each).getChildren().get(0);
            //System.out.println(each+" "+node);
            if(node.getClass()==TextField.class){
                node=Element.enableEdit((TextField) node);
            }
            else if(node.getClass()==TextArea.class){
                node=Element.enableEdit((TextArea) node);
            }
            //System.out.println("enable ended");
        }
        return page;
    }

    private static AnchorPane makePageUneditable(AnchorPane page){
        for(Node each : page.getChildren()){
            disableMovable(each);
            each.setStyle("-fx-background-color: transparent;" +
                    "-fx-border-width: 0px");
            if(each.getClass()==StackPane.class) {
                Node node = ((StackPane) each).getChildren().get(0);
                //System.out.println(each+" "+node);
                if (node.getClass() == TextField.class) {
                    node = Element.disableEdit((TextField) node);
                } else if (node.getClass() == TextArea.class) {
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
            selectedPane = null;
        }
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
                    bindPropertiesToSelectedPane();
                    regionstoSelectedPane();
                }
            }
        });
//</editor-fold>

        pageList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for(AnchorPane each : pages){
            Button indexButton=getIndexButton(pages.indexOf(each));
            pageList.getItems().add(indexButton);
        }
        try {
            display(0);
        }catch (Exception e){
            pageWindow.setContent(new Text("no page to display"));
        }

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
        topleft=getNewRegion();
        bottomright=getNewRegion();
        /*try{
            enableMovable(topleft);
            enableMovable(topcenter);
            enableMovable(topright);
            enableMovable(left);
            enableMovable(right);
            enableMovable(bottomleft);
            enableMovable(bottomcenter);
            enableMovable(bottomright);

        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void addPage() {
        if (checkifpdf == 1) {
            for (int i = 1; i < (document.getNumberOfPages()); i++) {
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setPrefWidth(1024);
                anchorPane.setPrefHeight(768);
                anchorPane.setStyle("-fx-background-color: green");
                pages.add(anchorPane);



                ImageView image=showpdf1(i);
                StackPane stackPane= getPdfBox();
                //ImageView imageView=(ImageView) image;
                stackPane.getChildren().add(image);

                //((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
                //((ImageView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

                ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
                setSelectedPane(stackPane);

                Button button = getIndexButton(pages.size() - 1);
                pageList.getItems().add(button);
                display(pages.size() - 1);
            }
            checkifpdf=0;
        }
        else {
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setPrefWidth(1024);
            anchorPane.setPrefHeight(768);
            anchorPane.setStyle("-fx-background-color: green");
            pages.add(anchorPane);

            Button button = getIndexButton(pages.size() - 1);
            pageList.getItems().add(button);
            pageList.getSelectionModel().clearSelection();
            pageList.getSelectionModel().select(button);
            display(pages.size() - 1);
        }
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
        pageWindow.setContent(pages.get(idx));
    }

    public void deletePage(){
        ArrayList<AnchorPane> tmp=new ArrayList<>();
        for(Integer each : (ObservableList<Integer>)pageList.getSelectionModel().getSelectedIndices()){
            tmp.add(pages.get(each));
        }
        pages.removeAll(tmp);
        pageList.getItems().removeAll(pageList.getSelectionModel().getSelectedItems());
    }

//<editor-fold desc="Adding Components">
    @FXML public void addTextField(){
        addTextField(new Rectangle(100.0,300.0,412.0,468.0));
    }
    @FXML public void addTextField(Rectangle rec){

        StackPane box=getTextBox();
        ((TextArea)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextArea)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());

        ((AnchorPane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addTitleField(){
        addTitleField(new Rectangle(100.0,100.0,412.0,100.0));
    }
    @FXML public void addTitleField(Rectangle rec){

        StackPane box=getTitleBox();
        ((TextField)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextField)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());

        ((AnchorPane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addImageBox(){
        addImageBox(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addImageBox(Rectangle rec){
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image","png","jpg","ico"));
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if(file!=null) {
            StackPane stackPane = getImageBox(new Image(file.toURI().toString()));
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getWidth());
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());

            AnchorPane.setLeftAnchor(stackPane,rec.getX());
            AnchorPane.setTopAnchor(stackPane,rec.getY());
            ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addVideoPlayer(){
        addVideoPlayer(new Rectangle(0.0,0.0,400.0,300.0));
    }
    @FXML public void addVideoPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if (file != null) {
            StackPane stackPane = getVideoBox(new Media(file.toURI().toString()));

            ((MediaView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
            ((MediaView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

            AnchorPane.setLeftAnchor(stackPane,rec.getX());
            AnchorPane.setTopAnchor(stackPane,rec.getY());
            ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addPdfViewer(){
        addPdfViewer(new Rectangle(0.0,0.0,100.0,200.0));
    }
    @FXML public void addPdfViewer(Rectangle rec) {
        checkifpdf=1;
        ImageView image=showpdf(0);

        StackPane stackPane= getPdfBox();
        //ImageView imageView=(ImageView) image;
        stackPane.getChildren().add(image);

        ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
        ((ImageView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

        AnchorPane.setLeftAnchor(stackPane,rec.getX());
        AnchorPane.setTopAnchor(stackPane,rec.getY());
        ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
        setSelectedPane(stackPane);
        addPage();
    }
//</editor-fold>


    public Document getDocument(){
        //playarea.contentProperty().unbind();
        //playarea.contentProperty().bind(image);
        Document document = new Document();
        try {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
            document.setFile(file.getAbsolutePath());
        } catch (PDFException | PDFSecurityException | IOException ex) {
            System.out.println(ex.toString());
        }
        //totalPages.setText("/"+String.valueOf(document.getNumberOfPages()));
        return document;
    }
    public  ImageView showpdf(Integer page) {
    ImageView image= new ImageView();
        try {

            float scale = 1.0f;
            float rotation = 0f;
             document=getDocument();
            // Paint each pages content to an image

            BufferedImage image1 = (BufferedImage) document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN,200, rotation, scale);

            WritableImage fxImage = SwingFXUtils.toFXImage(image1, null);

            if (image != null) {
                image.setImage(fxImage);
            } else {
                image=new ImageView(fxImage);
            }
            //Image image2=(Image)image1;

            //Clean up
            image1.flush();
            return image;

        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
            return null;

        }


    }

    public  ImageView showpdf1(Integer page) {
        ImageView image= new ImageView();
        try {

            float scale = 1.0f;
            float rotation = 0f;
            //document=getDocument();
            // Paint each pages content to an image

            BufferedImage image1 = (BufferedImage) document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN,200, rotation, scale);

            WritableImage fxImage = SwingFXUtils.toFXImage(image1, null);

            if (image != null) {
                image.setImage(fxImage);
            } else {
                image=new ImageView(fxImage);
            }
            //Image image2=(Image)image1;

            //Clean up
            image1.flush();
            return image;

        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
            return null;

        }


    }

    public static void setSelectedPane(Pane pane){
//        System.out.println("Selecting "+pane.toString());
        if( selectedPane!=null) {
            selectedPane.setStyle("-fx-border-style: dashed");
        }
        selectedPane=pane;
        selectedPane.setStyle("-fx-border-color: black");
        selectePaneChanged.setValue(true);
        //setting properties;
    }

    private void regionstoSelectedPane(){
        try {
            ((AnchorPane) topleft.getParent()).getChildren().removeAll(topleft, topcenter, topright, left, right, bottomleft, bottomcenter, bottomright);
        }catch (Exception e) {
            //e.printStackTrace();
        }
        try{
            //AnchorPane.setTopAnchor(topleft,AnchorPane.getTopAnchor(selectedPane));
            //AnchorPane.setLeftAnchor(topleft,AnchorPane.getLeftAnchor(selectedPane));
            //<editor-fold desc="repeat for all regions">
            /*AnchorPane.setTopAnchor(topcenter,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(topcenter,AnchorPane.getLeftAnchor(selectedPane)+1);
            AnchorPane.setTopAnchor(topright,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(topright,AnchorPane.getLeftAnchor(selectedPane)+1);
            AnchorPane.setTopAnchor(left,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(left,AnchorPane.getLeftAnchor(selectedPane)+1);
            AnchorPane.setTopAnchor(right,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(right,AnchorPane.getLeftAnchor(selectedPane)+1);
            AnchorPane.setTopAnchor(bottomleft,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(bottomleft,AnchorPane.getLeftAnchor(selectedPane)+1);
            AnchorPane.setTopAnchor(bottomcenter,AnchorPane.getTopAnchor(selectedPane)+1);
            AnchorPane.setLeftAnchor(bottomcenter,AnchorPane.getLeftAnchor(selectedPane)+1);
            */AnchorPane.setTopAnchor(bottomright,AnchorPane.getTopAnchor(selectedPane));
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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bindPropertiesToSelectedPane() {

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

        Node child=selectedPane.getChildren().get(0);
        if(child.getClass()==TextField.class){
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
            //</editor-fold>
        }
        else if(child.getClass()==TextArea.class){
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
            //</editor-fold>
        }
        else if(child.getClass()==ImageView.class){
            //<editor-fold>
            ImageView imageView= (ImageView) selectedPane.getChildren().get(0);
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
    }

    public void tempFunction(){
        System.out.println(pages);
    }
}