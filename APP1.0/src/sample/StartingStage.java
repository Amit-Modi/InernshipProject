package sample;

import course.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ghost on 13/6/17.
 */


public class StartingStage implements Initializable{

    @FXML
    Button startButton;
    @FXML
    AnchorPane welcomeScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        welcomeScene.requestFocus();
        startButton.requestFocus();
    }

    public void startNewCourse() throws Exception{
        Main.course=new Course();
        Main.course.courseName=PopUp.getName("My Course");
        Main.course.chapters=new ArrayList<>();
        showCourse();
    }

    public void openExistingCourse(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Couser File","course"));
        File file =fileChooser.showOpenDialog(Main.window);
        try{
            Main.courseFileLocation=file.getAbsolutePath();
            FileInputStream fis=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(fis);
            Main.course=(Course) ois.readObject();
            showCourse();
        }
        catch (Exception e){
            System.out.println("following error occured while opening "+file+"\n"+e);
        }
    }

    public void showCourse() throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Main.window.setTitle(Main.course.courseName);
        Main.window.setResizable(true);
        Scene s=new Scene(root,1200,600);
        Main.window.setX(100);
        Main.window.setY(100);
        Main.window.setWidth(1200);
        Main.window.setHeight(600);
        Main.window.setScene(s);
    }
}
