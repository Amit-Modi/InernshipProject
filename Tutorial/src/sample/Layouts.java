package sample;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


/**
 * Created by ghost on 2/6/17.
 */
public class Layouts {

    public static StackPane getTitleBox(){
        TextArea title=new TextArea();
        title.setPromptText("Enter Title");

        StackPane stackPane =MyUtil.getNewStackPane();
        stackPane.setPrefSize(800.0,100.0);
        stackPane.getChildren().add(title);
        return stackPane;
    }

    public static StackPane getTextBox(){
        TextArea textArea=new TextArea();
        textArea.setPromptText("Enter Title");

        StackPane stackPane =MyUtil.getNewStackPane();
        stackPane.setPrefSize(800.0,500.0);
        stackPane.getChildren().add(textArea);
        return stackPane;
    }

    public static AnchorPane getNormalLayout(){

        StackPane title = getTitleBox();
        StackPane textArea = getTextBox();

        AnchorPane.setTopAnchor(title,50.0);
        AnchorPane.setTopAnchor(textArea,200.0);

        AnchorPane newPage=new AnchorPane();
        newPage.setPrefSize(1024,768);

        newPage.getChildren().addAll(title,textArea);

        return newPage;
    }
}
