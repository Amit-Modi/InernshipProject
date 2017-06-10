package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ghost on 9/6/17.
 */

public class temp {

    /*public void storeObject(ArrayList<MCQ> objects){
        OutputStream ops=null;
        ObjectOutputStream objops=null;
        try{
            ops=new FileOutputStream("Myfile.txt");
            objops=new ObjectOutputStream(ops);
            objops.writeObject(objects);
            objops.flush();
        } catch (Exception e){
            System.out.println("ops objops");
            e.printStackTrace();
        } finally {
            try {
                if (objops != null)
                    objops.close();
            } catch ( Exception e){
                System.out.println("objops close");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<MCQ> getObject(){
        ArrayList<MCQ> objects=null;
        InputStream ips=null;
        ObjectInputStream obips=null;
        try{
            ips=new FileInputStream("Myfile.txt");
            obips=new ObjectInputStream(ips);
            objects=(ArrayList<MCQ>) obips.readObject();

        } catch (Exception e){
            e.printStackTrace();
        }
        return objects;
    }*/
    public static void main(String[]args) throws Exception{
        List<MCQ> questions=new ArrayList<>();
        MCQ q1=new MCQ();
        q1.setQuestion("Is this a question?");
        q1.getOptions().add("Yes");
        q1.getOptions().add("No");
        q1.setCorrectOptions(0);
        questions.add(q1);
        q1=new MCQ();
        q1.setQuestion("Is this also a question?");
        q1.getOptions().add("True");
        q1.getOptions().add("False");
        q1.setCorrectOptions(0);
        questions.add(q1);

        FileOutputStream fos =new FileOutputStream("Question.qtn");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(questions);
        oos.close();

        FileInputStream fis = new FileInputStream("Question.qtn");
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<MCQ> mcqList=(List<MCQ>)ois.readObject();
        ois.close();
        System.out.println(mcqList.get(0).getQuestion());
    }

}
