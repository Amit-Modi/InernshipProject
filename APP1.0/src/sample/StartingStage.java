package sample;

import course.Course;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by ghost on 13/6/17.
 */
public class StartingStage {

    public void startNewCourse() throws Exception{
        Main.course=new Course();
        Main.course.courseName="CourseName";
        Main.course.chapters=new ArrayList<>();
        showCourse();
    }

    public void openExistingCourse(){
        FileChooser fileChooser=new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Couser File","course"));
        File file =fileChooser.showOpenDialog(Main.window);
        try{
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
        Main.window.setTitle("My Authoring System");
        Main.window.setResizable(true);
        Scene s=new Scene(root,1200,600);
        Main.window.setWidth(1200);
        Main.window.setHeight(600);
        Main.window.setScene(s);
    }
}
