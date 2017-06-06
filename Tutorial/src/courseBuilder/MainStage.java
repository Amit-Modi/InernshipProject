package courseBuilder;

import course.Course;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.PopUP;

import java.io.File;
import java.util.ArrayList;

import static courseBuilder.Element.*;
import static sample.MyUtil.addNode;

/**
 * Created by ghost on 30/5/17.
 */
public class MainStage {
    private EventHandler<MouseEvent> pressEvent,dragEvent,releseEvent;

    @FXML public void startNewProject(){
        try {
            String title = PopUP.getTextInput("New Course", "Enter Cource Name :");
            if (title != null) {
                Store.selectedCourse = new Course(title);
                ((ScrollPane) Main.window.getScene().lookup("#indexTab")).setContent(Store.selectedCourse.index);
                (((ScrollPane)Main.window.getScene().lookup("#indexTab")).getContent()).setOnMouseClicked(e->{
    //                System.out.println("Selected course : "+Store.selectedCourse.index.getSelectionModel().getSelectedIndex());
                    display(Store.selectedCourse.index.getSelectionModel().getSelectedIndex());
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

    @FXML public void addNewPage(){
        try {
            display(Store.selectedCourse.addSubTopic("New Subtopic"));
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
            display(Store.selectedCourse.deleteTopics());
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
    //        System.out.println("Displaying : "+idx);
            ((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).setContent(Store.selectedCourse.page.get(idx));
            ((ScrollPane) Main.window.getScene().lookup("#pageNote")).setContent(Store.selectedCourse.pageNote.get(idx));
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
        addNode(Store.selectedCourse,box);
        Store.selectedNode=box;
    }

    @FXML public void addTitleField(){
        addTitleField(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addTitleField(Rectangle rec){

        StackPane box=getTitleBox();
        ((TextArea)box.getChildren().get(0)).setPrefWidth(rec.getWidth());
        ((TextArea)box.getChildren().get(0)).setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(box,rec.getX());
        AnchorPane.setTopAnchor(box,rec.getY());
        addNode(Store.selectedCourse,box);
        Store.selectedNode=box;
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
            addNode(Store.selectedCourse,stackPane);
            Store.selectedNode=stackPane;
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

            addNode(Store.selectedCourse,stackPane);
            Store.selectedNode=stackPane;
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

        addNode(Store.selectedCourse,box);
    }
    @FXML public void deleteElement(){
        try {
            Pane p=(Pane) Store.selectedNode.getParent();
            p.getChildren().remove(Store.selectedNode);
            Store.selectedNode=p.getChildren().get(0);
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }
    @FXML public void makeRectangle(){
        final Rectangle rect = new Rectangle(0, 0, 0, 0);
        try {
            final Pane group = (AnchorPane) ((ScrollPane) Main.window.getScene().lookup("#sceneWindow")).getContent();
            Store.flag = false;

            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            pressEvent = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if(e.getButton().equals(MouseButton.SECONDARY)) {
                        System.out.println("mouse clicked at " + e.getSceneX() + "," + e.getSceneY());
                        System.out.println("mouse clicked at " + group.getTranslateX() + "," + group.getLayoutX());
                        Store.orgSceneX = e.getSceneX() - group.getLayoutX();
                        Store.orgSceneY = e.getSceneY() - group.getLayoutY();

                        rect.setX(Store.orgSceneX);
                        rect.setY(Store.orgSceneY);
                        rect.setWidth(0);
                        rect.setHeight(0);

                        group.getChildren().add(rect);
                    }
                }
            };
            dragEvent = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if(e.getButton().equals(MouseButton.SECONDARY)) {
                        double offsetX = e.getSceneX() - Store.orgSceneX;
                        double offsetY = e.getSceneY() - Store.orgSceneY;

                        if (offsetX > 0)
                            rect.setWidth(offsetX);
                        else {
                            rect.setX(e.getSceneX());
                            rect.setWidth(Store.orgSceneX - rect.getX());
                        }

                        if (offsetY > 0) {
                            rect.setHeight(offsetY);
                        } else {
                            rect.setY(e.getSceneY());
                            rect.setHeight(Store.orgSceneY - rect.getY());
                        }
                    }
                }
            };
            releseEvent = new EventHandler<MouseEvent>(){
                public void handle (MouseEvent e) {
                    if(e.getButton().equals(MouseButton.SECONDARY)) {
                        double x = rect.getX();
                        double y = rect.getY();
                        double w = rect.getWidth();
                        double h = rect.getHeight();

                        // remove rubberband
                        rect.setX(0);
                        rect.setY(0);
                        rect.setWidth(0);
                        rect.setHeight(0);

                        group.getChildren().remove(rect);
                    }
                }
            };
            group.addEventHandler(MouseEvent.MOUSE_PRESSED, pressEvent);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragEvent);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED,releseEvent);
        }
        catch (Exception excption){
            System.out.println("exception while creating rectangle\n"+excption.toString());
        }
    }


}
