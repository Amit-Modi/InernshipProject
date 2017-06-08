package sample;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.util.Pair;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private LinkedHashMap<MenuItem,Pair<Integer,Pair<String,String>>> mapChapter;
    private Document document;
    private Duration duration;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture;
    private MenuItem temp;
    @FXML
    public MenuItem c1_0;
    @FXML
    public MenuItem c1_1;
    @FXML
    ScrollPane playarea;
    @FXML
    ScrollPane textbox;
    @FXML
    MediaView mediaView;
    @FXML
    HBox mediaContainer;
    @FXML
    HBox lectureContainer;
    @FXML
    AnchorPane scene;
    @FXML
    BorderPane totalContainer;
    @FXML
    VBox playAreaContainer;
    @FXML
    VBox playerContainer;
    @FXML
    VBox leftMenu;
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
    Label totalPages;
    @FXML
    TextField currentPage;

    @FXML
    Button nextTopic;

    @FXML
    Button previousButton;

    @FXML
    Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mapChapter = new LinkedHashMap<>();
            mapChapter.put(c1_0, new Pair<>(0,new Pair<>("CHAP1.pdf","/media/ghost/Live Free/Entertainment/videoplayback.mp4")));
            mapChapter.put(c1_1, new Pair<>(0,new Pair<>("CHAP1.pdf","/media/ghost/Live Free/Entertainment/videoplayback.mp4")));

        }catch (Exception e){
            System.out.println("Chapter not set due to :  "+e.toString());
        }
        try {
            textbox.setContent(new TextArea());
        }catch (Exception e){
            System.out.println("Note Box not set due to : "+e.toString());
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
                            showpdf(value);
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
    }

    public Controller(){
        super();
        executor=new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    public void onExit(MouseEvent e) throws IOException{

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

        String pdf=null;
        String video=null;
        try {
            pdf = mapChapter.get(event.getSource()).getValue().getKey();
            video = mapChapter.get(event.getSource()).getValue().getValue();

        }
        catch (Exception e){
            System.out.println("onMouseclick "+e.toString());
        }
        document=getDocument(pdf);
        currentPage.setText("1");
        showpdf(0);
        setVideo(video);
        temp= (MenuItem)event.getSource();
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
                        mediaProgressBar.setProgress((event.getSceneX()-leftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                        mediaView.getMediaPlayer().seek(duration.multiply(mediaProgressBar.getProgress()));
                    }
                }
            });
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
        /*if(scheduledFuture!=null && scheduledFuture.isCancelled() && scheduledFuture.isDone()){
            scheduledFuture.cancel(false);
        }*/
        volumeButton.show();
    }
    public void hideVolumeSlider(){/*
        try {
            scheduledFuture = executor.schedule(() -> {
                hideVolumeSliderUtil();
            }, 100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            System.out.println("exception while scheduling future hide"+e.toString());
        }*/
    }
    private void hideVolumeSliderUtil(){
        try{
            volumeButton.hide();
        }
        catch (Exception e){
            System.out.println("error in hiding volumeSlider:"+e.toString());
        }
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

        textbox.setPrefHeight(84.0);
        textbox.setMinHeight(84.0);
        textbox.setMaxHeight(84.0);
        {

            playAreaContainer.setLayoutY(0);
            scene.prefWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.prefWidthProperty().bind(scene.widthProperty());
            playAreaContainer.prefWidthProperty().bind(totalContainer.widthProperty().subtract(leftMenu.widthProperty()));
            lectureContainer.prefWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.prefWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.maxWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.maxWidthProperty().bind(scene.widthProperty());
            playAreaContainer.maxWidthProperty().bind(totalContainer.widthProperty().subtract(leftMenu.widthProperty()));
            lectureContainer.maxWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.maxWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.minWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.minWidthProperty().bind(scene.widthProperty());
            playAreaContainer.minWidthProperty().bind(totalContainer.widthProperty().subtract(leftMenu.widthProperty()));
            lectureContainer.minWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.minWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
        }
        {
            scene.prefHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.prefHeightProperty().bind(scene.heightProperty());
            leftMenu.prefHeightProperty().bind(totalContainer.heightProperty());
            playAreaContainer.prefHeightProperty().bind(totalContainer.heightProperty().subtract(textbox.heightProperty()));
            playarea.prefHeightProperty().bind(playAreaContainer.heightProperty());
            playerContainer.prefHeightProperty().bind(playAreaContainer.heightProperty());
            mediaContainer.prefHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty()));

            scene.maxHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.maxHeightProperty().bind(scene.heightProperty());
            leftMenu.maxHeightProperty().bind(totalContainer.heightProperty());
            playAreaContainer.maxHeightProperty().bind(totalContainer.heightProperty().subtract(textbox.heightProperty()));
            playarea.maxHeightProperty().bind(playAreaContainer.heightProperty());
            playerContainer.maxHeightProperty().bind(playAreaContainer.heightProperty());
            mediaContainer.maxHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty()));

            scene.minHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.minHeightProperty().bind(scene.heightProperty());
            leftMenu.minHeightProperty().bind(totalContainer.heightProperty());
            playAreaContainer.minHeightProperty().bind(totalContainer.heightProperty().subtract(textbox.heightProperty()));
            playarea.minHeightProperty().bind(playAreaContainer.heightProperty());
            playerContainer.minHeightProperty().bind(playAreaContainer.heightProperty());
            mediaContainer.minHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty()));

            mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());

        }
        textbox.setPrefHeight(84.0);
        textbox.setMinHeight(84.0);
        textbox.setMaxHeight(84.0);
    }

    public void getNextTopic(ActionEvent event){
        String pdf=null;
        String video=null;
        Iterator<MenuItem> it =mapChapter.keySet().iterator();

        MenuItem tempkey=null;

        while(it.hasNext()){
            if((temp==it.next())){
                tempkey= it.next();
            }
        }


        pdf = mapChapter.get(tempkey).getValue().getKey();
        video = mapChapter.get(tempkey).getValue().getValue();

        document=getDocument(pdf);
        currentPage.setText("1");
        showpdf(0);
        setVideo(video);

    }

    public void previouseButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())-1));
    }
    public void nextButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())+1));
    }
}
