package pageEditing;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import sample.Main;
import sample.PopUp;

import java.io.File;
import java.util.ArrayList;

public class Element {

    private static Insets margin=new Insets(5);

    private static void setToFront(Node n){
        try {
            n.toFront();
            if (n.getClass() == TextField.class)
                n.getParent().toFront();
            while (n.getParent().getClass() != AnchorPane.class) {
                n.toFront();
                n = n.getParent();
            }
            n.toFront();
            setSelectedPane((Pane) n);
        }
        catch (Exception e){
            PopUp.display("Error!",e.toString());
        }
    }

    public static void enableMovable(Node n){
        n.setOnMousePressed(e->{
            Main.orgSceneX=e.getSceneX();
            Main.orgSceneY=e.getSceneY();
            Main.orgTranslateX=((Node) e.getSource()).getTranslateX();
            Main.orgTranslateY=((Node) e.getSource()).getTranslateY();
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
            setToFront(n);

        });
        n.setOnMouseDragged(e->{
            double offsetX=e.getSceneX()- Main.orgSceneX;
            double offsetY=e.getSceneY()- Main.orgSceneY;
            double newTranslateX= Main.orgTranslateX+offsetX;
            double newTranslateY= Main.orgTranslateY+offsetY;
            ((Node) e.getSource()).setTranslateX(newTranslateX);
            ((Node) e.getSource()).setTranslateY(newTranslateY);
            ((Node) e.getSource()).setCursor(Cursor.MOVE);
        });
        n.setOnMouseEntered(e->{
            ((Node)e.getSource()).setCursor(Cursor.OPEN_HAND);
        });
        n.setOnMouseReleased(e->{
            ((Node)e.getSource()).setCursor(Cursor.OPEN_HAND);
        });
    }

    public static void disableMovable(Node n){
        n.setOnMousePressed(anyEvent->{        });
        n.setOnMouseDragged(anyEvent->{        });
        n.setOnMouseClicked(anyEvent->{        });
        n.setOnMouseReleased(anyEvent->{        });
        n.setOnMouseEntered(anyEvent->{        });
        n.setOnMouseExited(anyEven->{        });
    }

    private static void startVideo(MediaView m){
        m.getMediaPlayer().play();
        m.setOnMouseClicked(e->{
            stopVideo((MediaView) e.getSource());
        });
    }

    private static void stopVideo(MediaView m){
        m.getMediaPlayer().pause();
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }

    private static void addVideoControl(MediaView m){
        m.setOnMouseClicked(e->{
            startVideo((MediaView)e.getSource());
        });
    }

    private static StackPane getContainer(){
        StackPane stackPane=new StackPane();
        stackPane.setStyle("-fx-background-color: transparent;-fx-border-width: 1px;-fx-border-style: dashed;-fx-border-color: black");
        stackPane.prefHeight(Region.USE_COMPUTED_SIZE);
        stackPane.prefWidth(Region.USE_COMPUTED_SIZE);
        enableMovable(stackPane);

        return stackPane;
    }

    public static StackPane getTextBox(){
        TextArea textArea = new TextArea();
        textArea.setStyle("-fx-background-color: transparent");
        StackPane.setMargin(textArea,margin);
        textArea=enableEdit(textArea);

        StackPane container=getContainer();
        container.getChildren().add(textArea);
        return container;
    }
    public static TextArea enableEdit(TextArea textArea){
        textArea.setEditable(true);
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
        textArea.setPromptText("");
        textArea.setOnMouseClicked(em->{
        });
        textArea.setOnKeyTyped(ek->{
        });
        return textArea;
    }

    public static StackPane getTitleBox(){
        TextField box = new TextField();
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");
        box=enableEdit(box);

        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
    }
    public static TextField enableEdit(TextField textField){
        textField.setEditable(true);
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
        textField.setPromptText("");
        textField.setOnMouseClicked(em->{
        });
        textField.setOnKeyTyped(ek->{
        });
        return textField;
    }


    public static StackPane getImageBox(File imagePath){

        ImageView box = new ImageView(new Image(imagePath.toURI().toString()));
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");

        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
    }

    public static StackPane getVideoBox(File videoPath){
        MediaPlayer mediaPlayer=new MediaPlayer(new Media(videoPath.toURI().toString()));
        MediaView box = new MediaView(mediaPlayer);
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.dispose();
            }
        });
        addVideoControl(box);


        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
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

    public static void setSelectedPane(Pane pane){
//        System.out.println("Selecting "+pane.toString());
        if( Main.selectedPane!=null)
            Main.selectedPane.setStyle("-fx-border-style: dashed");
        Main.selectedPane=pane;
        Main.selectedPane.setStyle("-fx-border-color: black");
    }
}
