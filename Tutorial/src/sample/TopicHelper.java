package sample;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Pair;

/**
 * Created by ghost on 1/6/17.
 */
public class TopicHelper{
    public static TreeItem<String> clone(TreeItem<Pair<Integer,String>> courselist){
        int s=courselist.getChildren().size();
        Pair<Integer,String> pair=courselist.getValue();
        TreeItem<String> t=new TreeItem<>(pair.getValue());
        for(int i=0;i<s;i++){

        }
        return t;
    }
}
