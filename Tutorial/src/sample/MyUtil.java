package sample;

import course.Course;
import courseBuilder.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import java.util.ArrayList;

public class MyUtil {

    private static EventHandler<MouseEvent> pressEvent,dragEvent,releseEvent;

    public static void startNewCourse(String title){
        Store.selectedCourse=new Course();
        Store.selectedCourse.index = new TreeView<>(new TreeItem<>(title));
        Store.selectedCourse.page = new ArrayList<>();
        Store.selectedCourse.page.add(Layouts.getNormalLayout());
        Store.selectedCourse.pageNote = new ArrayList<>();
        Store.selectedCourse.pageNote.add(new TextArea());
        Store.selectedCourse.index.getSelectionModel().select(0);
    }

    public static void enableRectangle(Integer idx){
        final Rectangle rect = new Rectangle(0, 0, 0, 0);
        try {
            final Pane group = Store.selectedCourse.page.get(idx);
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

    public static void disableRectangle(Integer idx){
        Store.selectedCourse.page.get(idx).removeEventHandler(MouseEvent.MOUSE_PRESSED,pressEvent);
        Store.selectedCourse.page.get(idx).removeEventHandler(MouseEvent.MOUSE_DRAGGED,dragEvent);
        Store.selectedCourse.page.get(idx).removeEventHandler(MouseEvent.MOUSE_RELEASED,releseEvent);
    }

    public static Integer addTopic(String subTopicName){

        try{
            if(Store.selectedCourse.index.getSelectionModel().getSelectedItems().size()>1) {
                throw new Exception("Multiple Topics Selected.\nOnly one selection allowed");
            }
            TreeItem<String> item=new TreeItem<>(subTopicName);
            TreeItem<String> parent=Store.selectedCourse.index.getSelectionModel().getSelectedItem();
            parent.getChildren().add(item);
            Store.selectedCourse.index.getSelectionModel().clearSelection();
            Store.selectedCourse.index.getSelectionModel().select(item);

            Integer idx=Store.selectedCourse.index.getSelectionModel().getSelectedIndex();
//            System.out.println(idx);

            Store.selectedCourse.page.add(idx, Layouts.getNormalLayout());
            TextArea temp=new TextArea();
            temp.setPromptText("Enter Page Notes");
            Store.selectedCourse.pageNote.add(idx,temp);

            parent.setExpanded(true);
//            System.out.println("selected");
            return idx;
        }
        catch (Exception exception){
            PopUP.showException(exception);
            return Store.selectedCourse.index.getSelectionModel().getSelectedIndex();
        }
    }

    public static Integer removeSelectedTopics(){
        try{
            if(Store.selectedCourse.index.getSelectionModel().getSelectedItems().contains(Store.selectedCourse.index.getRoot()))
                throw new Exception("Course Cannot be Deleted From Here");
            for(TreeItem<String> selected:Store.selectedCourse.index.getSelectionModel().getSelectedItems()) {
                Integer idx=Store.selectedCourse.index.getRow(selected);
                if(idx>-1) {
                    removeTopic( selected);
                    deletePage(idx);
                    selected.getParent().getChildren().remove(selected);
                }
            }
            System.out.println(Store.selectedCourse.page.size());
        }
        catch(Exception e){
            PopUP.showException(e);
            return 0;
        }
        return Math.max(Store.selectedCourse.index.getSelectionModel().getSelectedIndex(),0);
    }

    public static Boolean setTitle(String title){
        try{
            Store.selectedCourse.index.getRoot().setValue(title);
            return true;
        }
        catch (Exception exception){
            PopUP.showException(exception);
            return false;
        }
    }

    public static String getTitle(){
        return Store.selectedCourse.index.getRoot().getValue();
    }

    public static void setSelectedPane(Pane pane){
//        System.out.println("Selecting "+pane.toString());
        if( Store.selectedPane!=null)
            Store.selectedPane.setStyle("-fx-border-style: dashed");
        Store.selectedPane=pane;
        Store.selectedPane.setStyle("-fx-border-color: black");
    }

    public static void setSelectedPage(Integer idx){
        try{
            disableRectangle(Store.selectedPage);
        }
        catch (Exception e){}
        Store.selectedPage=idx;
        enableRectangle(Store.selectedPage);
    }

    public static void addNode(Node node){
        if(Store.selectedCourse.index.getSelectionModel().getSelectedItems().size()>1){
            if(PopUP.confermBox("Confermation Box","You have selected multiple pages in index filed.\n\n   Would you like to add text box to all of them?")){
                for (Integer idx:Store.selectedCourse.index.getSelectionModel().getSelectedIndices()) {
                    Store.selectedCourse.page.get(idx).getChildren().add(node);
                    return;
                }
            }
        }
        Store.selectedCourse.page.get(Store.selectedCourse.index.getSelectionModel().getSelectedIndex()).getChildren().add(node);
    }

    public static void removeNode(Pane pane){
        deleteSubElements(pane);
        ((Pane)pane.getParent()).getChildren().remove(pane);
    }

    private static void removeTopic(TreeItem<String> parent){
        try {
            for (int i=parent.getChildren().size()-1;i>=0;i--){
                Integer idx=Store.selectedCourse.index.getRow(parent.getChildren().get(i));
                if(idx>-1) {
                    removeTopic(parent.getChildren().get(i));
                    deletePage(idx);
                    parent.getChildren().remove(i);
                }
            }
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    private static void deletePage(Integer idx){
        try {
            for (Node each : Store.selectedCourse.page.get(idx).getChildren()) {
                deleteSubElements((Pane) each);
            }
            Store.selectedCourse.page.get(idx).getChildren().clear();
            System.out.println("deleteing "+idx);
        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

    private static void deleteSubElements(Pane page){
        try {
            for(Node each:page.getChildren()){
                if(each.getClass()==MediaView.class){
                    ((MediaView)each).getMediaPlayer().dispose();
                }
                else if(each.getClass()==ImageView.class){
                    Image image=((ImageView)each).getImage();
                    image=null;
                    ((ImageView)each).setImage(null);
                    System.gc();
                }
                else if(each.getClass()==Pane.class){
                    deleteSubElements((Pane)each);
                }
            }
            page.getChildren().clear();

        }
        catch (Exception e){
            PopUP.showException(e);
        }
    }

}
