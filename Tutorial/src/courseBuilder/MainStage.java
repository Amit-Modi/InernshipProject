package courseBuilder;

import course.Course;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.MyUtil;
import sample.PopUP;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static courseBuilder.Element.*;
import static sample.MyUtil.addNode;

/**
 * Created by ghost on 30/5/17.
 */
public class MainStage implements Initializable{

    ContextMenu indexContextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexContextMenu=new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MyUtil.removeSelectedTopics();
            }
        });
        MenuItem rename = new MenuItem("Rename");
        MenuItem addSubtopic = new MenuItem("Add Subtopic");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem open = new MenuItem("Open");
        indexContextMenu.getItems().addAll(open,copy,cut,paste,addSubtopic,rename,delete);
    }

    @FXML public void startNewProject(){
        try {
            String title = PopUP.getTextInput("New Course", "Enter Cource Name :");
            if (title != null) {
                MyUtil.startNewCourse(title);
                ((ScrollPane) Main.window.getScene().lookup("#indexTab")).setContent(Store.selectedCourse.index);
                (((ScrollPane)Main.window.getScene().lookup("#indexTab")).getContent()).setOnMouseClicked(e->{
    //                System.out.println("Selected course : "+Store.selectedCourse.index.getSelectionModel().getSelectedIndex());
                    if(e.getButton()==MouseButton.PRIMARY){
                        display(Store.selectedCourse.index.getSelectionModel().getSelectedIndex());
                    }
                    else if(e.getButton()==MouseButton.SECONDARY){
                        indexContextMenu.show((Main.window.getScene().getRoot()),e.getSceneX(),e.getSceneY());
                    }
                });
                display(0);
                //((ListView)Main.window.getScene().lookup("#indexMenu")).getSelectionModel().select(0);
                //((ListView)Main.window.getScene().lookup("#indexMenu")).getFocusModel().focus(0);
            }

        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void removeContextMenu(){
        indexContextMenu.hide();
    }

    @FXML public void addNewPage(){
        try {
            display(MyUtil.addTopic("New Subtopic"));
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
            display(MyUtil.removeSelectedTopics());
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }
/*
    @FXML public void changePage(){
        System.out.println("changing page");
        display(Store.selectedCourse.index.getSelectionModel().getSelectedIndex());
    }
*/

    @FXML public void display(Integer idx){
        try {
            System.out.println("Displaying : "+idx);
            ((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).setContent(Store.selectedCourse.page.get(idx));
            ((ScrollPane) Main.window.getScene().lookup("#pageNote")).setContent(Store.selectedCourse.pageNote.get(idx));
            MyUtil.setSelectedPage(idx);
    //        scaleWindowtoFitScreen();
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void scaleWindowtoFitScreen(){
        try {
            System.out.println(((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).getHeight());
            ScrollPane temp = (ScrollPane) Main.window.getScene().lookup("#sceneWindow");
            Double h = (temp.getHeight()-5)/(((AnchorPane)temp.getContent()).getHeight());
            Double w = (temp.getWidth()-5)/(((AnchorPane)temp.getContent()).getWidth());
            scaleWindow(Math.min(h,w));
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    @FXML public void scaleWindow(double a){
        ScrollPane scrollPane=(ScrollPane)Main.window.getScene().lookup("#sceneWindow");
        AnchorPane anchorPane=(AnchorPane)scrollPane.getContent();
        //anchorPane.setTranslateX((anchorPane.getWidth()*(a-1.0)/2)+5);
        //anchorPane.setTranslateY((anchorPane.getHeight()*(a-1.0)/2)+5);
        anchorPane.setScaleX(a);
        anchorPane.setScaleY(a);
    }

    @FXML public void addTextField(){
        addTextField(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addTextField(Rectangle rec){

        StackPane box=getTextBox();
        ((TextArea)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextArea)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());
        addNode(box);
        MyUtil.setSelectedPane(box);
    }

    @FXML public void addTitleField(){
        addTitleField(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addTitleField(Rectangle rec){

        StackPane box=getTitleBox();
        ((TextField)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextField)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());
        addNode(box);
        MyUtil.setSelectedPane(box);
    }

    @FXML public void addImageBox(){
        addImageBox(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addImageBox(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(sample.Main.window);
        if(file!=null) {
            StackPane stackPane = getImageBox(file);
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getWidth());
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());

            AnchorPane.setLeftAnchor(stackPane,rec.getX());
            AnchorPane.setTopAnchor(stackPane,rec.getY());
            addNode(stackPane);
            MyUtil.setSelectedPane(stackPane);
        }
    }

    @FXML public void addVideoPlayer(){
        addVideoPlayer(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addVideoPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(sample.Main.window);
        if (file != null) {
            StackPane stackPane = getVideoBox(file);

            ((MediaView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
            ((MediaView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

            addNode(stackPane);
            MyUtil.setSelectedPane(stackPane);
        }
    }

    @FXML public void addMCQ(){
        addMCQ(new Rectangle(100.0,100.0,600,200.0));
    }
    @FXML public void addMCQ(Rectangle rec){
        ArrayList<String> options=new ArrayList<>();
        options.add("Option1");
        options.add("Option2");
        options.add("Option3");
        options.add("Option4");
        StackPane box=getMCQ("Write a question.",options,0,"Write Explanation.");

        ((Pane)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((Pane)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setTopAnchor(box,rec.getY());
        AnchorPane.setLeftAnchor(box,rec.getX());

        addNode(box);
        MyUtil.setSelectedPane(box);
    }

    @FXML public void deleteElement(){
        if(Store.selectedPane==null){
            PopUP.display("Error","No element selected");
        }
        else {
            MyUtil.removeNode(Store.selectedPane);
        }
    }


}
