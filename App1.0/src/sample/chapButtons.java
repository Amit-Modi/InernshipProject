package sample;

import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;

/**
 * Created by arnab on 6/6/17.
 */
public class chapButtons {



    public static VBox leftMenu=new VBox();
    static  int numChap=7;
   static MenuButton[] chaps;

    public static VBox addChaps(){
        leftMenu.setPrefWidth(160);
        leftMenu.setLayoutX(0);
        leftMenu.setLayoutY(0);

        for(int i=0;i<numChap;i++)
        {
                chaps[i]= new MenuButton("Chapter"+(i+1));

                chaps[i].setLayoutY(i*45);

            leftMenu.getChildren().add(chaps[i]);
        }
        return leftMenu;
    }

}
