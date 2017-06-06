package sample;

import courseBuilder.Element;
import courseBuilder.Store;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;


public class Controller {

    Region region;

    @FXML public void enableEditText(MouseEvent e){
    ((TextArea)e.getSource()).setEditable(true);
}

    @FXML public void disableEditText(MouseEvent e){
    ((TextArea)e.getSource()).setEditable(false);
}

    @FXML public void addTextField(Event e){

    StackPane s=Element.getTextBox();
       // s.widthProperty().addListener(new ChangeListener(){
       //     public void changed(ObservableValue observable, Object oldValue, Object newValue){
       //         System.out.println(oldValue+"|"+newValue);
       //     }
       // });
    Pane pane=(Pane) (Main.root.lookup("#scene"));
    pane.getChildren().add(s);

    System.out.println(s.localToScene(s.getBoundsInLocal()).getMaxX());
//    AnchorPane.setBottomAnchor(region,AnchorPane.getBottomAnchor(s));
//    AnchorPane.setLeftAnchor(region,AnchorPane.getLeftAnchor(s)+s.getWidth()/2);
}

    @FXML public void addImage(){
        try {
            region = new Region();
            region.setPrefSize(10, 10);
            region.setStyle("-fx-background-color: #0016ff");
            AnchorPane.setBottomAnchor(region, 0.0);
            AnchorPane.setLeftAnchor(region, (((Pane) Main.root.lookup("#scene")).getWidth() / 2) - 2.5);
            ((Pane)Main.root.lookup("#scene")).getChildren().add(region);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(Main.window);
    if(file!=null) {
        StackPane stackPane = Element.getImageBox(file);
        ((Pane) Main.root.lookup("#scene")).getChildren().add(stackPane);
    }
}

    @FXML public void addVideo() {
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(Main.window);
    if (file != null) {
        StackPane stackPane = Element.getVideoBox(file);
        ((MediaView)stackPane.getChildren().get(0)).setFitWidth(400);
        ((MediaView)stackPane.getChildren().get(0)).setFitHeight(300);
        ((Pane) Main.window.getScene().lookup("#scene")).getChildren().addAll(stackPane);
    }
}

    public void addBrowser(){
    FileChooser chooser = new FileChooser();
    File file=chooser.showOpenDialog(Main.window);
    if(file!=null){
        StackPane stackPane=Element.getBrowser(file);
        ((Pane)Main.window.getScene().lookup("#scene")).getChildren().addAll(stackPane);
    }
}

    @FXML public void makeDraggable(){
        try {
            Region topleft, topright, bottomleft, bottomright;
            bottomright = (Region)Main.root.lookup("#scene").lookup("#bottomright");
            bottomleft = (Region)Main.root.lookup("#scene").lookup("#bottomleft");
            topleft = (Region)Main.root.lookup("#scene").lookup("#topleft");
            topright = (Region)Main.root.lookup("#scene").lookup("#topright");
            System.out.println("taken");
            try {
                topleft.setOnMouseEntered(e -> {
                    topleft.setCursor(Cursor.E_RESIZE);
                });
                topright.setOnMouseEntered(e -> {
                    topright.setCursor(Cursor.E_RESIZE);
                });
                bottomleft.setOnMouseEntered(e -> {
                    bottomleft.setCursor(Cursor.E_RESIZE);
                });
                bottomright.setOnMouseEntered(e -> {
                    bottomright.setCursor(Cursor.E_RESIZE);
                });
                System.out.println("mouse entered set");
            }catch (Exception e){
                System.out.println(e.toString());
            }
            try {
                topleft.setOnMousePressed(e -> {
                    Store.orgSceneX = e.getSceneX();
                    Store.orgSceneY = e.getSceneY();
                    Store.orgTranslateX = topleft.getTranslateX();
                    Store.orgTranslateY = topleft.getTranslateY();
                });
                topright.setOnMousePressed(e -> {
                    Store.orgSceneX = e.getSceneX();
                    Store.orgSceneY = e.getSceneY();
                    Store.orgTranslateX = topright.getTranslateX();
                    Store.orgTranslateY = topright.getTranslateY();
                    ((Node) e.getSource()).setCursor(Cursor.E_RESIZE);
                });
                bottomleft.setOnMousePressed(e -> {
                    Store.orgSceneX = e.getSceneX();
                    Store.orgSceneY = e.getSceneY();
                    Store.orgTranslateX = bottomleft.getTranslateX();
                    Store.orgTranslateY = bottomleft.getTranslateY();
                });
                bottomright.setOnMousePressed(e -> {
                    Store.orgSceneX = e.getSceneX();
                    Store.orgSceneY = e.getSceneY();
                    Store.orgTranslateX = bottomright.getTranslateX();
                    Store.orgTranslateY = bottomright.getTranslateY();
                });
                System.out.println("mouse pressed set");
            }catch (Exception e){
                System.out.println(e.toString());
            }
            try {
                topleft.setOnMouseDragged(e -> {
                    double offsetX = e.getSceneX() - Store.orgSceneX;
                    double offsetY = e.getSceneY() - Store.orgSceneY;
                    double newTranslateX = Store.orgTranslateX + offsetX;
                    double newTranslateY = Store.orgTranslateY + offsetY;
                    topleft.setTranslateX(newTranslateX);
                    topleft.setTranslateY(newTranslateY);
                    topright.setTranslateY(newTranslateY);
                    bottomleft.setTranslateX(newTranslateX);
                });
                topright.setOnMouseDragged(e -> {
                    double offsetX = e.getSceneX() - Store.orgSceneX;
                    double offsetY = e.getSceneY() - Store.orgSceneY;
                    double newTranslateX = Store.orgTranslateX + offsetX;
                    double newTranslateY = Store.orgTranslateY + offsetY;
                    topright.setTranslateX(newTranslateX);
                    topright.setTranslateY(newTranslateY);
                    topleft.setTranslateY(newTranslateY);
                    bottomright.setTranslateX(newTranslateX);
                });
                bottomleft.setOnMouseDragged(e -> {
                    double offsetX = e.getSceneX() - Store.orgSceneX;
                    double offsetY = e.getSceneY() - Store.orgSceneY;
                    double newTranslateX = Store.orgTranslateX + offsetX;
                    double newTranslateY = Store.orgTranslateY + offsetY;
                    bottomleft.setTranslateX(newTranslateX);
                    bottomleft.setTranslateY(newTranslateY);
                    bottomright.setTranslateY(newTranslateY);
                    topleft.setTranslateX(newTranslateX);
                });
                bottomright.setOnMouseDragged(e -> {
                    double offsetX = e.getSceneX() - Store.orgSceneX;
                    double offsetY = e.getSceneY() - Store.orgSceneY;
                    double newTranslateX = Store.orgTranslateX + offsetX;
                    double newTranslateY = Store.orgTranslateY + offsetY;
                    bottomright.setTranslateX(newTranslateX);
                    bottomright.setTranslateY(newTranslateY);
                    bottomleft.setTranslateY(newTranslateY);
                    topright.setTranslateX(newTranslateX);
                });
                System.out.println("mouse dragged set");
            }catch (Exception e){
                System.out.println(e.toString());
            }
            TextArea t = (TextArea) Main.root.lookup("#textArea");

            t.prefWidthProperty();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void startVideo(MediaView m){
        m.getMediaPlayer().play();
        m.setOnMouseClicked(e->{
            stopVideo((MediaView) e.getSource());
        });
    }

    public static void stopVideo(MediaView m){
        m.getMediaPlayer().pause();
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }

    public static void addVideoControl(MediaView m){
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }
}
