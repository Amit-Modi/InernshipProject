package courseBuilder;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class Element {

    private Insets margin=new Insets(10);

    private static void makeMovable(Node n){
        n.setOnMousePressed(e->{
            Store.orgSceneX=e.getSceneX();
            Store.orgSceneY=e.getSceneY();
            Store.orgTranslateX=((Node) e.getSource()).getTranslateX();
            Store.orgTranslateY=((Node) e.getSource()).getTranslateY();
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
        });
        n.setOnMouseDragged(e->{
            double offsetX=e.getSceneX()- Store.orgSceneX;
            double offsetY=e.getSceneY()- Store.orgSceneY;
            double newTranslateX= Store.orgTranslateX+offsetX;
            double newTranslateY= Store.orgTranslateY+offsetY;
            ((Node) e.getSource()).setTranslateX(newTranslateX);
            ((Node) e.getSource()).setTranslateY(newTranslateY);
            ((Node) e.getSource()).setCursor(Cursor.MOVE);
        });
        n.setOnMouseEntered(e->{
            ((Node)e.getSource()).setCursor(Cursor.OPEN_HAND);
        });
    }

    private StackPane getContainer(){
        StackPane stackPane=new StackPane();
        stackPane.getStyleClass().add("styleSheet");
        stackPane.prefHeight(Region.USE_COMPUTED_SIZE);
        stackPane.prefWidth(Region.USE_COMPUTED_SIZE);
        makeMovable(stackPane);
        return stackPane;
    }

    public StackPane getTextBox(){
        TextArea box = new TextArea();
        StackPane.setMargin(box,margin);
        box.setStyle("-fx-background-color: transparent");

        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
    }

    public StackPane getImageBox(File imagePath){

        ImageView box = new ImageView(new Image(imagePath.toURI().toString()));

        StackPane container=getContainer();
        container.getChildren().add(box);
        return container;
    }

    public StackPane getVideoBox(File videoPath){
        MediaView box = new MediaView(new MediaPlayer(new Media(videoPath.toURI().toString())));

        StackPane container=getContainer();
        container.getChildren().add(box);

        return container;
    }
}
