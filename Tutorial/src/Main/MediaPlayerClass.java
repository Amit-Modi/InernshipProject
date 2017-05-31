package Main;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

/**
 * Created by ghost on 31/5/17.
 */
public class MediaPlayerClass extends Application{

    private static String MEDIA_URL;


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Embedded Media Player");
        Group root = new Group();
        Scene scene = new Scene(root, 540, 241);

        // create media player
        Media media = new Media (MEDIA_URL);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        MediaControl mediaControl = new MediaControl(mediaPlayer);

        Button button =new Button("OpenFile...");
        button.setOnAction(e->{
            FileChooser f=new FileChooser();
            File file=f.showOpenDialog(primaryStage);
            if(file!=null){
                MediaView m=((MediaView)primaryStage.getScene().lookup("player"));

            }
        });

        root.getChildren().addAll(mediaControl);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
