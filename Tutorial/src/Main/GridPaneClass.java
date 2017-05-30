package Main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by ghost on 28/5/17.
 */
public class GridPaneClass extends Application{

    Stage window;

    public static void main(String [] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("Grid Example");

        GridPane grid=new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(16);
        grid.setHgap(20);
//login grid components
        Label namelabel= new Label("UserName:");
        GridPane.setConstraints(namelabel,0,0);

        TextField nameInput=new TextField();
        nameInput.setPromptText("username");
        GridPane.setConstraints(nameInput,1,0);

        Label passwordLabel= new Label("Password:");
        GridPane.setConstraints(passwordLabel,0,1);

        TextField passwordInput=new TextField();
        passwordInput.setPromptText("password");
        GridPane.setConstraints(passwordInput,1,1);
//checkbox
        CheckBox rembemberBox=new CheckBox("Remember Me");
        rembemberBox.setSelected(true);
        GridPane.setConstraints(rembemberBox,1,2);

        Button login=new Button("LogIn");
        login.setOnAction(e->{
            checkLoginDetails(nameInput,passwordInput);
        });
        GridPane.setConstraints(login,1,3);
        Button reset=new Button("Reset");
        reset.setOnAction(e->{
            nameInput.clear();
            passwordInput.clear();
        });
        GridPane.setConstraints(reset,2,3);

//enter all components into grid
        grid.getChildren().addAll(namelabel,nameInput,passwordInput,passwordLabel,rembemberBox,login,reset);

//add pane to scene
        Scene scene=new Scene(grid,400,200);

//add scene to stage
        window.setScene(scene);
        window.show();
    }

    private void checkLoginDetails(TextField usernameInput,TextField passwordInput){
        String username=usernameInput.getText();
        String password=passwordInput.getText();
        System.out.println(username.matches("[_a-zA-Z][_a-zA-Z0-9]*"));
        if(username.matches("[_a-zA-Z][_a-zA-Z0-9]*")==false){
            System.out.println("incorrect username");
        }
        else if(password.matches("[_a-zA-Z0-9!@#$~]*")==false){
            System.out.println("incorrect password");
        }
        else{
            System.out.println("correct username and password");
            window.close();
        }
    }
}
