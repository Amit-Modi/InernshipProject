package sample;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;


import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Created by ghost on 9/6/17.
 */
public class ExamSceneController implements Initializable{

    public static List<MCQ> questions;
    public static Scene sourceScene;
    public static Integer score;

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
        for(MCQ each:questions){
            questionsContainer.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public VBox call(Integer pageIndex) {
                    VBox vBox=new VBox();
                    vBox.setSpacing(20);
                    vBox.getChildren().add(new Text(each.getQuestion()));
                    ToggleGroup group =new ToggleGroup();
                    for(String option :each.getOptions()){
                        RadioButton r=new RadioButton(option);
                        r.setToggleGroup(group);
                        Region re=new Region();
                        re.setPrefSize(50,0);
                        re.setMinSize(50,0);
                        re.setMaxSize(50,0);
                        vBox.getChildren().add(new HBox(re,r));
                    }
                    return vBox;
                }
            });
        }
    }

    public void finishExam(){
        Main.window.setScene(this.sourceScene);
    }
}
