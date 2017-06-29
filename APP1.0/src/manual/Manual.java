package manual;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import sample.Main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by arnab on 22/6/17.
 */
public class Manual  implements Initializable {
    @FXML
    VBox index;

    @FXML
    public ImageView manualView;

    public ImageView imageView;

    //public static WritableImage image;



    public Stage newWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //imageView= new ImageView();
        FileChooser choos = new FileChooser();
        File file= choos.showOpenDialog(Main.window.getScene().getWindow());
        imageView = new ImageView(new Image(file.toURI().toString()));
        System.out.println(imageView);
        manualView=imageView;

    }





    public void showpdf(Integer page) {



            Document document1 = new Document();
            try {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(Main.window.getScene().getWindow());
                document1.setFile(file.getAbsolutePath());
            } catch (PDFException | PDFSecurityException | IOException ex) {
                System.out.println(ex.toString());
            }
            // Paint each pages content to an image

            float scale = 1.0f;
            float rotation = 0f;

            // Paint each pages content to an image
            BufferedImage image = (BufferedImage)document1.getPageImage(page,
                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);

            WritableImage fxImage = SwingFXUtils.toFXImage(image,null);
            System.out.println(fxImage);

            if (imageView != null) {
                imageView.setImage(fxImage);
            } else {
                imageView= new ImageView(fxImage);
            }
            manualView=imageView;

            //Clean up
            image.flush();
        //return currentImage;
    }

}
