package javaFXPackage;

import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by ghost on 31/5/17.
 */
public class Sample {
    @FXML public void addVideo(){
        FileChooser chooser = new FileChooser();
        File file=chooser.showOpenDialog(Main.window);
        if(file!=null){
            Media videoSource = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(videoSource);
            MediaView mediaView=new MediaView();
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            mediaView.setOnMouseClicked(e->{
                startVideo((MediaView)e.getSource());
            });
            StackPane stackPane=new StackPane();
            stackPane.getChildren().addAll(mediaView);
            stackPane.setAlignment(Pos.CENTER);
//            stackPane.setPrefHeight(((Pane)Main.window.getScene().lookup("#scene")).getPrefHeight()/2);
//            stackPane.setPrefWidth(((Pane)Main.window.getScene().lookup("#scene")).getPrefWidth()/2);
            stackPane.setPrefSize(400,300);
            BorderPane b=new BorderPane();
            mediaView.fitHeightProperty().bind(stackPane.heightProperty());
            mediaView.fitWidthProperty().bind(stackPane.heightProperty());
            ((Pane)Main.window.getScene().lookup("#scene")).getChildren().addAll(stackPane);
        }
    }

    private void startVideo(MediaView m){
        m.getMediaPlayer().play();
        m.setOnMouseClicked(e->{
            stopVideo((MediaView) e.getSource());
        });
    }

    private void stopVideo(MediaView m){
        m.getMediaPlayer().pause();
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }
}
