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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import pageEditing.EditPages;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
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
    private static MenuButton checkChapNo;

    public ImageView buttonImage1,buttonImage2;
    public Image image1,image2;

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
            image1 = new Image("file:///home/arnab/Desktop/download.jpg");
            image2 = new Image("file:///home/arnab/IdeaProjects/APP1.0/download%20(1).jpg");

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
            currentPage.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue.matches("")){

                    }
                    else if(newValue.matches("[1-9][0-9]*")){
                        int value=Integer.parseInt(newValue)-1;
                        if(value<Integer.parseInt(totalPages.getText().substring(1))) {
                            display(value);
                            if(value==0){
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
                        else{
                            currentPage.setText(oldValue);
                        }
                    }
                    else{
                        currentPage.setText(oldValue);
                    }
                }
            });
        }catch (Exception e){
            System.out.println("current page property"+e.toString());
        }
//        checkChapNo=mapChapter.get(c15_2).getValue().getKey();
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
            menuButton.setText(PopUp.getName(menuButton.getText()));
        }catch (Exception e){
            System.out.println("Exception in addChapter");
            e.printStackTrace();
        }
    }

    public void showCourseChapters(){
        System.out.println(Main.course.chapters);
    }

    private MenuButton getNewChapterMenuButton(Chapter chapter){
        MenuButton menuButton = new MenuButton(chapter.chapterName);
        ContextMenu contextMenu=new ContextMenu();

        MenuItem addTopic=new MenuItem("Add Topic");
        addTopic.setOnAction(e->{
            String topicName="Topic"+String.valueOf(chapter.topics.size()+1);

            Topic newTopic =new Topic();
            newTopic.topicName=topicName;
            newTopic.pages=new ArrayList<>();
            chapter.topics.add(newTopic);

            MenuItem menuItem1=new MenuItem();
            Label label=new Label(topicName);

            label.textProperty().addListener((observable, oldValue, newValue) -> {
                newTopic.topicName=newValue;
            });

            ContextMenu contextMenu1=new ContextMenu();

            MenuItem editTopic= new MenuItem("editTopic");
            editTopic.setOnAction(ee->{
                try {
                    newTopic.pages = editPages(newTopic.topicName,newTopic.pages);
                }catch (Exception exception){
                    System.out.println(exception);
                }
            });

            MenuItem deleteTopic= new MenuItem("deleteTopic");
            deleteTopic.setOnAction(ed->{
                if(menuButton.getItems().remove(menuItem1));
                    System.out.println("topic removed from list");
                if(chapter.topics.remove(newTopic))
                    System.out.println("taopic remobed from course");

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
                        event.consume();
                    }
                    else{
                        display(newTopic.pages);
                    }
                }
            });
            menuItem1.setOnAction(actionEvent-> {
                display(newTopic.pages);
            });
            menuItem1.setGraphic(label);

            menuButton.getItems().add(menuItem1);
            label.setText(PopUp.getName(label.getText()));

        });

        MenuItem deleteChapter=new MenuItem("Delete");
        deleteChapter.setOnAction(e->{
            Main.course.chapters.remove(chapter);
            leftMenu.getItems().remove(menuButton);
        });

        MenuItem renameChapter=new MenuItem("Rename");
        renameChapter.setOnAction(e->{
            menuButton.setText(PopUp.getName(menuButton.getText()));
        });

        contextMenu.getItems().addAll(addTopic,deleteChapter,renameChapter);
        menuButton.setContextMenu(contextMenu);
        menuButton.setOnContextMenuRequested(e->{
            menuButton.getContextMenu().show(Main.window);
        });

        return menuButton;
    }

    private ArrayList<AnchorPane> editPages(String topicName,ArrayList<AnchorPane> pages) throws Exception{
        try {
            EditPages.pages = pages;
            Parent root = FXMLLoader.load(getClass().getResource("../pageEditing/editPages.fxml"));
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle(topicName);
            newWindow.setScene(new Scene(root));
            newWindow.showAndWait();
            return EditPages.pages;
        }catch (Exception e){
            e.printStackTrace();
        }
        return pages;
    }

    private void display(ArrayList<AnchorPane> pages) {
        try {
            Main.currentTopicPages = pages;
            totalPages.setText("/" + String.valueOf(pages.size()));
            currentPage.setText("1");
        }catch (Exception e){
            if(totalPages.getText()=="/0")
                playarea.setContent(new Text("no Slides added"));
            e.printStackTrace();
        }
    }
    private void display(Integer value){
        playarea.setContent(Main.currentTopicPages.get(value));
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
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Couser File","course"));
        File file=fileChooser.showOpenDialog(Main.window);
        if(file!=null){
            Main.courseFileLocation=file.getAbsolutePath();
            FileInputStream fis=new FileInputStream(Main.courseFileLocation);
            ObjectInputStream ois=new ObjectInputStream(fis);
            Main.course=(Course)ois.readObject();
            refreshCourse();
        }
    }

    public void saveAs() throws Exception{
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Course File","course"));
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
        if(Main.courseFileLocation!=null) {
            FileOutputStream fos = new FileOutputStream(Main.courseFileLocation);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Main.course);
            oos.close();
        }
    }

    public void refreshCourse(){
        leftMenu.getItems().clear();
        Main.window.setTitle(Main.course.courseName);
        for(Chapter eachChapter : Main.course.chapters){
            MenuButton menuButton=getNewChapterMenuButton(eachChapter);
            menuButton.textProperty().addListener((observable, oldValue, newValue) -> {
                eachChapter.chapterName=newValue;
            });

            for(Topic eachTopic : eachChapter.topics){
                MenuItem menuItem=new MenuItem();
                Label label=new Label(eachTopic.topicName);
                label.textProperty().addListener((observable, oldValue, newValue) -> {
                    eachTopic.topicName=newValue;
                });

                ContextMenu contextMenu1=new ContextMenu();

                MenuItem editTopic= new MenuItem("editTopic");
                editTopic.setOnAction(ee->{
                    try {
                        eachTopic.pages = editPages(eachTopic.topicName,eachTopic.pages);
                    }catch (Exception exception){
                        System.out.println(exception);
                    }
                });

                MenuItem deleteTopic= new MenuItem("deleteTopic");
                deleteTopic.setOnAction(ed->{
                    if(menuButton.getItems().remove(menuItem));
                        System.out.println("topic removed from list");
                    if(eachChapter.topics.remove(eachTopic))
                        System.out.println("taopic remobed from course");
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
                            event.consume();
                        }
                        else{
                            display(eachTopic.pages);
                        }
                    }
                });
                menuItem.setGraphic(label);

                menuButton.getItems().add(menuItem);
            }

            leftMenu.getItems().add(menuButton);
        }
        try{
            playarea.setContent(Main.course.chapters.get(0).topics.get(0).pages.get(0));
        }catch (Exception e){}
    }

    public void onExit(MouseEvent e) throws IOException{
        if(checkChapNo!=(MenuButton)e.getSource())

        ((MenuButton)e.getSource()).setStyle("-fx-background-color: white");

    }

    public void onEntered(MouseEvent e)throws IOException{

        try{
            b1.hide();
        }
        catch (Exception e1){}
        b1=(MenuButton)e.getSource();
        b1.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
        b1.show();

    }

    public void showpdf(Integer page) {

        try {

            float scale = 1.0f;
            float rotation = 0f;
            // Paint each pages content to an image
            BufferedImage image1 = (BufferedImage) document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);

            WritableImage fxImage = SwingFXUtils.toFXImage(image1, null);

            if (image.get() != null) {
                image.get().setImage(fxImage);
            } else {
                image.set(new ImageView(fxImage));
            }

            //Clean up
            image1.flush();
        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
        }
    }

    private Document getDocument(String string){
        playarea.contentProperty().unbind();
        playarea.contentProperty().bind(image);
        org.icepdf.core.pobjects.Document document = new Document();
        try {
            File file = new File(string);
            document.setFile(file.getAbsolutePath());
        } catch (PDFException | PDFSecurityException | IOException ex) {
            System.out.println(ex.toString());
        }
        totalPages.setText("/"+String.valueOf(document.getNumberOfPages()));
        return document;
    }

    public void onMenuItemClick(ActionEvent event){
        checkChapNo.setStyle("-fx-background-color: floralwhite");

        MenuButton chapno=null;


        String video=null;
        int pageno=0;
        try {
            topicLabel.setText(((MenuItem)event.getSource()).getText());
            pageno=mapChapter.get(event.getSource()).getKey();
            chapno= mapChapter.get(event.getSource()).getValue().getKey();
            chapno.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
            video = mapChapter.get(event.getSource()).getValue().getValue();
            examFilePath="Question.qtn";
        }
        catch (Exception e){
            System.out.println("onMouseclick "+e.toString());
        }

        String pdf=chapterno.get(chapno).getKey();
        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno+1));
        if(chapno==checkChapNo){
            showpdf(pageno);
        }else {
            showpdf(pageno);
            setVideo(video);
            checkChapNo=chapno;
        }
        temp= (MenuItem)event.getSource();

        getStudentNotes();

    }

    public void setVideo(String path){
        try{
            mediaView.getMediaPlayer().dispose();
        }catch (Exception e){    }
        File file=new File(path);
        if(file!=null) {
            Media media = new Media(file.toURI().toString());
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
        String pdf=null;
        String video=null;
        List list= new ArrayList(mapChapter.keySet());
        ListIterator<MenuItem> it =list.listIterator();

        MenuItem tempkey=null;

        for(int i=0;i<list.size();){
            if(temp==list.get(i)){
                tempkey=(MenuItem) list.get(i+1);
                temp=tempkey;
                break;
            }
            else{
                i++;
            }
        }



        int pageno=mapChapter.get(tempkey).getKey();
        MenuButton chapno= mapChapter.get(tempkey).getValue().getKey();
        if(checkChapNo!=chapno){
            checkChapNo.setStyle("-fx-background-color: white");
            checkChapNo=chapno;
        }
        chapno.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
        video = mapChapter.get(tempkey).getValue().getValue();
        pdf=chapterno.get(chapno).getKey();

        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno +1));
        showpdf(pageno);
        //setVideo(video);

    }
    public void getPrevTopic(ActionEvent event){
        String pdf=null;
        String video=null;
        List list= new ArrayList(mapChapter.keySet());
        ListIterator<MenuItem> it =list.listIterator();

        MenuItem tempkey=null;

        for(int i=0;i<list.size();i++){
            if(temp==list.get(i)){
                tempkey=(MenuItem) list.get(i-1);
                temp=tempkey;
            }
        }


        int pageno=mapChapter.get(tempkey).getKey();
        MenuButton chapno= mapChapter.get(tempkey).getValue().getKey();
        if(checkChapNo!=chapno){
            checkChapNo.setStyle("-fx-background-color: white");
            checkChapNo=chapno;
        }
        chapno.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
        video = mapChapter.get(tempkey).getValue().getValue();
        pdf=chapterno.get(chapno).getKey();

        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno +1));
        showpdf(pageno);
        //setVideo(video);


    }

    public void previouseButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())-1));
    }
    public void nextButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())+1));
    }


    public void startExam() throws Exception{
        FileInputStream fis=new FileInputStream(examFilePath);
        ObjectInputStream ois=new ObjectInputStream(fis);
        ExamSceneController.questions=(List<MCQ>) ois.readObject();
        ExamSceneController.sourceScene=Main.window.getScene();
        Scene examScene=new Scene(FXMLLoader.load(getClass().getResource("examScene.fxml")),Main.window.getScene().getWidth(),Main.window.getScene()    .getHeight());
        Main.window.setScene(examScene);

    }


    public void saveStudentNotes(ActionEvent event){
        try{
            MenuButton chapno=mapChapter.get(temp).getValue().getKey();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File(chapterno.get(chapno).getValue().getKey()));
            fileWriter.write(notes.getText());
            fileWriter.close();

        }catch(IOException e){
            System.out.println("save student notes   "+ e.toString());
        }

    }

    public void getStudentNotes(){
        try{
            MenuButton chapno=mapChapter.get(temp).getValue().getKey();
            BufferedReader br=new BufferedReader(new FileReader(chapterno.get(chapno).getValue().getKey()));
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

}