package course;

import javafx.scene.layout.Pane;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ghost on 13/6/17.
 */
public class Topic implements Serializable{
    public String topicName;
    public ArrayList<Pane> pages;
}
