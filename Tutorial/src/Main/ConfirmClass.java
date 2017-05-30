package Main;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by ghost on 27/5/17.
 */
public class ConfirmClass {

    static boolean result;

    public static boolean display(String title,String message){
        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label label=new Label(message);

        Button yes=new Button("Yes");
        Button no=new Button("No");

        yes.setOnAction(e->{
            System.out.println("confermed");
            result=true;
            window.close();
        });
        no.setOnAction(e->{
            System.out.println("action aborted");
            result= false;
            window.close();
        });

        VBox vbox=new VBox(20);
        vbox.getChildren().addAll(label);
        HBox hbox=new HBox(20);
        hbox.getChildren().addAll(yes,no);

        BorderPane layout=new BorderPane();
        layout.setCenter(vbox);
        layout.setBottom(hbox);

        Scene scene=new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
        return result;
    }
}
