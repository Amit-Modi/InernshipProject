package savedCourse;

import javafx.scene.control.TextArea;

import java.io.Serializable;

/**
 * Created by ghost on 17/6/17.
 */
public class TextComponent extends PageComponent implements Serializable {

    public String text;
    public String family;
    public double size;
    public int red;
    public int green;
    public int blue;
    public int opacity;

}
