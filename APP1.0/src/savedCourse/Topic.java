package savedCourse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ghost on 17/6/17.
 */
public class Topic implements Serializable {
    public String topicName;
    public ArrayList<Page> pages;
}
