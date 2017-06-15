package course;

import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ghost on 13/6/17.
 */


public class Course implements Serializable{

    public String courseName;
    public ArrayList<Chapter> chapters;
}
