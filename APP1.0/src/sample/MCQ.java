package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ghost on 9/6/17.
 */


public class MCQ implements Serializable{
    private String question;
    private ArrayList<String> options;
    private Set<Integer> correctOptions;
    private String reason;

    public MCQ(){
        options=new ArrayList<>();
        correctOptions =new HashSet<Integer>(){
            public boolean add(String string) {
                Integer integer=options.indexOf(string);
                if(integer>=-1)
                    return super.add(integer);
                return false;
            }
        };
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

    public Set<Integer> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(Set<Integer> correctOptions) {
        this.correctOptions = correctOptions;
    }

    public Boolean checkResult(ArrayList<String> answer){
        if(answer.size()!=correctOptions.size())
            return false;
        for(Integer idx : this.correctOptions){
            if(!answer.contains(options.get(idx))){
                return false;
            }
        }
        return true;
    }
}
