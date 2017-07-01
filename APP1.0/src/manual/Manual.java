package manual;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by arnab on 22/6/17.
 */
public class Manual  implements Initializable {
    @FXML
    VBox index;

    @FXML
    public Pagination manualPaginationView;

    //public static WritableImage image;



    public Stage newWindow;
    private ArrayList<StackPane> pages;
    private Integer each;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pages=showpdf();
        int size=pages.size();
        manualPaginationView.setMaxPageIndicatorCount(size);
        manualPaginationView.setPageCount(size);
        for(each=0; each<size;each++){
            System.out.println(each+" ");
            manualPaginationView.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer param) {
                    return pages.get(param);
                }
            });
        }

    }





    public ArrayList<StackPane> showpdf() {
        ArrayList<StackPane> result=new ArrayList<>();
        Document document1 = new Document();
        try {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(Main.window);
            System.out.println(file.getAbsolutePath());
            document1.setFile(file.getAbsolutePath());
        } catch (PDFException | PDFSecurityException | IOException ex) {
            ex.printStackTrace();
        }
        // Paint each pages content to an image

        float scale = 1.0f;
        float rotation = 0f;
        int size=document1.getNumberOfPages();
        for(int page=0;page<size;page++) {
            // Paint each pages content to an image
//            BufferedImage image = (BufferedImage) document1.getPageImage(page,
//                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
//                System.out.println(fxImage);
            result.add(new StackPane(new ImageView(SwingFXUtils.toFXImage((BufferedImage) document1.getPageImage(page,
                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale), null))));

            //Clean up
            //return currentImage;
        }
        return result;
    }

}
