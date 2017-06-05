package courseBuilder;

import course.Course;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import sample.MyUtil;
import sample.MyVideoControl;
import sample.PopUP;

import java.io.File;

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
        TextArea t=MyUtil.getNewTextBox(rec.getWidth(),rec.getHeight());

        StackPane s=MyUtil.getNewStackPane();
        s.getChildren().add(t);
        s.widthProperty().addListener(new ChangeListener(){
            public void changed(ObservableValue observable, Object oldValue, Object newValue){
                System.out.println(oldValue+"|"+newValue);
            }
        });

        AnchorPane.setLeftAnchor(s,rec.getX());
        AnchorPane.setTopAnchor(s,rec.getY());
        Store.selectedCourse.addNode(s);
    }

    @FXML public void addImageBox(){
        addImageBox(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addImageBox(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(sample.Main.window);
        if(file!=null) {
            StackPane stackPane = MyUtil.getNewStackPane();
            Image img = new Image(file.toURI().toString());
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(rec.getWidth());
            imgView.setFitHeight(rec.getHeight());
            stackPane.getChildren().add(imgView);
            AnchorPane.setLeftAnchor(stackPane,rec.getX());
            AnchorPane.setTopAnchor(stackPane,rec.getY());
            Store.selectedCourse.addNode(stackPane);
        }
    }

    @FXML public void addVideoPlayer(){
        addVideoPlayer(new Rectangle(0.0,0.0,100.0,100.0));
    }
    @FXML public void addVideoPlayer(Rectangle rec){
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(sample.Main.window);
        if (file != null) {
            StackPane stackPane = MyUtil.getNewStackPane();
            stackPane.setPrefSize(400, 300);
            Media videoSource = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(videoSource);
            MediaView mediaView = new MediaView();
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.dispose();
                }
            });
            mediaView.setMediaPlayer(mediaPlayer);
            MyVideoControl.addVideoControl(mediaView);
            stackPane.getChildren().addAll(mediaView);
            mediaView.fitHeightProperty().bind(stackPane.heightProperty());
            mediaView.fitWidthProperty().bind(stackPane.heightProperty());
            Store.selectedCourse.addNode(stackPane);
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
