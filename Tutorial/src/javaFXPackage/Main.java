package javaFXPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static double orgSceneX,orgSceneY;
    public static double orgTranslateX,orgTranslateY;
    public static Parent root;
    public static Stage window;

    public static StackPane selectedPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window=primaryStage;
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800  , 500));
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}