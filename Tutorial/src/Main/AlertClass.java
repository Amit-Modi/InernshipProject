package Main;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by ghost on 27/5/17.
 */
public class AlertClass {
    public static void display(String title, String messgaes){
        Stage window =new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label label =new Label();
        label.setText(messgaes);

        Button b1=new Button("OK");
        b1.setOnAction(e->{
            window.close();
        });

        VBox layout=new VBox(20);
        layout.getChildren().addAll(label,b1);
        layout.setAlignment(Pos.CENTER);

        Scene scene =new Scene(layout, 200,150);
        window.setScene(scene);
        window.showAndWait();
    }
}
