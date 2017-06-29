package sample;


import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

/**
 * Created by ghost on 13/6/17.
 */
public class PopUp {

    private static String string;

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
        layout.setPrefSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
        layout.setPadding(new Insets(10));

        Scene scene =new Scene(layout, Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
        window.setScene(scene);
        window.showAndWait();
    }

    public static String getName(final String defaultName){
        string=defaultName;
        Stage popUpStage =new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setMinWidth(200);
        TextField textField=new TextField(defaultName);

        textField.setOnAction(e->{
            if(textField.getText().equals("")){
                popUpStage.close();
            }
            else if (textField.getText().matches("[a-zA-Z][-a-zA-Z0-9_()!'   ]*")) {
                string = textField.getText();
                popUpStage.close();
            } else {
                PopUp.display("Warning!", "Not a valid name.");
                textField.setStyle("-fx-border-color: red");
                textField.clear();
            }
        });

        Scene scene =new Scene(textField);
        popUpStage.setScene(scene);
        popUpStage.centerOnScreen();
        textField.requestFocus();
        textField.selectAll();
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                popUpStage.close();
            }
        });
        popUpStage.showAndWait();
        return string;
    }

    public static String getUrl(final String defaultName){
        string=defaultName;
        Stage popUpStage =new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setMinWidth(200);
        TextField textField=new TextField(defaultName);

        textField.setOnAction(e->{
            if(textField.getText().equals("")){
                popUpStage.close();
            }
            else if (textField.getText().matches(".*")) {
                string = textField.getText();
                popUpStage.close();
            } else {
                PopUp.display("Warning!", "Not a valid url.");
                textField.setStyle("-fx-border-color: red");
                textField.clear();
            }
        });

        Scene scene =new Scene(textField);
        popUpStage.setScene(scene);
        popUpStage.centerOnScreen();
        textField.requestFocus();
        textField.selectAll();
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue==true && newValue==false){
                textField.fireEvent(new ActionEvent());
            }
        });
        popUpStage.showAndWait();
        return string;
    }

}
