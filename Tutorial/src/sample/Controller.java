package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.util.Set;


public class Controller {

@FXML
private ListView listView;
private Set<String> stringSet;
ObservableList observableList= FXCollections.observableArrayList();



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
@FXML public void addTextField(Event e){
    TextField t=new TextField();
    t.setPromptText("Enter something");
    t.setOnMouseClicked(em->{
        MyUtil.setToFront((Node)em.getSource());
    });
    t.setOnKeyTyped(ek->{
        MyUtil.setToFront((Node)ek.getSource());
    });
    StackPane s=MyUtil.getNewStackPane();
    s.getChildren().add(t);
    Pane pane=(Pane) (Main.root.lookup("#scene"));
    pane.getChildren().add(s);
}
@FXML public void addText(Event e){
    System.out.println("Function Not Written");
}

@FXML public void addImage(Event e){
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(Main.window);
    if(file!=null) {
        StackPane stackPane = MyUtil.getNewStackPane();
        Image img = new Image(file.toURI().toString());
        ImageView imgView = new ImageView(img);
        stackPane.getChildren().add(imgView);
        ((Pane) Main.root.lookup("#scene")).getChildren().add(stackPane);
    }
}


@FXML public void addVideo() {
    FileChooser chooser = new FileChooser();
    File file = chooser.showOpenDialog(Main.window);
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
        ((Pane) Main.window.getScene().lookup("#scene")).getChildren().addAll(stackPane);
    }
}
public void addBrowser(){
    FileChooser chooser = new FileChooser();
    File file=chooser.showOpenDialog(Main.window);
    if(file!=null){
        StackPane stackPane=MyUtil.getNewStackPane();

        WebView browser=new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(file.toURI().toString());

        stackPane.getChildren().add(browser);

        ((Pane)Main.window.getScene().lookup("#scene")).getChildren().addAll(stackPane);
    }
}
}
