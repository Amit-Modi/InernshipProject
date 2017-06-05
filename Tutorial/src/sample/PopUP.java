package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by ghost on 1/6/17.
 */
public class PopUP {

    private static String string;
    private static Boolean aBoolean;

    public static String getTextInput(String title,String message){
        Stage window =new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label label =new Label();
        label.setText(message);

        TextField textField=new TextField();

        Button ok=new Button("OK");
        Button cancel=new Button("Cancel");

        textField.setOnAction(e->{
            if(textField.getText().matches("[a-zA-Z][a-zA-Z0-1_@()']*")){
                string=textField.getText();
                window.close();
            }
            else{
                PopUP.display("Warning!","Not a valid course name.");
                textField.setStyle("-fx-border-color: red");
                textField.clear();
            }
        });

        ok.setOnAction(e->{
            if(textField.getText().matches("[a-zA-Z][a-zA-Z0-1_@()']*")){
                string=textField.getText();
                window.close();
            }
            else{
                PopUP.display("Warning!","Not a valid course name.");
                textField.setStyle("-fx-border-color: red");
                textField.clear();
            }
        });

        cancel.setOnAction(e->{
            string= "";
            window.close();
        });

        GridPane gridPane=new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(16);
        gridPane.setHgap(20);
        GridPane.setConstraints(label,0,0);
        GridPane.setConstraints(textField,1,0);
        HBox buttonContainer=new HBox(20);

        buttonContainer.getChildren().addAll(cancel,ok);

        GridPane.setConstraints(buttonContainer,1,1);

        gridPane.getChildren().addAll(label,textField,buttonContainer);
        gridPane.setAlignment(Pos.CENTER);
        Scene scene =new Scene(gridPane, 400,150);
        window.setScene(scene);
        window.centerOnScreen();
        window.showAndWait();
        return string;
    }

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

    public static void showException(Exception e){
        System.out.println(e.toString());
        PopUP.display("Error","Following error occurred while deleting Selecting Item:\n"+e.getMessage());
    }

    public static Boolean confermBox(String title,String message){
        Stage window=new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(200);

        Label label=new Label(message);

        Button yes=new Button("Yes");
        Button no=new Button("No");

        yes.setOnAction(e->{
            System.out.println("confermed");
            aBoolean=true;
            window.close();
        });
        no.setOnAction(e->{
            System.out.println("action aborted");
            aBoolean= false;
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
        return aBoolean;
    }

}
