package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

/**
 * Created by ghost on 22/6/17.
 */
public class Layout extends AnchorPane {

    public static final int EMPTY = 0;
    public static final int BASIC = 1;
    public static final int TITLE_PAGE = 2;

    public Layout(){
        super();
        setMaxSize(1024,768);
        setMinSize(1024,768);
        setStyle("-fx-background-color: white");
    }

    public Layout(int l){
        this();
        if(l==1){
            this.getChildren().addAll(
                    Element.getTitleBox(new Rectangle(100,100,824,100)),
                    Element.getTextBox(new Rectangle(100,300,824,368))
            );
        }
        else if(l==2){
            this.getChildren().addAll(
                    Element.getTitleBox(new Rectangle(100,334,824,100))
            );
        }
    }
}