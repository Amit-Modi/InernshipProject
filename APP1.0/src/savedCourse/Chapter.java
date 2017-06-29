package savedCourse;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ghost on 17/6/17.
 */
public class Chapter implements Serializable{
    public String chapterName;
    public ArrayList<Topic> topics;
    public byte[] chapterVideo;
    String fileName;
    String fileType;
}
/*
    private static course.Chapter convertToChapter(Chapter savedChapter) {
        course.Chapter chapter=new course.Chapter();
        chapter.chapterName=savedChapter.chapterName;
        chapter.topics=new ArrayList<>();

        for(Topic each : savedChapter.topics){
            course.Topic topic=convertToTopic(each);
            topic.parent=chapter;
            chapter.topics.add(topic);

        }

        ByteArrayInputStream s=new ByteArrayInputStream(savedChapter.chapterVideo);
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
            return chapter;
        }catch(IOException e){
            System.out.println(e.toString());
            return null;
        }
    }
    private static Chapter convertToSavableChapter(course.Chapter chapter) {
        Chapter savableChapter=new Chapter();
        savableChapter.chapterName=chapter.chapterName;
        savableChapter.topics=new ArrayList<>();
        for(course.Topic each : chapter.topics){
            savableChapter.topics.add(convertToSavableTopic(each));
        }

        String filePath=chapter.media.getSource();

        File file=new File(filePath.split(":")[1]);
        String[] strings=(file.getName().split("\\."));
        savableChapter.fileName=strings[0];
        savableChapter.fileType=strings[1];
        savableChapter.chapterVideo=new byte[(int)file.length()];
        try{
            FileInputStream fis=new FileInputStream(file);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[]buf = new byte[1024];
            int n;
            while(-1!=(n=fis.read(buf)))
                baos.write(buf,0,n);
            savableChapter.chapterVideo=baos.toByteArray();

        }catch(IOException e) {
            System.out.println(e.toString());
        }
        return savableChapter;
    }
    */

