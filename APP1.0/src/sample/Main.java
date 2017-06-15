package sample;

import course.Course;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public static Stage window;
    public static Course course;
    public static Integer selectedChapter;
    public static String courseFileLocation;
    public static Boolean changesMade;
    public static ArrayList<AnchorPane> currentTopicPages;
    public static double orgSceneX;
    public static double orgSceneY;
    public static double orgTranslateX;
    public static double orgTranslateY;
    public static Pane selectedPane;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("startingStage.fxml"));

        Scene s=new Scene(root);
        window.setResizable(false);
        window.setScene(s);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
