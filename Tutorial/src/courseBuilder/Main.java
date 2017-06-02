package courseBuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

/**
 * Created by ghost on 30/5/17.
 */
public class Main extends Application{

    public static SubScene selectedScene;
    public static Stage window;

    public static void main(String[]args){


        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainStage.fxml"));
        primaryStage.setTitle("Course Builder");
        primaryStage.setScene(new Scene(root, 1200  , 900));
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
