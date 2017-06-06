package course;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import sample.Layouts;
import sample.Main;
import sample.MyUtil;
import sample.PopUP;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by ghost on 30/5/17.
 */
public class Course {

    public TreeView<String> index;
    public ArrayList<AnchorPane> page;
    public ArrayList<TextArea> pageNote;


    private void removeChildern(TreeItem<String> parent){
        try {
            for (TreeItem<String> treeItem : parent.getChildren()) {
                removeChildern(treeItem);
            }
            deletePage(this.index.getRow(parent));
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    private void deletePage(Integer idx){
        this.pageNote.remove(idx);
        this.page.remove(idx);
    }


    public Course(String title){
//initialising index
        TreeItem<String> root=new TreeItem<>(title);
        this.index=new TreeView<>(root);
        this.index.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//initialising page
        this.page=new ArrayList<>();
        page.add(Layouts.getNormalLayout());

//initialising pageNote
        pageNote=new ArrayList<>();
        TextArea temp=new TextArea();
        temp.setPromptText("Enter Page Notes");
        pageNote.add(temp);

//selecting root index
        this.index.getSelectionModel().select(0);
    }

    public Boolean setTitle(String title){
        try{
            this.index.getRoot().setValue(title);
            return true;
        }
        catch (Exception exception){
            PopUP.showException(exception);
            return false;
        }
    }

    public String getTitle(){
        return this.index.getRoot().getValue();
    }

    public Integer addSubTopic(String subTopicName){

        try{
            if(this.index.getSelectionModel().getSelectedItems().size()>1) {
                throw new Exception("Multiple Topics Selected.\nOnly one selection allowed");
            }
            TreeItem<String> item=new TreeItem<>(subTopicName);
            TreeItem<String> parent=this.index.getSelectionModel().getSelectedItem();
            parent.getChildren().add(item);
            this.index.getSelectionModel().clearSelection();
            this.index.getSelectionModel().select(item);

            Integer idx=this.index.getSelectionModel().getSelectedIndex();
//            System.out.println(idx);

            this.page.add(idx, Layouts.getNormalLayout());
            TextArea temp=new TextArea();
            temp.setPromptText("Enter Page Notes");
            this.pageNote.add(idx,temp);

            parent.setExpanded(true);
//            System.out.println("selected");
            return idx;
        }
        catch (Exception exception){
            PopUP.showException(exception);
            return this.index.getSelectionModel().getSelectedIndex();
        }
    }

    public Integer deleteTopics(){

        try{
            if(this.index.getSelectionModel().getSelectedItems().contains(this.index.getRoot()))
                throw new Exception("Course Cannot be Deleted From Here");
            Integer idx=this.index.getSelectionModel().getSelectedIndex();
            for(TreeItem<String> selected:this.index.getSelectionModel().getSelectedItems()) {
                removeChildern(selected);
                selected.getParent().getChildren().remove(selected);
            }
            return idx;
        }
        catch(Exception e){
            PopUP.showException(e);
            return 0;
        }
    }

}
