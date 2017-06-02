package sample;


import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Created by ghost on 31/5/17.
 */
public class MyTextField extends StackPane{

    private StackPane container;
    private TextField textField;

    public StackPane getContainer() {
        return container;
    }

    public void setContainer(StackPane container) {
        this.container = container;
    }

    public MyTextField(StackPane container) {

        this.container = container;
    }

    public MyTextField(){
        this.textField=new TextField();
        textField.setOnMousePressed(em->{
            MyUtil.setToFront((Node)em.getSource());
        });
        textField.setOnKeyTyped(ek->{
            MyUtil.setToFront((Node)ek.getSource());
        });
    }

}
