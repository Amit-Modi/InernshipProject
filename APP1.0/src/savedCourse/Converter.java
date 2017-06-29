package savedCourse;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import sample.Element;
import sample.Layout;
import sample.PopUp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

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
        if(savedChapter.chapterVideo!=null) {
            ByteArrayInputStream s = new ByteArrayInputStream(savedChapter.chapterVideo);
            try {
                File file = File.createTempFile(savedChapter.fileName, savedChapter.fileType);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int n;
                while ((n = s.read(buf)) > 0) {
                    fos.write(buf, 0, n);
                }
                s.close();
                fos.close();
                chapter.media = new javafx.scene.media.Media(file.toURI().toString());
                file.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
                PopUp.display("Error!", e.toString());
            }
        }
        else {
            chapter.media=null;
        }
        return chapter;
    }

    public static course.Topic convertToTopic(Topic savedTopic) {
        course.Topic topic=new course.Topic();
        topic.topicName=savedTopic.topicName;
        topic.pages=new ArrayList<>();
        for(Page each : savedTopic.pages){
            topic.pages.add(convertToAnchorPane(each));
        }
        return topic;
    }

    private static AnchorPane convertToAnchorPane(Page savedPage) {
        AnchorPane anchorPane= new Layout();
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
            Rectangle rec=new Rectangle();
            rec.setX(textComponent.x);
            rec.setY(textComponent.y);
            rec.setWidth(textComponent.width);
            rec.setHeight(textComponent.height);
            StackPane textBox= Element.getTextBox(rec);
            ((TextArea)textBox.getChildren().get(0)).setText(textComponent.text);
            ((TextArea)textBox.getChildren().get(0)).setStyle(textComponent.style);

            return textBox;
        }
        else if(each.getClass()== TitleComponent.class){
            TitleComponent titleComponent=(TitleComponent)each;
            Rectangle rec=new Rectangle();
            rec.setX(titleComponent.x);
            rec.setY(titleComponent.y);
            rec.setWidth(titleComponent.width);
            rec.setHeight(titleComponent.height);
            StackPane titleBox= Element.getTitleBox(rec);
            ((TextField)titleBox.getChildren().get(0)).setText(titleComponent.text);
            ((TextField)titleBox.getChildren().get(0)).setStyle(titleComponent.style);
            return titleBox;
        }
        else if(each.getClass()==ImageComponent.class){
            ImageComponent imageComponent= (ImageComponent) each;

            ByteArrayInputStream s=new ByteArrayInputStream(imageComponent.imageBytes);
            try {
                BufferedImage bufferedImage = ImageIO.read(s);
                Image image=SwingFXUtils.toFXImage(bufferedImage,null);
                Rectangle rec=new Rectangle();
                rec.setX(imageComponent.x);
                rec.setY(imageComponent.y);
                rec.setWidth(imageComponent.width);
                rec.setHeight(imageComponent.height);
                StackPane imageBox= Element.getImageBox(image,rec);
                return imageBox;

            }catch (Exception e){
                System.out.println("Error occurred while reading an image\n"+e);
            }
        }
        else if(each.getClass()== MediaComponent.class){
            MediaComponent mediaComponent = (MediaComponent) each;

            ByteArrayInputStream s=new ByteArrayInputStream(mediaComponent.videoBytes);
            try {
                File file=File.createTempFile(mediaComponent.fileName,mediaComponent.fileType);
                FileOutputStream fos=new FileOutputStream(file);
                byte[] buf=new byte[1024];
                int n;
                while((n=s.read(buf))>0){
                    fos.write(buf,0,n);
                }
                s.close();
                fos.close();
                Media media=new Media(file.toURI().toString());
                Rectangle rec=new Rectangle();
                rec.setX(mediaComponent.x);
                rec.setY(mediaComponent.y);
                rec.setWidth(mediaComponent.width);
                rec.setHeight(mediaComponent.height);
                StackPane mediaBox=Element.getVideoBox(media,rec);
                return mediaBox;
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
        if(chapter.media!=null) {
            String filePath = chapter.media.getSource();

            File file = new File(filePath.split(":")[1]);
            String[] strings = (file.getName().split("\\."));
            savableChapter.fileName = strings[0];
            savableChapter.fileType = strings[1];
            savableChapter.chapterVideo = new byte[(int) file.length()];
            try {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = fis.read(buf)))
                    baos.write(buf, 0, n);
                savableChapter.chapterVideo = baos.toByteArray();

            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
        else {
            savableChapter.chapterVideo=null;
            savableChapter.fileName=null;
            savableChapter.fileType=null;
        }
        return savableChapter;
    }

    public static Topic convertToSavableTopic(course.Topic topic) {
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
                System.out.println("Error occurred while saving :"+img+"\n");
                e.printStackTrace();
            }
        }
        else if(component.getClass()== MediaView.class){
            try {
                MediaView mediaView= (MediaView) component;
                MediaComponent mediaComponent=new MediaComponent();
                System.out.println("Media Source "+mediaView.getMediaPlayer().getMedia().getSource());
                String filePath=mediaView.getMediaPlayer().getMedia().getSource();

                File file=new File(filePath.split(":")[1]);
                String[] strings=(file.getName().split("\\."));
                mediaComponent.fileName=strings[0];
                mediaComponent.fileType=strings[1];
                mediaComponent.videoBytes=new byte[(int)file.length()];

                FileInputStream fis=new FileInputStream(file);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[]buf = new byte[1024];
                int n;
                while(-1!=(n=fis.read(buf)))
                    baos.write(buf,0,n);
                mediaComponent.videoBytes=baos.toByteArray();

                mediaComponent.width=((MediaView)component).getFitWidth();
                mediaComponent.height=((MediaView)component).getFitHeight();
                mediaComponent.x=AnchorPane.getLeftAnchor(each);
                mediaComponent.y=AnchorPane.getTopAnchor(each);
                return mediaComponent;
            }catch (Exception e){
                System.out.println("Error occurred while saving video:"+"\n");
                e.printStackTrace();
            }
        }
        return null;
    }
}
