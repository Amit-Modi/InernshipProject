package Main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by ghost on 29/5/17.
 */
public class ListViewClass extends Application {

    Stage window;
    Scene scene;
    Button button;
    ListView<String> listView;

    public static void main(String[]args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("ListView");

        button=new Button("Submit");
        button.setOnAction(e->{
            String message="";
            ObservableList<String> mans;
            mans=listView.getSelectionModel().getSelectedItems();
            for(String m:mans){
                message+=m+"\n";
            }
            System.out.println(message);
        });

        listView=new ListView<>();
        listView.getItems().addAll("ironman","Spiderman","batman","superman","boogeyman");
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VBox layout=new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(listView,button);

        scene=new Scene(layout,300,200);

        window.setScene(scene);
        window.show();
    }
}
