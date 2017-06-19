package savedCourse;

import course.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import pageEditing.Element;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by ghost on 17/6/17.
 */
public class Converter {
    public static course.Course convertToCourse(Course savedCourse) {
        course.Course course=new course.Course();
        course.courseName=savedCourse.courseName;
        course.chapters=new ArrayList<>();
        for(Chapter each :savedCourse.chapters){
            course.chapters.add(convertToChapter(each));
        }
        return course;
    }

    private static course.Chapter convertToChapter(Chapter savedChapter) {
        course.Chapter chapter=new course.Chapter();
        chapter.chapterName=savedChapter.chapterName;
        chapter.topics=new ArrayList<>();
        for(Topic each : savedChapter.topics){
            course.Topic topic=convertToTopic(each);
            topic.parent=chapter;
            chapter.topics.add(topic);
        }
        return chapter;
    }

    private static course.Topic convertToTopic(Topic savedTopic) {
        course.Topic topic=new course.Topic();
        topic.topicName=savedTopic.topicName;
        topic.pages=new ArrayList<>();
        for(Page each : savedTopic.pages){
            topic.pages.add(convertToAnchorPane(each));
        }
        return topic;
    }

    private static AnchorPane convertToAnchorPane(Page savedPage) {
        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setMaxSize(savedPage.width,savedPage.height);
        anchorPane.setMinSize(savedPage.width,savedPage.height);
        anchorPane.setStyle(savedPage.style);
        for(PageComponent each : savedPage.pageComponents){
            Node child=convertToNode(each);
            if(child!=null)
                anchorPane.getChildren().add(child);
        }
        return anchorPane;
    }

    private static Node convertToNode(PageComponent each) {
        if(each.getClass()==TextComponent.class){
            TextComponent textComponent=(TextComponent) each;
            StackPane textBox= Element.getTextBox();
            ((TextArea)textBox.getChildren().get(0)).setText(textComponent.text);
            ((TextArea)textBox.getChildren().get(0)).setStyle(textComponent.style);
            ((TextArea)textBox.getChildren().get(0)).setMaxSize(textComponent.width,textComponent.height);
            ((TextArea)textBox.getChildren().get(0)).setMinSize(textComponent.width,textComponent.height);
            AnchorPane.setLeftAnchor(textBox,textComponent.x);
            AnchorPane.setTopAnchor(textBox,textComponent.y);
            return textBox;
        }
        else if(each.getClass()== TitleComponent.class){
            TitleComponent titleComponent=(TitleComponent)each;
            StackPane titleBox= Element.getTitleBox();
            ((TextField)titleBox.getChildren().get(0)).setText(titleComponent.text);
            ((TextField)titleBox.getChildren().get(0)).setStyle(titleComponent.style);
            ((TextField)titleBox.getChildren().get(0)).setMaxSize(titleComponent.width,titleComponent.height);
            ((TextField)titleBox.getChildren().get(0)).setMinSize(titleComponent.width,titleComponent.height);
            AnchorPane.setLeftAnchor(titleBox,titleComponent.x);
            AnchorPane.setTopAnchor(titleBox,titleComponent.y);
            return titleBox;
        }
        else if(each.getClass()==ImageComponent.class){
            ImageComponent imageComponent= (ImageComponent) each;

            ByteArrayInputStream s=new ByteArrayInputStream(imageComponent.imageBytes);
            try {
                BufferedImage bufferedImage = ImageIO.read(s);
                Image image=SwingFXUtils.toFXImage(bufferedImage,null);
                StackPane imageBox= Element.getImageBox(image);
                ((ImageView)imageBox.getChildren().get(0)).setFitWidth(imageComponent.width);
                ((ImageView)imageBox.getChildren().get(0)).setFitHeight(imageComponent.height);
                AnchorPane.setLeftAnchor(imageBox,imageComponent.x);
                AnchorPane.setTopAnchor(imageBox,imageComponent.y);
                return imageBox;

            }catch (Exception e){
                System.out.println("Error occurred while reading an image\n"+e);
            }
        }
        else if(each.getClass()==VideoComponent.class){
            VideoComponent videoComponent= (VideoComponent) each;

            ByteArrayInputStream s=new ByteArrayInputStream(videoComponent.videoBytes);
            try {
                throw new Exception("Function not written");

            }catch (Exception e){
                System.out.println("Error occurred while reading an video\n"+e);
            }
        }
        return null;
    }

    public static Course convertToSavableCourse(course.Course course) {
        Course savableCourse=new Course();
        savableCourse.courseName=course.courseName;
        savableCourse.chapters=new ArrayList<>();
        for(course.Chapter each : course.chapters){
            savableCourse.chapters.add(convertToSavableChapter(each));
        }
        return savableCourse;
    }

    private static Chapter convertToSavableChapter(course.Chapter chapter) {
        Chapter savableChapter=new Chapter();
        savableChapter.chapterName=chapter.chapterName;
        savableChapter.topics=new ArrayList<>();
        for(course.Topic each : chapter.topics){
            savableChapter.topics.add(convertToSavableTopic(each));
        }
        return savableChapter;
    }

    private static Topic convertToSavableTopic(course.Topic topic) {
        Topic savableTopic=new Topic();
        savableTopic.topicName=topic.topicName;
        savableTopic.pages=new ArrayList<>();
        for(AnchorPane each : topic.pages){
            savableTopic.pages.add(convertToSavablePage(each));
        }
        return savableTopic;
    }

    private static Page convertToSavablePage(AnchorPane anchorPane) {
        Page savablePage=new Page();
        savablePage.height=anchorPane.getHeight();
        savablePage.width=anchorPane.getWidth();
        savablePage.style=anchorPane.getStyle();
        savablePage.pageComponents=new ArrayList<>();
        for(Node each : anchorPane.getChildren()){
            PageComponent pageComponent=convertToSavablePageComponent(each);
            if(pageComponent!=null)
                savablePage.pageComponents.add(pageComponent);
        }
        return savablePage;
    }

    private static PageComponent convertToSavablePageComponent(Node each) {
        Node component=((StackPane)each).getChildren().get(0);
        if(component.getClass()==TextArea.class){
            TextArea textArea= (TextArea) component;
            TextComponent textComponent=new TextComponent();
            textComponent.style=textArea.getStyle();
            textComponent.text=textArea.getText();
            textComponent.width=textArea.getWidth();
            textComponent.height=textArea.getHeight();
            textComponent.x=AnchorPane.getLeftAnchor(each);
            textComponent.y=AnchorPane.getTopAnchor(each);
            return textComponent;
        }
        else if(component.getClass()==TextField.class){
            TextField textField= (TextField) component;
            TitleComponent titleComponent=new TitleComponent();
            titleComponent.style=textField.getStyle();
            titleComponent.text=textField.getText();
            titleComponent.width=textField.getWidth();
            titleComponent.height=textField.getHeight();
            titleComponent.x=AnchorPane.getLeftAnchor(each);
            titleComponent.y=AnchorPane.getTopAnchor(each);
            return titleComponent;
        }
        else if(component.getClass()== ImageView.class){
            Image img=((ImageView)component).getImage();
            ImageComponent imageComponent=new ImageComponent();
            BufferedImage bufferedImage= SwingFXUtils.fromFXImage(img,null);
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            try {
                ImageIO.write(bufferedImage, "png", s);
                imageComponent.imageBytes = s.toByteArray();
                s.close();
                imageComponent.width=((ImageView)component).getFitWidth();
                imageComponent.height=((ImageView)component).getFitHeight();
                imageComponent.x=AnchorPane.getLeftAnchor(each);
                imageComponent.y=AnchorPane.getTopAnchor(each);
                return imageComponent;
            }catch (Exception e){
                System.out.println("Error occurred while saving :"+img+"\n"+e);
            }
        }
        else if(component.getClass()== MediaView.class){
            try {
                throw new Exception("Function not written");
            }catch (Exception e){
                System.out.println("Error occurred while saving video:"+"\n"+e);
            }
        }
        return null;
    }
}
