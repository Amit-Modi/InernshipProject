package sample;

import courseBuilder.Store;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


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
            Store.orgSceneX=e.getSceneX();
            Store.orgSceneY=e.getSceneY();
            Store.orgTranslateX=((Node) e.getSource()).getTranslateX();
            Store.orgTranslateY=((Node) e.getSource()).getTranslateY();
            ((Node)e.getSource()).setCursor(Cursor.MOVE);
            setToFront((Node)e.getSource());
            setSelected(n);
        });
        n.setOnMouseDragged(e->{
            double offsetX=e.getSceneX()- Store.orgSceneX;
            double offsetY=e.getSceneY()- Store.orgSceneY;
            double newTranslateX= Store.orgTranslateX+offsetX;
            double newTranslateY= Store.orgTranslateY+offsetY;
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

    public static TextArea getNewTextBox(double width,double height){
        TextArea t=new TextArea();
        t.setPromptText("Enter something");
        t.setOnMouseClicked(em->{
            MyUtil.setToFront((Node)em.getSource());
        });
        t.setOnKeyTyped(ek->{
            MyUtil.setToFront((Node)ek.getSource());
        });
        t.setPrefSize(width,height);
        return t;
    }

    public static Region getNewRegion(){
        Region r=new Region();

        r.setPrefSize(5,5);
        r.setMaxSize(r.getPrefWidth(),r.getPrefHeight());
        r.setMinSize(r.getPrefWidth(),r.getPrefHeight());
        r.setStyle("-fx-background-color: aqua");
        return r;
    }

    public static void setSelected(Node n){
        Store.selectedNode=n;

    }
}
