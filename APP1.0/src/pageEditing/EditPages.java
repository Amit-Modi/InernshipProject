package pageEditing;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import sample.Main;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import static pageEditing.Element.*;


/**
 * Created by ghost on 14/6/17.
 */
public class EditPages implements Initializable{

    public static ArrayList<Pane> pages;
    @FXML
    ListView pageList;
    @FXML
    ListView paneList;
    @FXML
    ScrollPane pageWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pageList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    //    int n=Main.currentTopicPages.size();
    //    for(Pane each : Main.currentTopicPages){
    //        Pane eachIndex=each;
    //        eachIndex.setDisable(true);
    //        double scale=(paneList.getWidth()-20)/each.getWidth();
    //        eachIndex.setScaleX(scale);
    //        eachIndex.setScaleY(scale);
    //        paneList.getItems().add(eachIndex);
    //    }
    }

    public void addPage(){
        Pane Pane=new Pane();
        Pane.setPrefWidth(1024);
        Pane.setPrefHeight(768);
        Pane.setStyle("-fx-background-color: green");
        pages.add(Pane);
        pageWindow.setContent(pages.get(pages.size()-1));

        Label label=new Label("Page"+String.valueOf(pages.size()));
        label.setOnMouseClicked(actionEvent->{
            pageList.getSelectionModel().clearSelection();
            pageList.getSelectionModel().select(actionEvent.getSource());
            pageWindow.setContent(pages.get(pageList.getSelectionModel().getSelectedIndex()));
            System.out.println(pageList.getSelectionModel().getSelectedIndex());
        });
        pageList.getItems().add(label);
    }
    public void deletePage(){
        System.out.println(pageList.getSelectionModel().getSelectedIndices());
        ArrayList<Pane> tmp=new ArrayList<>();
        for(Integer each : (ObservableList<Integer>)pageList.getSelectionModel().getSelectedIndices()){
            tmp.add(pages.get(each));
        }
        pages.removeAll(tmp);
        pageList.getItems().removeAll(pageList.getSelectionModel().getSelectedItems());
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

        ((Pane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
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

        ((Pane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
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
            ((Pane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
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

            ((Pane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    /*@FXML public void addMCQ(){
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
        Pane.setTopAnchor(box,rec.getY());
        Pane.setLeftAnchor(box,rec.getX());

        addNode(box);
        MyUtil.setSelectedPane(box);
    }*/

    public void tempFunction(){
        System.out.println(pages);
    }
}
