package testpackage;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.MCQ;
import savedCourse.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghost on 17/6/17.
 */
public class temp {

    public static void main(String[]A) throws Exception{
        Course course=new Course();
        course.courseName="My Courese";
        course.chapters=new ArrayList<>();

        Chapter chapter=new Chapter();
        chapter.chapterName="C1";
        chapter.topics=new ArrayList<>();

        Topic topic=new Topic();
        topic.topicName="t1";
        topic.pages=new ArrayList<>();

        Page page=new Page();
        page.height=1024.0;
        page.width=768.0;
        page.style="-fx-background-color: red";
        page.pageComponents=new ArrayList<>();

        TextComponent textComponent=new TextComponent();
        textComponent.height=300.0;
        textComponent.width=400.0;
        textComponent.x=100.0;
        textComponent.y=100.0;
        textComponent.text="hello tihis hsis";
        textComponent.style="";

        TextComponent textComponent2=new TextComponent();
        textComponent2.height=300.0;
        textComponent2.width=400.0;
        textComponent2.x=150.0;
        textComponent2.y=50.0;
        textComponent2.text="this is 2";
        textComponent2.style="";

        page.pageComponents.add(textComponent);
        page.pageComponents.add(textComponent2);
        topic.pages.add(page);
        chapter.topics.add(topic);
        course.chapters.add(chapter);

        FileOutputStream fos =new FileOutputStream("CourseA.course");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(course);
        oos.close();

    }
}
