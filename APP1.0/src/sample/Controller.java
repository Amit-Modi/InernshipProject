package sample;

import course.Chapter;
import course.Course;
import course.Topic;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;
import savedCourse.Converter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Controller implements Initializable{

    private  ObjectProperty<ImageView> image=new ObjectPropertyBase<ImageView>() {
        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    };
    private MenuButton b1;
    private LinkedHashMap<MenuItem,Pair<Integer,Pair<MenuButton,String>>> mapChapter;
    private LinkedHashMap<MenuButton,Pair<String,Pair<String,String>>> chapterno;
    private Document document;
    private Duration duration;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture;
    private MenuItem temp;
    private EventHandler<?> exitFullScreenHandler;
    private String examFilePath;
    private static MenuButton checkChapNo,selectedChapter;

    public ImageView buttonImage1,buttonImage2;
    public Image image1,image2;
    public File file;

    @FXML
    VBox topBar;
    @FXML
    MenuBar menuBar;
    @FXML
    HBox toolBar;
    @FXML
    ScrollPane playarea;
    @FXML
    ScrollPane textbox;
    @FXML
    TextArea notes;
    @FXML
    MediaView mediaView;
    @FXML
    HBox mediaContainer;
    @FXML
    HBox lectureContainer;
    @FXML
    Pane scene;
    @FXML
    ScrollPane scrollLeftMenu;
    @FXML
    ListView<MenuButton> leftMenu;
    @FXML
    BorderPane totalContainer;
    @FXML
    VBox playAreaContainer;
    @FXML
    VBox playerContainer;
    @FXML
    HBox buttonContainer;
    @FXML
    HBox progressContainer;
    @FXML
    ProgressBar mediaProgressBar;
    @FXML
    Button playButton;
    @FXML
    Slider timeSlider;
    @FXML
    Label playTime;
    @FXML
    MenuButton volumeButton;
    @FXML
    Slider volumeSlider;
    @FXML
    Button fullScreenButton;
    @FXML
    Label totalPages;
    @FXML
    TextField currentPage;
    @FXML
    Label topicLabel;

    @FXML
    Button nextTopic;
    @FXML
    Button prevTopic;

    @FXML
    Button previousButton;

    @FXML
    Button nextButton;






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshCourse();
        try {
           // File file=new File("Next.jpg");
           URL url=Controller.class.getResource("Next.jpg");
//           System.out.println(url);
            image1 = new Image(url.toURI().toString());
            url=Controller.class.getResource("Previous.jpg");
            image2 = new Image(url.toURI().toString());

            buttonImage1= new ImageView(image1);
            buttonImage1.setFitHeight(20.0);
            buttonImage1.setFitWidth(25.0);
            nextTopic.setGraphic(buttonImage1);

            buttonImage2= new ImageView(image2);
            buttonImage2.setFitHeight(20.0);
            buttonImage2.setFitWidth(25.0);
            prevTopic.setGraphic(buttonImage2);
        }catch (Exception e){
            System.out.println("ErrorLoading images: "+e.toString());
        }
        try{
            currentPage.setOnAction(action->{
                int value=Integer.parseInt(currentPage.getText())-1;
                if(value<Integer.parseInt(totalPages.getText().substring(1))) {
                    display(value);
                    if(value<=0){
                        previousButton.setDisable(true);
                    }
                    else{
                        previousButton.setDisable(false);
                    }
                    if(value==(Integer.parseInt(totalPages.getText().substring(1))-1)){
                        nextButton.setDisable(true);
                    }
                    else{
                        nextButton.setDisable(false);
                    }
                }
            });
            currentPage.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    //System.out.println("current page value changed to "+newValue);
                    if(!newValue.matches("[0-9]*")) {
                        currentPage.setText(oldValue);
                    }
                }
            });
        }catch (Exception e){
            System.out.println("current page property error "+e.toString());
        }

        ContextMenu contextMenu= new ContextMenu();
        MenuItem generalHelp= new MenuItem("Help");
        generalHelp.setOnAction(e->{
            showManual();
        });
        contextMenu.getItems().add(generalHelp);

        playarea.setContextMenu(contextMenu);


    }

    public void refreshCourse() {
        leftMenu.getItems().clear();
        playarea.setContent(null);
        Main.window.setTitle(Main.course.courseName);
        for (Chapter eachChapter : Main.course.chapters) {
            MenuButton menuButton = getNewChapterMenuButton(eachChapter);
            checkChapNo=menuButton;
            menuButton.textProperty().addListener((observable, oldValue, newValue) -> {
                eachChapter.chapterName = newValue;
            });
            Integer idx = leftMenu.getItems().size();
            leftMenu.getItems().add(menuButton);
            for (Topic eachTopic : eachChapter.topics) {
                eachTopic.pages = EditPages.makePagesUneditable(eachTopic.pages);
                addTopicMenuItem(idx, eachTopic);
            }
        }
        if (Main.course != null && !Main.course.chapters.isEmpty() && !Main.course.chapters.get(0).topics.isEmpty()) {
            Main.selectedChapter = 0;
            selectedChapter = leftMenu.getItems().get(0);
            display(Main.course.chapters.get(0).topics.get(0));
        }
        else {
            display(-1);
        }
    }

    public void showChapterContextMenu(){
        try {
            if (leftMenu.getContextMenu().isShowing()) {
                leftMenu.getContextMenu().hide();
            }
            leftMenu.getContextMenu().show(Main.window);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void addChapter(){
        try {
            String chapterName = "Chapter" + String.valueOf(Main.course.chapters.size()+1);
            Chapter newChapter = new Chapter();
            newChapter.chapterName = chapterName;
            newChapter.topics=new ArrayList<>();

            MenuButton menuButton=getNewChapterMenuButton(newChapter);

            menuButton.textProperty().addListener(((observable, oldValue, newValue) -> {
                newChapter.chapterName=newValue;
            }));

            Main.course.chapters.add(newChapter);
            leftMenu.getItems().add(menuButton);
            Main.selectedChapter = Main.course.chapters.size()-1;
            Main.currentTopic=null;
            menuButton.setText(PopUp.getName(menuButton.getText()));
        }catch (Exception e){
            System.out.println("Exception in addChapter");
            e.printStackTrace();
        }
    }

    private MenuButton getNewChapterMenuButton(Chapter chapter){
        MenuButton menuButton = new MenuButton(chapter.chapterName);
        ContextMenu contextMenu=new ContextMenu();
        selectedChapter=menuButton;

        MenuItem addTopic=new MenuItem("Add Topic");
        addTopic.setOnAction(e->{
            int idx=Main.course.chapters.indexOf(chapter);
            addNewTopic(idx);
        });

        MenuItem deleteChapter=new MenuItem("Delete");
        deleteChapter.setOnAction(e->{
            removeChapter(chapter);
        });

        MenuItem renameChapter=new MenuItem("Rename");
        renameChapter.setOnAction(e->{
            menuButton.setText(PopUp.getName(menuButton.getText()));
        });

        MenuItem addVideo= new MenuItem("Add Video");
        addVideo.setOnAction(e->{
            FileChooser fileChooser= new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Video File","*.mp4","*.flv"),
                    new FileChooser.ExtensionFilter("All File","*")
            );

            File file= fileChooser.showOpenDialog(Main.window.getScene().getWindow());
            chapter.media=new Media(file.toURI().toString());
            if(chapter.media!=null) {
                setVideo(chapter.media);
            }
            playarea.setContent(null);
            if(menuButton.getItems().isEmpty()){
                Main.currentTopic=null;
            }
            else {
                ((MenuItem) menuButton.getItems().get(0)).fire();
            }
        });

        MenuItem menuHelp= new MenuItem("Help");
        menuHelp.setOnAction(e->{
            showManual();
        });
        menuButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {

                selectedChapter.setStyle("-fx-background-color: white");
                selectedChapter=menuButton;
                selectedChapter.setStyle("-fx-border-color: rgba(0,0,0,0.99);" +
                        "-fx-border-width: 2px;" +
                        "-fx-padding: 2px;" );
                checkChapNo=menuButton;
                getStudentNotes();
                if(event.getButton()== MouseButton.PRIMARY){
                    //System.out.println((event.getSceneX()-leftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                    if(chapter.media!=null) {
                        setVideo(chapter.media);
                    }

                    playarea.setContent(null);
                    if(menuButton.getItems().isEmpty()){
                        Main.currentTopic=null;
                    }
                    else {
                        display(Main.course.chapters.get(leftMenu.getSelectionModel().getSelectedIndex()).topics.get(0));
                    }

                }

            }



        });

        contextMenu.getItems().addAll(addTopic,deleteChapter,renameChapter,addVideo,menuHelp);
        menuButton.setContextMenu(contextMenu);
        menuButton.setPopupSide(Side.RIGHT);
        menuButton.setOnContextMenuRequested(e->{
            menuButton.getContextMenu().show(Main.window);

        });

        menuButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                menuButton.fire();
                onEntered(menuButton);

            }
        });

        menuButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onExit(menuButton);
            }
        });

        return menuButton;
    }

    private void addNewTopic(Integer idx){

        String topicName="Topic"+String.valueOf(Main.course.chapters.get(idx).topics.size()+1);
        Topic newTopic=new Topic();
        newTopic.topicName=topicName;
        newTopic.pages=new ArrayList<>();
        newTopic.parent=Main.course.chapters.get(idx);
        Main.course.chapters.get(idx).topics.add(newTopic);

        Label label=addTopicMenuItem(idx,newTopic);

        label.setText(PopUp.getName(label.getText()));
    }

    private Label addTopicMenuItem(Integer idx,Topic topic){

        MenuItem menuItem=new MenuItem();
        Label label=new Label(topic.topicName);

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            topic.topicName=newValue;
        });

        ContextMenu contextMenu1=new ContextMenu();

        MenuItem editTopic= new MenuItem("editTopic");
        editTopic.setOnAction(ee->{
            try {
                topic.pages=editPages(topic);
                Main.selectedChapter=idx;
                selectedChapter=leftMenu.getItems().get(idx);
                display(topic);
                //System.out.println("this is returned topic "+topic.pages);
            }catch (Exception exception){
                System.out.println(exception);
            }
        });

        MenuItem deleteTopic= new MenuItem("deleteTopic");
        deleteTopic.setOnAction(ed->{
            removeTopic(topic);
            topic.parent.topics.remove(topic);
            ((MenuButton) leftMenu.getItems().get(idx)).getItems().remove(menuItem);
        });

        MenuItem renameTopic= new MenuItem("renameTopic");
        renameTopic.setOnAction(er->{
            label.setText(PopUp.getName(label.getText()));
        });

        contextMenu1.getItems().addAll(editTopic,deleteTopic,renameTopic);
        label.setContextMenu(contextMenu1);

        label.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton()==MouseButton.SECONDARY){
                   // System.out.println("right mouse presse on "+topic);
                    event.consume();
                }
                else{
                   // System.out.println("left mouse presse on "+topic);
                    Main.selectedChapter=idx;
                    selectedChapter=leftMenu.getItems().get(idx);
                    display(topic);
                }
            }
        });
        menuItem.setGraphic(label);
        ((MenuButton)leftMenu.getItems().get(idx)).getItems().add(menuItem);
        return label;
    }

    private Topic removeTopic(Topic topic){
        playarea.setContent(null);
        Chapter chapter=topic.parent;
        int idx=chapter.topics.indexOf(topic);
        EditPages.removeAllPages(topic);
        if(idx!=0) {
            return chapter.topics.get(idx - 1);
        }
        else {
            if(chapter.topics.size()>0){
                return chapter.topics.get(0);
            }
            else{
                return null;
            }
        }
    }

    private Chapter removeChapter(Chapter chapter){
        playarea.setContent(null);
        chapter.media=null;
        if(mediaView.getMediaPlayer()!=null)
            mediaView.getMediaPlayer().dispose();
        for(Topic each : chapter.topics){
            removeTopic(each);
        }
        chapter.topics.clear();
        int idx=Main.course.chapters.indexOf(chapter);
        leftMenu.getItems().remove(idx);
        Main.course.chapters.remove(chapter);
        if(idx!=0){
            return Main.course.chapters.get(idx-1);
        }
        else{
            if(Main.course.chapters.size()>0){
                return Main.course.chapters.get(0);
            }
            else{
                return null;
            }
        }
    }

    private ArrayList<AnchorPane> editPages(Topic topic) throws Exception{
        try {
            currentPage.setText("0");
            currentPage.fireEvent(new ActionEvent());
            //ArrayList<AnchorPane> clonedPages=EditPages.clonePages(topic.pages);
            EditPages.pages = EditPages.makePagesEditable(topic.pages);
            Parent root = FXMLLoader.load(getClass().getResource("editPages.fxml"));
            Stage newWindow=new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle(topic.topicName);
            newWindow.setScene(new Scene(root));
            newWindow.setOnCloseRequest(e->{
                EditPages.setSelectedPane(null);
            });
            newWindow.showAndWait();
            topic.pages=EditPages.makePagesUneditable(EditPages.pages);
            EditPages.pages=null;
            Main.currentTopic=topic;
        }catch (Exception e){
            PopUp.display("error",e.toString());
            e.printStackTrace();
        }
        return topic.pages;
    }

    private void display(Topic topic) {
        Main.currentTopic=topic;
        if(topic==null){
            totalPages.setText("/0");
            currentPage.setText("0");
        }
        else {
            if(topic.pages.size()==0){
                totalPages.setText("/0");
                currentPage.setText("0");
            }
            else {
                totalPages.setText("/" + topic.pages.size());
                currentPage.setText("1");
            }

            if(Main.course.chapters.get(Main.selectedChapter).topics.indexOf(topic)==0)
                prevTopic.setDisable(true);
            else
                prevTopic.setDisable(false);

            if(Main.course.chapters.get(Main.selectedChapter).topics.indexOf(topic)==Main.course.chapters.get(Main.selectedChapter).topics.size()-1)
                nextTopic.setDisable(true);
            else
                nextTopic.setDisable(false);

        }
        currentPage.fireEvent(new ActionEvent());
    }
    private void display(Integer value){
        try {
            if(value<0 || value>=Main.currentTopic.pages.size()){
                StackPane stackPane=new StackPane(new Text("No pages to display"));
                stackPane.setPrefSize(playarea.getWidth(),playarea.getHeight());
                Group zoomGroup=new Group();
                zoomGroup.getChildren().add(stackPane);
                Group contentGroup=new Group();
                contentGroup.getChildren().add(zoomGroup);
                playarea.setContent(contentGroup);
            }
            else {
                Group zoomGroup=new Group(Main.currentTopic.pages.get(value));
                Group contentGroup=new Group(zoomGroup);
                playarea.setContent(contentGroup);
                final double minscale=(playarea.getWidth()-10)/Main.currentTopic.pages.get(value).getWidth();
                scaleWindow(zoomGroup,0.0,0.0,minscale);
                zoomGroup.setOnScroll(event -> {
                    if(event.isControlDown()){
                        event.consume();
                        final double zoomFactor=Math.max(minscale,((Scale) zoomGroup.getTransforms().get(0)).getX()+event.getDeltaY()/1600.0);
                        scaleWindow(zoomGroup,event.getX(),event.getY(),zoomFactor);
                    }
                });
            }
            Main.currentPage=value;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void scaleWindow(Group zoomGroup, Double pivotX, Double pivotY, Double scaleValue){
        Scale scale = new Scale();
        scale.setPivotX(0.5);
        scale.setPivotY(0.5);
        scale.setX( scaleValue);
        scale.setY( scaleValue);
        zoomGroup.getTransforms().clear();
        zoomGroup.getTransforms().add(scale);
    }

    public void newCourse(){
        Main.course=new Course();
        Main.course.chapters=new ArrayList<>();
        Main.course.courseName="My New Course";
        Main.courseFileLocation=null;
        refreshCourse();
    }
    public void openCourse() throws  Exception{
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Couser File","*.course"),
                new FileChooser.ExtensionFilter("All File","*")
        );
        File file=fileChooser.showOpenDialog(Main.window);
        if(file!=null){
            Main.courseFileLocation=file.getAbsolutePath();
            FileInputStream fis=new FileInputStream(Main.courseFileLocation);
            ObjectInputStream ois=new ObjectInputStream(fis);
            savedCourse.Course savedCourse=(savedCourse.Course)ois.readObject();
            Main.course= Converter.convertToCourse(savedCourse);
            refreshCourse();
        }
    }

    public void saveAs() throws Exception{
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Couser File","*.course"),
                new FileChooser.ExtensionFilter("All File","*")
        );
        File file=fileChooser.showSaveDialog(Main.window);
        if(file!=null) {
            Main.courseFileLocation = file.toString();
            saveCourse();
        }
    }
    public void saveCourse() throws Exception{
        if(Main.courseFileLocation==null){
            saveAs();
        }
        else{
            FileOutputStream fos = new FileOutputStream(Main.courseFileLocation);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Converter.convertToSavableCourse(Main.course));
            oos.close();
        }
    }

    public void onExit(MenuButton menuButton) {
        if(checkChapNo!=menuButton) {
            if(menuButton!=selectedChapter)
                menuButton.setStyle("-fx-background-color: white");
            else
                menuButton.setStyle("-fx-border-color: rgba(0,0,0,0.99);" +
                        "-fx-border-width: 2px;" +
                        "-fx-padding: 2px;");
        }
    }

    public void onEntered(MenuButton menuButton){

        if(checkChapNo!=menuButton) {
            if(checkChapNo.isShowing())
                checkChapNo.hide();
            //selectedChapter.hide();
            if (checkChapNo != selectedChapter)
                //checkChapNo.hide();
                checkChapNo.setStyle("-fx-background-color: white");
        }

        if(menuButton!=selectedChapter) {

            menuButton.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
            checkChapNo = menuButton;
        }
        else{
            menuButton.setStyle("-fx-background-color: rgba(119,255,47,0.42);" +
                    "-fx-border-color: rgba(0,0,0,0.99);" +
                    "-fx-border-width: 2px;" +
                    "-fx-padding: 2px;");
            checkChapNo=menuButton;
        }
    }


    public void setVideo(Media media){
        if(mediaView.getMediaPlayer()!=null)
            mediaView.getMediaPlayer().dispose();
        if(media!=null) {
            duration=media.getDuration();
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.getMediaPlayer().setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    stopMedia();
                }
            });
            mediaView.getMediaPlayer().currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    updateMediaValue();
                }
            });
            mediaView.getMediaPlayer().setOnReady(new Runnable() {
                @Override
                public void run() {
                    duration=mediaView.getMediaPlayer().getMedia().getDuration();
                }
            });
            mediaProgressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton()== MouseButton.PRIMARY){
                        //System.out.println((event.getSceneX()-leftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                        mediaProgressBar.setProgress((event.getSceneX()-scrollLeftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                        mediaView.getMediaPlayer().seek(duration.multiply(mediaProgressBar.getProgress()));
                    }
                }
            });
            mediaView.getMediaPlayer().volumeProperty().bindBidirectional(volumeSlider.valueProperty());
            stopMedia();
            updateMediaValue();
        }
    }

    public void playMedia(){
        mediaView.getMediaPlayer().play();
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pauseMedia();
            }
        });
        playButton.setText("Pause");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pauseMedia();
            }
        });
    }
    public void pauseMedia(){
        mediaView.getMediaPlayer().pause();
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playMedia();
            }
        });
        playButton.setText("Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playMedia();
            }
        });
    }
    public void stopMedia(){
        mediaView.getMediaPlayer().seek(duration.multiply(0.0));
        mediaProgressBar.setProgress(0.0);
        pauseMedia();
    }
    public void muteMedia(){
        if(mediaView.getMediaPlayer().isMute()){
            mediaView.getMediaPlayer().setMute(false);
        }
        else{
            mediaView.getMediaPlayer().setMute(true);
        }
    }

    public void showVolumeSlider(){
        if(!volumeButton.isShowing())
            volumeButton.show();
    }
    public void hideVolumeSlider(){
        if(volumeButton.isShowing()){
            volumeButton.hide();
        }
    }

    public void setMediaToFullScreen() {
        Main.window.setFullScreen(true);
        toolBar.setPrefHeight(0.0);
        toolBar.setMinHeight(0.0);
        toolBar.setMaxHeight(0.0);
        scrollLeftMenu.setPrefWidth(0.0);
        scrollLeftMenu.setMinWidth(0.0);
        scrollLeftMenu.setMaxWidth(0.0);
        textbox.setPrefHeight(0.0);
        textbox.setMinHeight(0.0);
        textbox.setMaxHeight(0.0);
        playarea.setPrefWidth(0.0);
        playarea.setMinWidth(0.0);
        playarea.setMaxWidth(0.0);
        toolBar.setVisible(false);
        scrollLeftMenu.setVisible(false);
        textbox.setVisible(false);
        playarea.setVisible(false);
        fullScreenButton.setText("ExitFullScreen");
        fullScreenButton.setOnAction(event -> {
            exitMediaFromFullScreen();
        });
//        Main.window.getScene().getRoot().addEventHandler(KeyEvent,exitFullScreenHandler);

    }

    public void exitMediaFromFullScreen(){
        Main.window.setFullScreen(false);
        toolBar.setPrefHeight(30.0);
        toolBar.setMinHeight(30.0);
        toolBar.setMaxHeight(30.0);
        scrollLeftMenu.setPrefWidth(200.0);
        scrollLeftMenu.setMinWidth(200.0);
        scrollLeftMenu.setMaxWidth(200.0);
        textbox.setPrefHeight(84.0);
        textbox.setMinHeight(84.0);
        textbox.setMaxHeight(84.0);
        playarea.setPrefWidth(620.0);
        playarea.setMinWidth(620.0);
        playarea.setMaxWidth(620.0);
        toolBar.setVisible(true);
        scrollLeftMenu.setVisible(true);
        textbox.setVisible(true);
        playarea.setVisible(true);
        fullScreenButton.setText("FullScreen");
        fullScreenButton.setOnAction(event -> {
            setMediaToFullScreen();
        });
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
    private void updateMediaValue(){
        if(mediaProgressBar!=null && playTime!=null){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration currentTime=mediaView.getMediaPlayer().getCurrentTime();
                    playTime.setText(formatTime(currentTime,duration));
                    mediaProgressBar.setProgress(currentTime.toMillis()/duration.toMillis());
                }
            });
        }
    }

    public void setProperties(){
        {

            playAreaContainer.setLayoutY(0);
            scene.prefWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.prefWidthProperty().bind(scene.widthProperty());
            playAreaContainer.prefWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.prefWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.prefWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.maxWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.maxWidthProperty().bind(scene.widthProperty());
            playAreaContainer.maxWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.maxWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.maxWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.minWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.minWidthProperty().bind(scene.widthProperty());
            playAreaContainer.minWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.minWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.minWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            mediaProgressBar.prefWidthProperty().bind(mediaContainer.widthProperty());
            mediaProgressBar.maxWidthProperty().bind(mediaContainer.widthProperty());
            mediaProgressBar.minWidthProperty().bind(mediaContainer.widthProperty());
            mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
        }
        {
            scene.prefHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.prefHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.prefHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.prefHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.prefHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.prefHeightProperty().bind(playarea.heightProperty());
            mediaContainer.prefHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            scene.maxHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.maxHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.maxHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.maxHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.maxHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.maxHeightProperty().bind(playarea.heightProperty());
            mediaContainer.maxHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            scene.minHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.minHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.minHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.minHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.minHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.minHeightProperty().bind(playarea.heightProperty());
            mediaContainer.minHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());

        }
    }

    public void getNextTopic(ActionEvent event){
        int newTopicIndex= Main.course.chapters.get(Main.selectedChapter).topics.indexOf(Main.currentTopic)+1;
        display(Main.course.chapters.get(Main.selectedChapter).topics.get(newTopicIndex));
    }

    public void getPrevTopic(ActionEvent event){
        int newTopicIndex= Main.course.chapters.get(Main.selectedChapter).topics.indexOf(Main.currentTopic)-1;
        display(Main.course.chapters.get(Main.selectedChapter).topics.get(newTopicIndex));
    }

    public void previouseButtonAction(){
        Main.currentPage-=1;
        currentPage.setText(String.valueOf(Main.currentPage+1));
        currentPage.fireEvent(new ActionEvent());
    }
    public void nextButtonAction() {
        Main.currentPage += 1;
        currentPage.setText(String.valueOf(Main.currentPage + 1));
        currentPage.fireEvent(new ActionEvent());
    }

    public void saveStudentNotes(ActionEvent event){
        try{
            //MenuButton chapno=mapChapter.get(temp).getValue().getKey();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File(selectedChapter.getText()));
            fileWriter.write(notes.getText());
            fileWriter.close();

        }catch(IOException e){
            System.out.println("save student notes   "+ e.toString());
        }

    }

    public void getStudentNotes(){
        try{
            //MenuButton chapno=mapChapter.get(temp).getValue().getKey();
            BufferedReader br=new BufferedReader(new FileReader(selectedChapter.getText()));
            notes.deleteText(0,notes.getText().length());
            String line=br.readLine();
            while(line!=null){
                notes.appendText(line);
                notes.appendText(System.lineSeparator());
                line=br.readLine();
            }
        }catch(IOException e){
            System.out.println("Getting student notes "+ e.toString());
        }
    }

    public void showManual(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../manual/manual.fxml"));
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.NONE);
            newWindow.setTitle("Manual");

            newWindow.setScene(new Scene(root));
            newWindow.show();
        }catch (IOException e){
            System.out.println(e.toString());
            e.printStackTrace();
        }



    }

    public void renameCourse(){
        Main.course.courseName=PopUp.getName(Main.course.courseName);
        Main.window.setTitle(Main.course.courseName);

    }

}
