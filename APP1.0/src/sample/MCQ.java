package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ghost on 9/6/17.
 */


public class MCQ implements Serializable{
    private String question;
    private TempClass tempClass;
    private ArrayList<String> options;
    //private Set<Integer> correctOptions;
    private Integer correctOptions;
    private String reason;

    public MCQ(){
        options=new ArrayList<>();
        correctOptions=0;
        /*correctOptions =new HashSet<Integer>(){
            public boolean add(String string) {
                Integer integer=options.indexOf(string);
                if(integer>=-1)
                    return super.add(integer);
                return false;
            }
        };*/
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    /*public Set<Integer> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(Set<Integer> correctOptions) {
        this.correctOptions = correctOptions;
    }*/

    public Integer getCorrectOptions(){
        return this.correctOptions;
    }

    public void setCorrectOptions(Integer correctOptions){
        this.correctOptions=correctOptions;
    }

    /*public Boolean checkResult(ArrayList<String> answer){
        if(answer.size()!=correctOptions.size())
            return false;
        for(Integer idx : this.correctOptions){
            if(!answer.contains(options.get(idx))){
                return false;
            }
        }
        return true;
    }*/
    public Boolean checkResult(String answer){
        if(answer.equals(options.get(correctOptions))){
            return true;
        }
        return false;
    }
}
