package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by ghost on 29/5/17.
 */
public class ComboBoxClass extends Application {

    Stage window;
    Scene scene;
    ComboBox<String> comboBox;

    public static void main(String[]ss){
        launch(ss);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("CombowBox");

        Button button=new Button("Select");
        button.setOnAction(e->{
            System.out.println("selected"+comboBox.getValue());
        });

        comboBox=new ComboBox<>();
        comboBox.getItems().addAll("1","2","3");
        comboBox.setPromptText("what is your number");

        comboBox.setOnAction(e->System.out.println(comboBox.getValue()));
        comboBox.setEditable(true);

        VBox layout =new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(comboBox,button);

        scene=new Scene(layout,300,200);
        window.setScene(scene);
        window.show();

    }
}
