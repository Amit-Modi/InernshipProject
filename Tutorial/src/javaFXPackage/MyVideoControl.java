package javaFXPackage;

import javafx.scene.media.MediaView;

/**
 * Created by ghost on 31/5/17.
 */
public class MyVideoControl {
    public static void startVideo(MediaView m){
        m.getMediaPlayer().play();
        m.setOnMouseClicked(e->{
            stopVideo((MediaView) e.getSource());
        });
    }

    public static void stopVideo(MediaView m){
        m.getMediaPlayer().pause();
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }

    public static void addVideoControl(MediaView m){
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }
}
