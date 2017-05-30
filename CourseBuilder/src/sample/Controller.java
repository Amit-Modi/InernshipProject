package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Set;


public class Controller {

    @FXML
    private ListView listView;
    private Set<String> stringSet;
    ObservableList observableList= FXCollections.observableArrayList();

    @FXML public void mouseButtenPressed(MouseEvent e){
        System.out.println("pressed");
        System.out.println(((Node)e.getSource()).getId());
        Main.orgSceneX=e.getSceneX();
        Main.orgSceneY=e.getSceneY();
        Main.orgTranslateX=((Node) e.getSource()).getTranslateX();
        Main.orgTranslateY=((Node) e.getSource()).getTranslateY();
        Node temp=(javafx.scene.Node)e.getSource();
        while(temp!=Main.root) {
            temp.toFront();
            temp=temp.getParent();
        }
    }

    @FXML public void mouseButtonDragged(MouseEvent e){
        System.out.println("dragged");
        System.out.println(((Node)e.getSource()).getId());
        double offsetX=e.getSceneX()-Main.orgSceneX;
        double offsetY=e.getSceneY()-Main.orgSceneY;
        double newTranslateX=Main.orgTranslateX+offsetX;
        double newTranslateY=Main.orgTranslateY+offsetY;

        ((Node) e.getSource()).setTranslateX(newTranslateX);
        ((Node) e.getSource()).setTranslateY(newTranslateY);
    }

    @FXML public void setListView(MouseEvent e){
        stringSet.add("1");
        stringSet.add("2");
        stringSet.add("3");
        observableList.setAll(stringSet);
        ((ListView<String>)e.getSource()).getItems().addAll("Amit","modi");
    }

    @FXML public void enableEditText(MouseEvent e){
        ((TextArea)e.getSource()).setEditable(true);
    }
    @FXML public void disableEditText(MouseEvent e){
        ((TextArea)e.getSource()).setEditable(false);
    }
}
