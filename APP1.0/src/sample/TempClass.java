package sample;

import course.Chapter;
import course.Topic;
import javafx.scene.layout.AnchorPane;

/**
 * Created by ghost on 14/6/17.
 */
public class TempClass {
    public static void main(String[]A){
        Chapter chapter=new Chapter();
        chapter.chapterName="1";
        Topic topic=new Topic();
        topic.parent=chapter;
        chapter.chapterName="2";
        System.out.println(chapter.chapterName);
        System.out.println(topic.parent.chapterName);
    }

}
