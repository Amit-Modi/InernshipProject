package sample;

import course.Course;
import course.Topic;
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
    public static Topic currentTopic;
    public static Integer currentPage;
    public static String courseFileLocation;
    public static Boolean changesMade;

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
