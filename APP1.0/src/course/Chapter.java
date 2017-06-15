package course;

import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ghost on 13/6/17.
 */
public class Chapter implements Serializable{
    public String chapterName;
    public ArrayList<Topic> topics;
}