package savedCourse;

import java.io.Serializable;

/**
 * Created by ghost on 17/6/17.
 */
public class MediaComponent extends PageComponent implements Serializable {
    byte[] videoBytes;
    String fileName;
    String fileType;
}
