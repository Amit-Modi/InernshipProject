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
import javafx.scene.text.Text;
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

    public static ArrayList<AnchorPane> pages;
    @FXML
    ListView pageList;
    @FXML
    ListView paneList;
    @FXML
    ScrollPane pageWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pageList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for(AnchorPane each : pages){
            Button indexButton=getIndexButton(pages.indexOf(each));
            pageList.getItems().add(indexButton);
        }
        try {
            display(0);
        }catch (Exception e){
            pageWindow.setContent(new Text("no page to display"));
        }
    }

    public void addPage(){
        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setPrefWidth(1024);
        anchorPane.setPrefHeight(768);
        anchorPane.setStyle("-fx-background-color: green");
        pages.add(anchorPane);

        Button button=getIndexButton(pages.size()-1);
        pageList.getItems().add(button);
        display(pages.size()-1);
    }

    private Button getIndexButton(Integer idx){
        Button button=new Button("Pages"+(idx+1));

        button.setOnAction(actionEvent->{
            pageList.getSelectionModel().clearSelection();
            pageList.getSelectionModel().select(actionEvent.getSource());
            display(idx);
        });

        return button;
    }

    private void display(Integer idx){
        pageWindow.setContent(pages.get(idx));
    }

    public void deletePage(){
        ArrayList<AnchorPane> tmp=new ArrayList<>();
        for(Integer each : (ObservableList<Integer>)pageList.getSelectionModel().getSelectedIndices()){
            tmp.add(pages.get(each));
        }
        pages.removeAll(tmp);
        pageList.getItems().removeAll(pageList.getSelectionModel().getSelectedItems());
    }

    @FXML public void addTextField(){
        addTextField(new Rectangle(100.0,300.0,412.0,468.0));
    }
    @FXML public void addTextField(Rectangle rec){

        StackPane box=getTextBox();
        ((TextArea)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextArea)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());

        ((AnchorPane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addTitleField(){
        addTitleField(new Rectangle(100.0,100.0,412.0,100.0));
    }
    @FXML public void addTitleField(Rectangle rec){

        StackPane box=getTitleBox();
        ((TextField)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextField)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());

        ((AnchorPane)pageWindow.getContent()).getChildren().add(box);
        setSelectedPane(box);
    }

    @FXML public void addImageBox(){
        addImageBox(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addImageBox(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if(file!=null) {
            StackPane stackPane = getImageBox(file);
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getWidth());
            ((ImageView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());

            AnchorPane.setLeftAnchor(stackPane,rec.getX());
            AnchorPane.setTopAnchor(stackPane,rec.getY());
            ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    @FXML public void addVideoPlayer(){
        addVideoPlayer(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addVideoPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(pageWindow.getScene().getWindow());
        if (file != null) {
            StackPane stackPane = getVideoBox(file);

            ((MediaView)stackPane.getChildren().get(0)).setFitHeight(rec.getHeight());
            ((MediaView)stackPane.getChildren().get(0)).setFitWidth(rec.getWidth());

            ((AnchorPane)pageWindow.getContent()).getChildren().add(stackPane);
            setSelectedPane(stackPane);
        }
    }

    public void tempFunction(){
        System.out.println(pages);
    }
}
