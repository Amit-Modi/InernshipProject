package Main;

 import javafx.application.Application;
 import javafx.event.EventHandler;
 import javafx.scene.Node;
 import javafx.scene.control.Button;
 import javafx.application.Platform;
 import javafx.embed.swing.SwingFXUtils;
 import javafx.fxml.FXML;
 import javafx.scene.Scene;
 import javafx.scene.SnapshotParameters;
 import javafx.scene.image.WritableImage;
 import javafx.scene.layout.HBox;
 import javafx.scene.layout.StackPane;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javax.imageio.ImageIO;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.io.File;
 import java.io.IOException;
 import java.util.Timer;
 import java.util.TimerTask;
public class tutorial1 extends Application {

    Scene s1,s2;
    Stage window;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//Setting Stage
        primaryStage.setTitle("Title");
        window=primaryStage;
//Modify close
        window.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });

//Setting scene1
        Button b1=new Button();
        b1.setText("Change to scene2");
        b1.setOnAction(e->{
                System.out.println("hell is here");
                primaryStage.setScene(s2);
        });
        StackPane layout=new StackPane();
        layout.getChildren().add(b1);

        s1=new Scene(layout,300,300);

//Setting scene2
        Button b2=new Button();
        b2.setText("Change to Scene1");
        b2.setOnAction(e->{
            System.out.println("hell is here too");
            primaryStage.setScene(s1);
        });
//Alert Box
        Button alertBox=new Button("Alert Me");
        alertBox.setOnAction(e->AlertClass.display("Alert","You have been alerted "));

//Confirm Box
        Button confirmBox=new Button("Close");
        confirmBox.setOnAction(e->{
            closeProgram();
        });
// Adding components to scene2
        VBox layout2=new VBox(20);
        layout2.getChildren().addAll(b2,alertBox,confirmBox);

        s2=new Scene(layout2,300,600);

//displaying stage
        window.setScene(s2);
        window.show();
    }

    public void closeProgram(){
        if(ConfirmClass.display("Alert","Are you sure you want to close this window?"))
            window.close();
    }
}
