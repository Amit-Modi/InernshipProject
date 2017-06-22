package testpackage;


import javafx.scene.text.Font;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by ghost on 17/6/17.
 */
public class temp {

    public static void main(String[]A) throws Exception{

        Font font=new Font("Verdana",14);

        String style=font.toString();
        FileOutputStream fos =new FileOutputStream("font.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(style);
        oos.close();
        System.out.println(style);
    }
}
