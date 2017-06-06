package sample;

import course.Course;
import courseBuilder.Store;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


/**
 * Created by ghost on 31/5/17.
 */
public class MyUtil {

    public static void addNode(Course course,Node node){
        if(course.index.getSelectionModel().getSelectedItems().size()>1){
            if(PopUP.confermBox("Confermation Box","You have selected multiple pages in index filed.\n\n   Would you like to add text box to all of them?")){
                for (Integer idx:course.index.getSelectionModel().getSelectedIndices()) {
                    course.page.get(idx).getChildren().add(node);
                    return;
                }
            }
        }
        course.page.get(course.index.getSelectionModel().getSelectedIndex()).getChildren().add(node);
    }

}
