package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Callback;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Created by ghost on 9/6/17.
 */
public class ExamSceneController implements Initializable{

    private  ArrayList<VBox> questionList;
    private static Integer score;

    public static List<MCQ> questions;
    public static Scene sourceScene;
    @FXML
    public AnchorPane mainContainer;
    @FXML
    public Pagination questionsContainer;
    @FXML
    public Button finish;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        questionsContainer.setPageCount(questions.size());
        questionsContainer.setMaxPageIndicatorCount(questions.size());
        questionList=new ArrayList<>();
        for (MCQ each:questions){
            VBox vBox=new VBox();
            vBox.setSpacing(20);
            vBox.getChildren().add(new Text(each.getQuestion()));
            ToggleGroup group =new ToggleGroup();
            for(String option :each.getOptions()){
                RadioButton r=new RadioButton(option);
                r.setToggleGroup(group);
                r.setId(option);
                Region re=new Region();
                re.setPrefSize(50,0);
                re.setMinSize(50,0);
                re.setMaxSize(50,0);
                vBox.getChildren().add(new HBox(re,r));
            }
            questionList.add(vBox);
        }
        for(MCQ each:questions){
            questionsContainer.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public VBox call(Integer pageIndex) {
                    return questionList.get(pageIndex);
                }
            });
        }
    }

    public void finishExam(){
        Main.window.setScene(this.sourceScene);
        int s=questions.size();
        this.score=0;
        for(int i=0 ;i<s;i++){
            VBox vbox=questionList.get(i);
            Integer correctOptionIndex=questions.get(i).getCorrectOptions();
            String correctOption=questions.get(i).getOptions().get(correctOptionIndex);
            try{
                RadioButton r=(RadioButton)searchById(vbox,correctOption);
                if(r.isSelected())
                    score++;

            }catch (Exception e){
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        final Popup popup =new Popup();
        popup.setX((Main.window.getX()+Main.window.getWidth()/2));
        popup.setY(Main.window.getY()+Main.window.getScene().getY());
        popup.setHeight(300);
        popup.setWidth(400);
        Button button=new Button("OK");
        button.setOnAction(e->{
            popup.hide();
        });
        button.setAlignment(Pos.CENTER_RIGHT);
        VBox vBox=new VBox(new Label("Your Score is: "+String.valueOf(score)+"/"+String.valueOf(questions.size())),new HBox(button));
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        vBox.setStyle("-fx-background-color: lightgray;-fx-border-color: black;-fx-border-width: 1px;");
        popup.getContent().addAll(vBox);
        popup.show(Main.window);
    }

    private Node searchById(Node pane,String id){
        if(pane.getId()!=null && pane.getId().equals(id)){
            return pane;
        }
        try{
            for (Node each :((Pane)pane).getChildren()){
                Node temp=searchById(each,id);
                if(temp!=null){
                    return temp;
                }
            }
        }catch (Exception e){
//            System.out.println(pane.toString()+"not a Parent\n"+e.toString());
        }
        return null;
    }
}
