package pageEditing;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import pageEditing.EditPages;
import sample.PopUp;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static pageEditing.EditPages.enableMovable;
import static pageEditing.EditPages.setToFront;

public class Element {

    private static ScheduledThreadPoolExecutor executor;
    private static ScheduledFuture<?> scheduledFuture;

    private static Insets margin=new Insets(5);

    private static StackPane getContainer(){
        StackPane stackPane=new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;-fx-border-width: 1px;-fx-border-style: dashed;-fx-border-color: black");
        stackPane.prefHeight(Region.USE_COMPUTED_SIZE);
        stackPane.prefWidth(Region.USE_COMPUTED_SIZE);
        enableMovable(stackPane);

        return stackPane;
    }

    public static StackPane getTextBox(Rectangle rec){
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-background-color: transparent;");
        StackPane.setMargin(textArea,margin);
        textArea.setFont(javafx.scene.text.Font.font(
                textArea.getFont().getFamily(),
                18
        ));
        textArea=enableEdit(textArea);

        StackPane container=getContainer();
        container.getChildren().add(textArea);

        textArea.setPrefWidth(rec.getWidth());
        textArea.setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(container,rec.getX());
        AnchorPane.setTopAnchor(container,rec.getY());
        return container;
    }
    public static TextArea enableEdit(TextArea textArea){
        textArea.setEditable(true);
        //System.out.println("textArea editing enabled");
        textArea.setPromptText("Click to edit");
        textArea.setOnMouseClicked(em->{
            setToFront((Node)em.getSource());
        });
        textArea.setOnKeyTyped(ek->{
            setToFront((Node)ek.getSource());
        });
        return textArea;
    }
    public static TextArea disableEdit(TextArea textArea){
        textArea.setEditable(false);
        //System.out.println("textArea editing disabled");
        textArea.setPromptText("");
        textArea.setOnMouseClicked(em->{
        });
        textArea.setOnKeyTyped(ek->{
        });
        return textArea;
    }

    public static StackPane getTitleBox(Rectangle rec){
        TextField box = new TextField();
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent;");
        box.setFont(javafx.scene.text.Font.font(
                box.getFont().getFamily(),
                32
        ));
        box=enableEdit(box);

        StackPane container=getContainer();
        container.getChildren().add(box);

        box.setPrefWidth(rec.getWidth());
        box.setPrefHeight(rec.getHeight());
        AnchorPane.setLeftAnchor(container,rec.getX());
        AnchorPane.setTopAnchor(container,rec.getY());
        return container;
    }
    public static TextField enableEdit(TextField textField){
        textField.setEditable(true);
        //System.out.println("textfield editing enabled");
        textField.setPromptText("Click to enter title");
        textField.setOnMouseClicked(em->{
            setToFront((Node)em.getSource());
        });
        textField.setOnKeyTyped(ek->{
            setToFront((Node)ek.getSource());
        });
        return textField;
    }
    public static TextField disableEdit(TextField textField){
        textField.setEditable(false);
        //System.out.println("textfield editing disabled");
        textField.setPromptText("");
        textField.setOnMouseClicked(em->{
        });
        textField.setOnKeyTyped(ek->{
        });
        return textField;
    }


    public static StackPane getImageBox(Image image,Rectangle rec){

        ImageView box = new ImageView(image);
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");

        StackPane container=getContainer();
        container.getChildren().add(box);

        box.setFitHeight(rec.getWidth());
        box.setFitHeight(rec.getHeight());

        AnchorPane.setLeftAnchor(container,rec.getX());
        AnchorPane.setTopAnchor(container,rec.getY());
        return container;
    }

    public static StackPane getVideoBox(Media media,Rectangle rec){
        StackPane container=getContainer();
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        MediaView box = new MediaView(mediaPlayer);
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");

        VBox vBox=new VBox();
        vBox.setMinHeight(40);
        vBox.setMaxHeight(40);
        vBox.setPadding(new Insets(0,5,5,5));
        vBox.prefWidthProperty().bind(box.fitWidthProperty());
        vBox.minWidthProperty().bind(box.fitWidthProperty());
        vBox.maxWidthProperty().bind(box.fitWidthProperty());
        vBox.setOnMouseClicked(e->{e.consume();});
        vBox.setOnMouseDragged(e->{e.consume();});
        vBox.setOnMousePressed(e->{e.consume();});
        vBox.setOnMouseReleased(e->{e.consume();});
        StackPane.setAlignment(vBox, Pos.BOTTOM_CENTER);
        box.setOnMouseMoved(action->{
            if(scheduledFuture!=null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()){
                scheduledFuture.cancel(false);
            }
            vBox.setVisible(true);
            executor = new ScheduledThreadPoolExecutor(1);
            executor.setRemoveOnCancelPolicy(true);
            scheduledFuture=executor.schedule(()->vBox.setVisible(false),3, TimeUnit.SECONDS);
        });

        Slider progressBar=new Slider();
        progressBar.setMinHeight(10);
        progressBar.setMaxHeight(10);
        progressBar.prefWidthProperty().bind(vBox.widthProperty());
        progressBar.minWidthProperty().bind(vBox.widthProperty());
        progressBar.maxWidthProperty().bind(vBox.widthProperty());
        progressBar.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if(progressBar.isValueChanging()) {
                    try {
                        //System.out.println("progressBar change observed. progressing to "+box.getMediaPlayer().getMedia().getDuration().multiply(progressBar.getValue()/100.0));
                        box.getMediaPlayer().seek(box.getMediaPlayer().getMedia().getDuration().multiply(progressBar.getValue() / 100.0));
                    }catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        });

        HBox controls=new HBox();
        controls.setMinHeight(30);
        controls.setMaxHeight(30);
        controls.prefWidthProperty().bind(vBox.widthProperty());
        controls.minWidthProperty().bind(vBox.widthProperty());
        controls.maxWidthProperty().bind(vBox.widthProperty());

        Button playpause =getMediaButton();
        playpause.setText(">/||");
/*        playpause.setOnAction(action->{
            playMedia(box,progressBar,controls);
        });
        box.setOnMouseClicked(action->{
            playMedia(box,progressBar,controls);
        });*/

        Button stop =getMediaButton();
        stop.setText("STOP");
        stop.setOnAction(action->{
            stopMedia(box,progressBar,controls);
        });
        box.getMediaPlayer().setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                stopMedia(box,progressBar,controls);
            }
        });

        Button mute =getMediaButton();
        mute.setText("MUTE");
        mute.setOnAction(action->{
            muteMedia(box,progressBar,controls);
        });

        Slider volumeSlider=new Slider();
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                box.getMediaPlayer().setVolume(volumeSlider.getValue()/100);
            }
        });
        volumeSlider.setValue(80);

        Label playtime=new Label();
        playtime.setStyle("-fx-text-fill: white;-fx-font-family: sans-serif");
        box.getMediaPlayer().setOnReady(new Runnable() {
            @Override
            public void run() {
                playtime.setText(formatTime(new Duration(0),box.getMediaPlayer().getMedia().getDuration()));
                stopMedia(box,progressBar,controls);
                box.getMediaPlayer().currentTimeProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Duration currentTime=box.getMediaPlayer().getCurrentTime();
                                Duration duration=box.getMediaPlayer().getMedia().getDuration();
                                //System.out.println("curret time duration "+currentTime);
                                //System.out.println("total time duration "+box.getMediaPlayer().getMedia().getDuration());
                                if(!progressBar.isValueChanging()){
                                    //System.out.println("progressBar value updating to "+(currentTime.toMillis()/box.getMediaPlayer().getMedia().getDuration().toMillis())*100);
                                    progressBar.setValue((currentTime.toMillis()/duration.toMillis())*100);
                                }
                                playtime.setText(formatTime(currentTime,duration));
                            }
                        });
                    }
                });
            }
        });
        Region region =new Region();
        HBox.setHgrow(region,Priority.ALWAYS);

        Button fullScreen = getMediaButton();

        controls.getChildren().addAll(playpause,stop,mute,volumeSlider,playtime,region,fullScreen);

        vBox.getChildren().addAll(progressBar,controls);

        container.getChildren().addAll(box,vBox);
        vBox.setVisible(false);

        box.setFitHeight(rec.getWidth());
        box.setFitHeight(rec.getHeight());

        AnchorPane.setLeftAnchor(container,rec.getX());
        AnchorPane.setTopAnchor(container,rec.getY());
        return container;
    }

    private static Button getMediaButton() {
        Button button=new Button();
        button.setMaxHeight(30);
        button.setMinHeight(30);
        button.setMaxWidth(30);
        button.setMinWidth(30);
        button.setStyle("-fx-text-fill: white;-fx-background-color: transparent;-fx-border-color: transparent;");
        return button;
    }
    private static void playMedia(MediaView mediaView,Slider progressBar,HBox controls){
        mediaView.getMediaPlayer().play();
        Button playpause=(Button) controls.getChildren().get(0);
//        playpause.setText("||");
        playpause.setOnAction(action->{
            pauseMedia(mediaView,progressBar,controls);
        });
        mediaView.setOnMouseClicked(action->{
            pauseMedia(mediaView, progressBar, controls);
        });
    }
    private static void pauseMedia(MediaView mediaView,Slider progressBar,HBox controls){
        mediaView.getMediaPlayer().pause();
        Button playpause=(Button) controls.getChildren().get(0);
//        playpause.setText("play");
        playpause.setOnAction(action->{
            playMedia(mediaView,progressBar,controls);
        });
        mediaView.setOnMouseClicked(action->{
            playMedia(mediaView,progressBar,controls);
        });
    }
    private static void stopMedia(MediaView mediaView,Slider progressBar,HBox controls){
        mediaView.getMediaPlayer().seek(new Duration(0));
        pauseMedia(mediaView, progressBar, controls);
        //mediaView.getMediaPlayer().stop();
    }
    private static void muteMedia(MediaView mediaView,Slider progressBar,HBox controls){
        if(mediaView.getMediaPlayer().isMute()){
            mediaView.getMediaPlayer().setMute(false);
        }
        else{
            mediaView.getMediaPlayer().setMute(true);
        }
    }
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public static StackPane getBrowser(File filePath){
        WebView browser=new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(filePath.toURI().toString());

        StackPane conainer=getContainer();
        conainer.getChildren().add(browser);
        return conainer;
    }

    public static StackPane getMCQ(String question, ArrayList<String> options, Integer answer,String explanation){
        TextArea questionField=new TextArea(question);
        VBox.setMargin(questionField,margin);
        questionField.setStyle("-fx-background-color: transparent");

        VBox box = new VBox();
        box.getChildren().add(questionField);

        ToggleGroup group = new ToggleGroup();
        for (String option:options) {
            RadioButton temp=new RadioButton(option);
            temp.setToggleGroup(group);
            VBox.setMargin(temp,new Insets(5,0,5,30));
            box.getChildren().add(temp);
        }
        Button submit = new Button("Submit");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(((RadioButton)group.getSelectedToggle()).getText().equals(answer)){
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setText("Corrent Answer");
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setStyle("-fx-background-color: lightgreen");
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setStyle("-fx-border-color: green");
                }
                else{
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setAccessibleText("Wrong Answer");
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setStyle("-fx-background-color: lightcoral");
                    ((Label)(box.getChildren().get(box.getChildren().size()-2))).setStyle("-fx-border-color: red");
                }
                ((TextField)box.getChildren().get(box.getChildren().size()-1)).setVisible(true);
            }
        });
        Label result=new Label("Choose an option");
        TextField reason=new TextField(explanation);
        reason.setVisible(false);

        box.getChildren().addAll(submit,result,reason);

        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
    }

}