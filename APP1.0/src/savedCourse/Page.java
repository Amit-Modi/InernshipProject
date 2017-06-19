package savedCourse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ghost on 17/6/17.
 */
public class Page implements Serializable {
    public double height;
    public double width;
    public String style;
    public ArrayList<PageComponent> pageComponents;
}
