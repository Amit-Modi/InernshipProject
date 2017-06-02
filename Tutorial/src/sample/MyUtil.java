package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;


/**
 * Created by ghost on 31/5/17.
 */
public class MyUtil {

    public static void setToFront(Node n){
        n.toFront();
        if(n.getClass()== TextField.class)
            n.getParent().toFront();
/*        while(n!=Main.root) {
            n.toFront();
            n=n.getParent();
        }*/
    }
    public static void makeMovable(Node n){
        n.setOnMousePressed(e->{
            Main.orgSceneX=e.getSceneX();
            Main.orgSceneY=e.getSceneY();
            Main.orgTranslateX=((Node) e.getSource()).getTranslateX();
            Main.orgTranslateY=((Node) e.getSource()).getTranslateY();
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
            setToFront((Node)e.getSource());
        });
        n.setOnMouseDragged(e->{
            double offsetX=e.getSceneX()- Main.orgSceneX;
            double offsetY=e.getSceneY()- Main.orgSceneY;
            double newTranslateX= Main.orgTranslateX+offsetX;
            double newTranslateY= Main.orgTranslateY+offsetY;
            ((Node) e.getSource()).setTranslateX(newTranslateX);
            ((Node) e.getSource()).setTranslateY(newTranslateY);
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
        });
        n.setOnMouseReleased(e->{
            ((Node)e.getSource()).setCursor(Cursor.CLOSED_HAND);
        });
        n.setOnMouseEntered(e->{
            ((Node)e.getSource()).setCursor(Cursor.CLOSED_HAND);
        });
        n.setOnMouseExited(e->{
            ((Node)e.getSource()).setCursor(Cursor.DEFAULT);
        });
    }

    public static StackPane getNewStackPane(){
        StackPane stackPane=new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setPadding(new Insets(10));
        stackPane.setStyle("-fx-background-color: green");
        stackPane.minHeight(Region.USE_COMPUTED_SIZE);
        stackPane.minWidth(Region.USE_COMPUTED_SIZE);
        stackPane.prefHeight(Region.USE_COMPUTED_SIZE);
        stackPane.prefWidth(Region.USE_COMPUTED_SIZE);
        stackPane.maxHeight(Region.USE_COMPUTED_SIZE);
        stackPane.maxWidth(Region.USE_COMPUTED_SIZE);
        MyUtil.makeMovable(stackPane);
        return stackPane;
    }

}
