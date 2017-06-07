package sample;

import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
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
    private HashMap<MenuItem,Pair<Integer,Pair<Integer,String>>> mapChapter;
    @FXML
    public MenuItem c1_0;
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



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            mapChapter = new HashMap<>();
            mapChapter.put(c1_0, new Pair<>(0,new Pair<>(0,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

        }catch (Exception e){
            System.out.println("initialize  "+e.toString());
        }
        textbox.setContent(new TextArea());

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
            playarea.contentProperty().unbind();
            playarea.contentProperty().bind(image);
            org.icepdf.core.pobjects.Document document = new Document();
            try {
                File file = new File("CHAP1.pdf");
                document.setFile(file.getAbsolutePath());
            } catch (PDFException | PDFSecurityException | IOException ex) {
                System.out.println(ex.toString());

            }


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

    public void onMenuItemClick(ActionEvent event){

        Integer pageno = 0;
        String video=null;
        try {
            pageno = mapChapter.get(event.getSource()).getKey();
            video = mapChapter.get(event.getSource()).getValue().getValue();

        }
        catch (Exception e){
            System.out.println("onMouseclick "+e.toString());
        }
        showpdf(pageno);
        setVideo(video);
    }

    public void setVideo(String path){
        File file=new File(path);
        if(file!=null) {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            try{
                mediaView.getMediaPlayer().dispose();
            }catch (Exception e){    }
            mediaView.setMediaPlayer(mediaPlayer);
        }
    }

    public void playMedia(){
        mediaView.getMediaPlayer().play();
    }
    public void pauseMedia(){
        mediaView.getMediaPlayer().pause();
    }
    public void stopMedia(){
        mediaView.getMediaPlayer().stop();
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

}
