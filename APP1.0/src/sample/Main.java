package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("My Authoring System");
        Scene s=new Scene(root,1200,600);

        primaryStage.setScene(s);
        window=primaryStage;
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
