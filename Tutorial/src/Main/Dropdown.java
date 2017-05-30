package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by ghost on 29/5/17.
 */
public class Dropdown extends Application{

    Stage window;
    Scene scene;

    public static void main(String [] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("DropdownList");

        ChoiceBox<String> choiceBox=new ChoiceBox<>();
//getItems() returns the ObservableList object which you can add items to
        choiceBox.getItems().add("apple");
        choiceBox.getItems().addAll("Banana","Grapes");

//set default value
        choiceBox.setValue("apple");

//set button to receive choiceBox value
        Button button =new Button("Order");
        button.setOnAction(e->{
            System.out.println("Order is :"+ getChoice(choiceBox));
        });

//choiceBox without Button
        ChoiceBox<String> choiceBox1=new ChoiceBox<>();
        choiceBox1.getItems().addAll("BLUE","RED","GREEN","WHITE","BLACK");
        choiceBox1.setValue("WHITE");
        choiceBox1.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> {
            System.out.println(newValue);
            Color backgroundColor;
            switch (choiceBox1.getValue()){
                case "BLUE":backgroundColor=Color.BLUE;break;
                case "RED":backgroundColor=Color.RED;break;
                case "GREEN":backgroundColor=Color.GREEN;break;
                case "BLACK":backgroundColor=Color.BLACK;break;
                case "WHITE":backgroundColor=Color.WHITE;break;
                default:backgroundColor=Color.GRAY;
            }
            scene.setFill(Color.BLACK);
        });

        VBox layout =new VBox(10);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(new Text("ChoiceBox With Button"),choiceBox,button,new Text("ChoiceBox Without Button"),choiceBox1);

        scene=new Scene(layout,300,250);
        scene.setFill(Color.BLACK);
        window.setScene(scene);
        window.show();
    }
    private String getChoice(ChoiceBox<String> choiceBox){
        return choiceBox.getValue();
    }
}
