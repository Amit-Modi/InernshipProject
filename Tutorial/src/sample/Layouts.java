package sample;

import courseBuilder.Element;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import static courseBuilder.Element.getTextBox;
import static courseBuilder.Element.getTitleBox;


/**
 * Created by ghost on 2/6/17.
 */
public class Layouts {


    public static AnchorPane getNormalLayout(){

        StackPane title = getTitleBox();
        StackPane textArea = getTextBox();

        AnchorPane.setTopAnchor(title,50.0);
        AnchorPane.setLeftAnchor(title,50.0);
        AnchorPane.setRightAnchor(title,50.0);
        AnchorPane.setTopAnchor(textArea,200.0);
        AnchorPane.setLeftAnchor(textArea,50.0);
        AnchorPane.setRightAnchor(textArea,50.0);

        AnchorPane newPage=new AnchorPane();
        newPage.setPrefSize(1024,768);
        newPage.setStyle("-fx-background-color: cyan");

        newPage.getChildren().addAll(title,textArea);

        return newPage;
    }
}
