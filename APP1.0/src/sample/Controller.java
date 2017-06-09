package sample;

import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.util.Pair;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Controller implements Initializable{

    private  ObjectProperty<ImageView> image=new ObjectPropertyBase<ImageView>() {
        @Override
        public Object getBean() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    };
    private MenuButton b1;
    private LinkedHashMap<MenuItem,Pair<Integer,Pair<Integer,String>>> mapChapter;
    private LinkedHashMap<Integer,Pair<String,Pair<String,String>>> chapterno;
    private Document document;
    private Duration duration;
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> scheduledFuture;
    private MenuItem temp;
    private EventHandler<?> exitFullScreenHandler;
    private String examFilePath;
    private static int checkChapNo;

    public ImageView buttonImage1,buttonImage2;
    public Image image1,image2;

    @FXML
    public MenuItem c1_1;
    @FXML
    public MenuItem c1_2;
    @FXML
    public MenuItem c1_3;
    @FXML
    public MenuItem c1_4;
    @FXML
    public MenuItem c1_5;
    @FXML
    public MenuItem c2_1;
    @FXML
    public MenuItem c2_2;
    @FXML
    public MenuItem c2_3;
    @FXML
    public MenuItem c2_4;
    @FXML
    public MenuItem c2_5;
    @FXML
    public MenuItem c3_5;
    @FXML
    public MenuItem c3_4;
    @FXML
    public MenuItem c3_3;
    @FXML
    public MenuItem c3_2;
    @FXML
    public MenuItem c3_1;

    @FXML
    public MenuItem c4_1;
    @FXML
    public MenuItem c4_2;
    @FXML
    public MenuItem c4_3;
    @FXML
    public MenuItem c4_4;
    @FXML
    public MenuItem c4_5;

    @FXML
    public MenuItem c5_1;
    @FXML
    public MenuItem c5_2;

    @FXML
    public MenuItem c6_1;
    @FXML
    public MenuItem c6_2;
    @FXML
    public MenuItem c6_3;

    @FXML
    public MenuItem c7_1;
    @FXML
    public MenuItem c7_2;
    @FXML
    public MenuItem c7_3;
    @FXML
    public MenuItem c7_4;
    @FXML
    public MenuItem c7_5;
    @FXML
    public MenuItem c7_6;

    @FXML
    public MenuItem c8_1;
    @FXML
    public MenuItem c8_2;
    @FXML
    public MenuItem c8_3;
    @FXML
    public MenuItem c8_4;
    @FXML
    public MenuItem c8_5;
    @FXML
    public MenuItem c8_6;

    @FXML
    public MenuItem c9_1;
    @FXML
    public MenuItem c9_2;
    @FXML
    public MenuItem c9_3;
    @FXML
    public MenuItem c9_4;
    @FXML
    public MenuItem c9_5;
    @FXML
    public MenuItem c9_6;

    @FXML
    public MenuItem c10_1;
    @FXML
    public MenuItem c10_2;
    @FXML
    public MenuItem c10_3;
    @FXML
    public MenuItem c10_5;
    @FXML
    public MenuItem c10_6;
    @FXML
    public MenuItem c10_7;

    @FXML
    public MenuItem c11_1;
    @FXML
    public MenuItem c11_2;
    @FXML
    public MenuItem c11_3;

    @FXML
    public MenuItem c12_1;
    @FXML
    public MenuItem c12_2;
    @FXML
    public MenuItem c12_3;
    @FXML
    public MenuItem c12_4;
    @FXML
    public MenuItem c12_5;
    @FXML
    public MenuItem c12_6;


    @FXML
    public MenuItem c13_1;
    @FXML
    public MenuItem c13_2;
    @FXML
    public MenuItem c13_3;

    @FXML
    public MenuItem c14_1;
    @FXML
    public MenuItem c14_2;
    @FXML
    public MenuItem c14_3;
    @FXML
    public MenuItem c14_4;
    @FXML
    public MenuItem c14_5;

    @FXML
    public MenuItem c15_1;
    @FXML
    public MenuItem c15_2;










    @FXML
    HBox topBar;
    @FXML
    ScrollPane playarea;
    @FXML
    ScrollPane textbox;
    @FXML
    TextArea notes;
    @FXML
    MediaView mediaView;
    @FXML
    HBox mediaContainer;
    @FXML
    HBox lectureContainer;
    @FXML
    AnchorPane scene;
    @FXML
    ScrollPane scrollLeftMenu;
    @FXML
    BorderPane totalContainer;
    @FXML
    VBox playAreaContainer;
    @FXML
    VBox playerContainer;
    @FXML
    HBox buttonContainer;
    @FXML
    HBox progressContainer;
    @FXML
    ProgressBar mediaProgressBar;
    @FXML
    Button playButton;
    @FXML
    Slider timeSlider;
    @FXML
    Label playTime;
    @FXML
    MenuButton volumeButton;
    @FXML
    Button fullScreenButton;
    @FXML
    Label totalPages;
    @FXML
    TextField currentPage;
    @FXML
    Label topicLabel;

    @FXML
    Button nextTopic;
    @FXML
    Button prevTopic;

    @FXML
    Button previousButton;

    @FXML
    Button nextButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkChapNo=0;
        try {
            mapChapter = new LinkedHashMap<>();
            mapChapter.put(c1_1, new Pair<>(0,new Pair<>(1,"banana.mp4")));
            mapChapter.put(c1_2, new Pair<>(1,new Pair<>(1,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c1_3, new Pair<>(3,new Pair<>(1,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c1_4, new Pair<>(5,new Pair<>(1,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c1_5, new Pair<>(8,new Pair<>(1,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c2_1, new Pair<>(0,new Pair<>(2,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c2_2, new Pair<>(1,new Pair<>(2,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c2_3, new Pair<>(5,new Pair<>(2,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c2_4, new Pair<>(10,new Pair<>(2,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c2_5, new Pair<>(11,new Pair<>(2,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c3_1, new Pair<>(0,new Pair<>(3,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c3_2, new Pair<>(2,new Pair<>(3,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c3_3, new Pair<>(4,new Pair<>(3,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c3_4, new Pair<>(6,new Pair<>(3,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c3_5, new Pair<>(8,new Pair<>(3,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c4_1, new Pair<>(0,new Pair<>(4,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c4_2, new Pair<>(1,new Pair<>(4,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c4_3, new Pair<>(3,new Pair<>(4,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c4_4, new Pair<>(4,new Pair<>(4,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c4_5, new Pair<>(6,new Pair<>(4,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c5_1, new Pair<>(0,new Pair<>(5,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c5_2, new Pair<>(2,new Pair<>(5,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c6_1, new Pair<>(0,new Pair<>(6,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c6_2, new Pair<>(1,new Pair<>(6,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c6_3, new Pair<>(3,new Pair<>(6,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c7_1, new Pair<>(0,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c7_2, new Pair<>(2,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c7_3, new Pair<>(3,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c7_4, new Pair<>(5,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c7_5, new Pair<>(9,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c7_6, new Pair<>(16,new Pair<>(7,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c8_1, new Pair<>(0,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c8_2, new Pair<>(2,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c8_3, new Pair<>(4,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c8_4, new Pair<>(6,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c8_5, new Pair<>(9,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c8_6, new Pair<>(12,new Pair<>(8,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c9_1, new Pair<>(0,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c9_2, new Pair<>(2,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c9_3, new Pair<>(4,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c9_4, new Pair<>(4,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c9_5, new Pair<>(8,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c9_6, new Pair<>(9,new Pair<>(9,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c10_1, new Pair<>(0,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c10_2, new Pair<>(3,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c10_3, new Pair<>(5,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c10_5, new Pair<>(7,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c10_6, new Pair<>(10,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c10_7, new Pair<>(11,new Pair<>(10,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c11_1, new Pair<>(0,new Pair<>(11,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c11_2, new Pair<>(3,new Pair<>(11,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c11_3, new Pair<>(9,new Pair<>(11,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c12_1, new Pair<>(0,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c12_2, new Pair<>(1,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c12_3, new Pair<>(7,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c12_4, new Pair<>(9,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c12_5, new Pair<>(10,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c12_6, new Pair<>(10,new Pair<>(12,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c13_1, new Pair<>(0,new Pair<>(13,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c13_2, new Pair<>(2,new Pair<>(13,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c13_3, new Pair<>(4,new Pair<>(13,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));


            mapChapter.put(c14_1, new Pair<>(0,new Pair<>(14,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c14_2, new Pair<>(4,new Pair<>(14,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c14_3, new Pair<>(6,new Pair<>(14,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c14_4, new Pair<>(7,new Pair<>(14,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c14_5, new Pair<>(11,new Pair<>(14,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

            mapChapter.put(c15_1, new Pair<>(0,new Pair<>(15,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));
            mapChapter.put(c15_2, new Pair<>(6,new Pair<>(15,"/media/arnab/Collections/Videos/&#39;Tu Jo Mila&#39; VIDEO Song - K.K. _ Salman Khan, Nawazuddin, Harshaali _ Bajrangi Bhaijaan - YouTube (1080p).mp4")));

        }catch (Exception e){
            System.out.println("Chapter not set due to :  "+e.toString());
        }

        try{
            chapterno=new LinkedHashMap<>();
            chapterno.put(1,new Pair<>("CHAP1.pdf",new Pair<>("CHAP1.txt","CHAP1.qtn")));
            chapterno.put(2,new Pair<>("CHAP2.pdf",new Pair<>("CHAP2.txt","CHAP2.qtn")));
            chapterno.put(3,new Pair<>("CHAP 3.pdf",new Pair<>("CHAP3.txt","CHAP3.qtn")));
            chapterno.put(4,new Pair<>("CHAP 4.pdf",new Pair<>("CHAP4.txt","CHAP4.qtn")));
            chapterno.put(5,new Pair<>("CHAP 5.pdf",new Pair<>("CHAP5.txt","CHAP5.qtn")));
            chapterno.put(6,new Pair<>("CHAP 6.pdf",new Pair<>("CHAP6.txt","CHAP6.qtn")));
            chapterno.put(7,new Pair<>("CHAP 7.pdf",new Pair<>("CHAP7.txt","CHAP7.qtn")));
            chapterno.put(8,new Pair<>("CHAP 8.pdf",new Pair<>("CHAP8.txt","CHAP8.qtn")));
            chapterno.put(9,new Pair<>("CHAP 9.pdf",new Pair<>("CHAP9.txt","CHAP9.qtn")));
            chapterno.put(10,new Pair<>("CHAP 10.pdf",new Pair<>("CHAP10.txt","CHAP10.qtn")));
            chapterno.put(11,new Pair<>("CHAP 11.pdf",new Pair<>("CHAP11.txt","CHAP11.qtn")));
            chapterno.put(12,new Pair<>("CHAP 12.pdf",new Pair<>("CHAP12.txt","CHAP12.qtn")));
            chapterno.put(13,new Pair<>("CHAP 13.pdf",new Pair<>("CHAP13.txt","CHAP13.qtn")));
            chapterno.put(14,new Pair<>("CHAP 14.pdf",new Pair<>("CHAP14.txt","CHAP14.qtn")));
            chapterno.put(15,new Pair<>("CHAP 15.pdf",new Pair<>("CHAP15.txt","CHAP15.qtn")));


        }catch (Exception e){
            System.out.println("Chapter number not set due to:  "+e.toString());
        }


        try {
//            textbox.setContent(new TextArea());
            image1 = new Image("file:///home/arnab/Desktop/download.jpg");
            image2 = new Image("file:///home/arnab/IdeaProjects/APP1.0/download%20(1).jpg");

            buttonImage1= new ImageView(image1);
            buttonImage1.setFitHeight(20.0);
            buttonImage1.setFitWidth(25.0);
            nextTopic.setGraphic(buttonImage1);

            buttonImage2= new ImageView(image2);
            buttonImage2.setFitHeight(20.0);
            buttonImage2.setFitWidth(25.0);
            prevTopic.setGraphic(buttonImage2);
        }catch (Exception e){
            System.out.println("ErrorLoading images: "+e.toString());
        }
        try{
            currentPage.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue.matches("")){

                    }
                    else if(newValue.matches("[1-9][0-9]*")){
                        int value=Integer.parseInt(newValue)-1;
                        if(value<Integer.parseInt(totalPages.getText().substring(1))) {
                            showpdf(value);
                            if(value==0){
                                previousButton.setDisable(true);
                            }
                            else{
                                previousButton.setDisable(false);
                            }
                            if(value==(Integer.parseInt(totalPages.getText().substring(1))-1)){
                                nextButton.setDisable(true);
                            }
                            else{
                                nextButton.setDisable(false);
                            }
                        }
                        else{
                            currentPage.setText(oldValue);
                        }
                    }
                    else{
                        currentPage.setText(oldValue);
                    }
                }
            });
        }catch (Exception e){
            System.out.println("current page property"+e.toString());
        }
    }

    public Controller(){
        super();
        executor=new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    public void onExit(MouseEvent e) throws IOException{

        ((MenuButton)e.getSource()).setStyle("-fx-background-color: white");

    }

    public void onEntered(MouseEvent e)throws IOException{

        try{
            b1.hide();
        }
        catch (Exception e1){}
        b1=(MenuButton)e.getSource();
        b1.setStyle("-fx-background-color: rgba(119,255,47,0.42)");
        b1.show();

    }

    public void showpdf(Integer page) {

        try {

            float scale = 1.0f;
            float rotation = 0f;
            // Paint each pages content to an image
            BufferedImage image1 = (BufferedImage) document.getPageImage(page,
                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);

            WritableImage fxImage = SwingFXUtils.toFXImage(image1, null);

            if (image.get() != null) {
                image.get().setImage(fxImage);
            } else {
                image.set(new ImageView(fxImage));
            }

            //Clean up
            image1.flush();
        }catch (Exception e){
            System.out.println("show PDF "+e.toString());
        }
    }

    private Document getDocument(String string){
        playarea.contentProperty().unbind();
        playarea.contentProperty().bind(image);
        org.icepdf.core.pobjects.Document document = new Document();
        try {
            File file = new File(string);
            document.setFile(file.getAbsolutePath());
        } catch (PDFException | PDFSecurityException | IOException ex) {
            System.out.println(ex.toString());
        }
        totalPages.setText("/"+String.valueOf(document.getNumberOfPages()));
        return document;
    }

    public void onMenuItemClick(ActionEvent event){

        int chapno=0;


        String video=null;
        int pageno=0;
        try {
            topicLabel.setText(((MenuItem)event.getSource()).getText());
            pageno=mapChapter.get(event.getSource()).getKey();
            chapno= mapChapter.get(event.getSource()).getValue().getKey();
            video = mapChapter.get(event.getSource()).getValue().getValue();
            examFilePath="Question.qtn";
        }
        catch (Exception e){
            System.out.println("onMouseclick "+e.toString());
        }

        String pdf=chapterno.get(chapno).getKey();
        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno+1));
        if(chapno==checkChapNo){
            showpdf(pageno);
        }else {
            showpdf(pageno);
            setVideo(video);
            checkChapNo=chapno;
        }
        temp= (MenuItem)event.getSource();

        getStudentNotes();

    }

    public void setVideo(String path){
        try{
            mediaView.getMediaPlayer().dispose();
        }catch (Exception e){    }
        File file=new File(path);
        if(file!=null) {
            Media media = new Media(file.toURI().toString());
            duration=media.getDuration();
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaView.getMediaPlayer().setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    stopMedia();
                }
            });
            mediaView.getMediaPlayer().currentTimeProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    updateMediaValue();
                }
            });
            mediaView.getMediaPlayer().setOnReady(new Runnable() {
                @Override
                public void run() {
                    duration=mediaView.getMediaPlayer().getMedia().getDuration();
                }
            });
            mediaProgressBar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton()== MouseButton.PRIMARY){
                        //System.out.println((event.getSceneX()-leftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                        mediaProgressBar.setProgress((event.getSceneX()-scrollLeftMenu.getWidth()-playarea.getWidth())/mediaProgressBar.getWidth());
                        mediaView.getMediaPlayer().seek(duration.multiply(mediaProgressBar.getProgress()));
                    }
                }
            });
            stopMedia();
        }
    }

    public void playMedia(){
        mediaView.getMediaPlayer().play();
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pauseMedia();
            }
        });
        playButton.setText("Pause");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pauseMedia();
            }
        });
    }
    public void pauseMedia(){
        mediaView.getMediaPlayer().pause();
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playMedia();
            }
        });
        playButton.setText("Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playMedia();
            }
        });
    }
    public void stopMedia(){
        mediaView.getMediaPlayer().seek(duration.multiply(0.0));
        mediaProgressBar.setProgress(0.0);
        pauseMedia();
    }
    public void muteMedia(){
        if(mediaView.getMediaPlayer().isMute()){
            mediaView.getMediaPlayer().setMute(false);
        }
        else{
            mediaView.getMediaPlayer().setMute(true);
        }
    }

    public void setMediaToFullScreen() {
        Main.window.setFullScreen(true);
        topBar.setPrefHeight(0.0);
        topBar.setMinHeight(0.0);
        topBar.setMaxHeight(0.0);
        scrollLeftMenu.setPrefWidth(0.0);
        scrollLeftMenu.setMinWidth(0.0);
        scrollLeftMenu.setMaxWidth(0.0);
        textbox.setPrefHeight(0.0);
        textbox.setMinHeight(0.0);
        textbox.setMaxHeight(0.0);
        playarea.setPrefWidth(0.0);
        playarea.setMinWidth(0.0);
        playarea.setMaxWidth(0.0);
        topBar.setVisible(false);
        scrollLeftMenu.setVisible(false);
        textbox.setVisible(false);
        playarea.setVisible(false);
        fullScreenButton.setText("ExitFullScreen");
        fullScreenButton.setOnAction(event -> {
            exitMediaFromFullScreen();
        });
        exitFullScreenHandler=new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== KeyCode.ESCAPE){
                    exitMediaFromFullScreen();
                }
            }
        };
//        Main.window.getScene().getRoot().addEventHandler(KeyEvent,exitFullScreenHandler);

    }

    public void exitMediaFromFullScreen(){
        Main.window.setFullScreen(false);
        topBar.setPrefHeight(30.0);
        topBar.setMinHeight(30.0);
        topBar.setMaxHeight(30.0);
        scrollLeftMenu.setPrefWidth(200.0);
        scrollLeftMenu.setMinWidth(200.0);
        scrollLeftMenu.setMaxWidth(200.0);
        textbox.setPrefHeight(84.0);
        textbox.setMinHeight(84.0);
        textbox.setMaxHeight(84.0);
        playarea.setPrefWidth(620.0);
        playarea.setMinWidth(620.0);
        playarea.setMaxWidth(620.0);
        topBar.setVisible(true);
        scrollLeftMenu.setVisible(true);
        textbox.setVisible(true);
        playarea.setVisible(true);
        fullScreenButton.setText("FullScreen");
        fullScreenButton.setOnAction(event -> {
            setMediaToFullScreen();
        });
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
    private void updateMediaValue(){
        if(mediaProgressBar!=null && playTime!=null){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Duration currentTime=mediaView.getMediaPlayer().getCurrentTime();
                    playTime.setText(formatTime(currentTime,duration));
                    mediaProgressBar.setProgress(currentTime.toMillis()/duration.toMillis());
                }
            });
        }
    }

    public void setProperties(){
        {

            playAreaContainer.setLayoutY(0);
            scene.prefWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.prefWidthProperty().bind(scene.widthProperty());
            playAreaContainer.prefWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.prefWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.prefWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.maxWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.maxWidthProperty().bind(scene.widthProperty());
            playAreaContainer.maxWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.maxWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.maxWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            scene.minWidthProperty().bind(Main.window.getScene().widthProperty());
            totalContainer.minWidthProperty().bind(scene.widthProperty());
            playAreaContainer.minWidthProperty().bind(totalContainer.widthProperty().subtract(scrollLeftMenu.widthProperty()));
            lectureContainer.minWidthProperty().bind(playAreaContainer.widthProperty());
            mediaContainer.minWidthProperty().bind(lectureContainer.widthProperty().subtract(playarea.widthProperty()));

            mediaProgressBar.prefWidthProperty().bind(mediaContainer.widthProperty());
            mediaProgressBar.maxWidthProperty().bind(mediaContainer.widthProperty());
            mediaProgressBar.minWidthProperty().bind(mediaContainer.widthProperty());
            mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
        }
        {
            scene.prefHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.prefHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.prefHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.prefHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.prefHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.prefHeightProperty().bind(playarea.heightProperty());
            mediaContainer.prefHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            scene.maxHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.maxHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.maxHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.maxHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.maxHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.maxHeightProperty().bind(playarea.heightProperty());
            mediaContainer.maxHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            scene.minHeightProperty().bind(Main.window.getScene().heightProperty());
            totalContainer.minHeightProperty().bind(scene.heightProperty());
            scrollLeftMenu.minHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playAreaContainer.minHeightProperty().bind(totalContainer.heightProperty().subtract(topBar.heightProperty()));
            playarea.minHeightProperty().bind(playAreaContainer.heightProperty().subtract(textbox.heightProperty()));
            playerContainer.minHeightProperty().bind(playarea.heightProperty());
            mediaContainer.minHeightProperty().bind(playerContainer.heightProperty().subtract(buttonContainer.heightProperty().add(progressContainer.heightProperty())));

            mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());

        }
    }

    public void getNextTopic(ActionEvent event){
        String pdf=null;
        String video=null;
        List list= new ArrayList(mapChapter.keySet());
        ListIterator<MenuItem> it =list.listIterator();

        MenuItem tempkey=null;

        for(int i=0;i<list.size();){
            if(temp==list.get(i)){
                tempkey=(MenuItem) list.get(i+1);
                temp=tempkey;
                break;
            }
            else{
                i++;
            }
        }



        int pageno=mapChapter.get(tempkey).getKey();
        int chapno= mapChapter.get(tempkey).getValue().getKey();
        video = mapChapter.get(tempkey).getValue().getValue();
        pdf=chapterno.get(chapno).getKey();

        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno +1));
        showpdf(pageno);
        //setVideo(video);

    }
    public void getPrevTopic(ActionEvent event){
        String pdf=null;
        String video=null;
        List list= new ArrayList(mapChapter.keySet());
        ListIterator<MenuItem> it =list.listIterator();

        MenuItem tempkey=null;

        for(int i=0;i<list.size();i++){
            if(temp==list.get(i)){
                tempkey=(MenuItem) list.get(i-1);
                temp=tempkey;
            }
        }


        int pageno=mapChapter.get(tempkey).getKey();
        int chapno= mapChapter.get(tempkey).getValue().getKey();
        video = mapChapter.get(tempkey).getValue().getValue();
        pdf=chapterno.get(chapno).getKey();

        document=getDocument(pdf);
        currentPage.setText(String.valueOf(pageno +1));
        showpdf(pageno);
        //setVideo(video);


    }

    public void previouseButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())-1));
    }
    public void nextButtonAction(){
        currentPage.setText(String.valueOf(Integer.parseInt(currentPage.getText())+1));
    }


    public void startExam() throws Exception{
        FileInputStream fis=new FileInputStream(examFilePath);
        ObjectInputStream ois=new ObjectInputStream(fis);
        ExamSceneController.questions=(List<MCQ>) ois.readObject();
        ExamSceneController.sourceScene=Main.window.getScene();
        Scene examScene=new Scene(FXMLLoader.load(getClass().getResource("examScene.fxml")),Main.window.getScene().getWidth(),Main.window.getScene()    .getHeight());
        Main.window.setScene(examScene);

    }


    public void saveStudentNotes(ActionEvent event){
        try{
            int chapno=mapChapter.get(temp).getValue().getKey();
            FileWriter fileWriter;
            fileWriter = new FileWriter(new File(chapterno.get(chapno).getValue().getKey()));
            fileWriter.write(notes.getText());
            fileWriter.close();

        }catch(IOException e){
            System.out.println("save student notes   "+ e.toString());
        }

    }

    public void getStudentNotes(){
        try{
            int chapno=mapChapter.get(temp).getValue().getKey();
            Scanner scanner=new Scanner(new File(chapterno.get(chapno).getValue().getKey()));
            notes.deleteText(0,notes.getText().length());
            while(scanner.hasNext()){
                notes.appendText(scanner.next());
            }
        }catch(FileNotFoundException e){
            System.out.println("Getting student notes "+ e.toString());
        }
    }



}