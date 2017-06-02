package courseBuilder;

import course.Course;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import sample.PopUP;

import java.util.List;

/**
 * Created by ghost on 30/5/17.
 */
public class MainStage {

    @FXML public void startNewProject(){
        try {
            String title = PopUP.getTextInput("New Course", "Enter Cource Name :");
            if (title != null) {
                Course course = new Course(title);
                SelectedItems.selectedCourse = course;
                ((ScrollPane) Main.window.getScene().lookup("#indexTab")).setContent(course.index);
                display(0);
                //((ListView)Main.window.getScene().lookup("#indexMenu")).getSelectionModel().select(0);
                //((ListView)Main.window.getScene().lookup("#indexMenu")).getFocusModel().focus(0);
            }
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void addNewPage(){
        try {
            display(SelectedItems.selectedCourse.addSubTopic("New Subtopic"));
        }
        catch (Exception e){
            PopUP.showException(e);
        }
        //((TreeView)((ScrollPane)Main.window.getScene().lookup("#indexTab")).getContent()).scrollTo(idx);
        //((TreeView)((ScrollPane)Main.window.getScene().lookup("#indexTab")).getContent()).getFocusModel().focus(idx);
        //((TreeView)((ScrollPane)Main.window.getScene().lookup("#indexTab")).getContent()).getSelectionModel().clearAndSelect(idx);

    }

    @FXML public void removePages(){

        try{
            display(SelectedItems.selectedCourse.deleteTopics());
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void display(Integer idx){
        try {
            ((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).setContent(SelectedItems.selectedCourse.page.get(idx));
            ((ScrollPane) Main.window.getScene().lookup("#pageNote")).setContent(SelectedItems.selectedCourse.pageNote.get(idx));
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void scaleWindowtoFitScreen(){
        try {
            System.out.println(((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).getHeight());
            ScrollPane temp = (ScrollPane) Main.window.getScene().lookup("#sceneWindow");
            Double h = (temp.getHeight())/(((AnchorPane)temp.getContent()).getHeight());
            Double w = (temp.getWidth())/(((AnchorPane)temp.getContent()).getWidth());
            scaleWindow(Math.min(h,w));
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void scaleWindow(double a){
        ((AnchorPane)((ScrollPane)Main.window.getScene().lookup("#sceneWindow")).getContent()).setScaleX(a);
        ((AnchorPane)((ScrollPane)Main.window.getScene().lookup("#sceneWindow")).getContent()).setScaleY(a);
    }
}
