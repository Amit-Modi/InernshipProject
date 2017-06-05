package Main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ghost on 29/5/17.
 * TreeView
 * and
 * DoubleClick and Single Click
 */
public class TreeViewClass extends Application {
    Stage window;
    Scene scene;
    TreeView<String> treeView;
    Boolean dragFlag=false;
    int clickCounter=0;

    ScheduledThreadPoolExecutor executor;
    ScheduledFuture<?> scheduledFuture;

    public TreeViewClass(){
        executor=new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true);
    }

    public static void main(String[]args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window=primaryStage;
        window.setTitle("TreeView");

        TreeItem<String> root,c1,c2;
        //Root
        root=new TreeItem<>();
        root.setExpanded(false);
        //c1
        c1=makeBranch("C10",root);
        makeBranch("1",c1);
        makeBranch("2",c1);
        //c2
        c2=makeBranch("C20",root);
        makeBranch("1",c2);
        makeBranch("2",c2);

        treeView=new TreeView<>(root);
        treeView.setShowRoot(false);
        //Set Listener
/*        treeView.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue)->{
            if(newValue!=null){
                System.out.println(newValue.getValue());
            }
        });
*/
//set Double click or singel click
        treeView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    dragFlag=true;
                }
            }
        });
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(!dragFlag){
                        System.out.println(++clickCounter+" "+ event.getClickCount());
                        if(event.getClickCount()==1){
                            scheduledFuture=executor.schedule(()->sigleClickAction(),500, TimeUnit.MILLISECONDS);
                        }
                        else if(event.getClickCount()>1){
                            if(scheduledFuture!=null && !scheduledFuture.isCancelled() && !scheduledFuture.isDone()){
                                scheduledFuture.cancel(false);
                                doubleClickAction();
                            }
                        }
                    }
                }
            }
        });

        StackPane layout =new StackPane();
        layout.getChildren().addAll(treeView);

        Scene scene=new Scene(layout,300,200);
        window.setScene(scene);
        window.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        executor.shutdown();
    }

    public TreeItem<String> makeBranch(String s, TreeItem<String> p){
        TreeItem<String>item =new TreeItem<>(s);
        item.setExpanded(true);
        p.getChildren().add(item);
        return item;
    }

    private void sigleClickAction(){
        //System.out.println("single");
        System.out.println(treeView.getSelectionModel().getSelectedIndex());

    }
    private void doubleClickAction(){
        //System.out.println("double");
        String message="";
        TreeItem<String> mans;
        mans=treeView.getSelectionModel().getSelectedItem();
        System.out.println(mans.getValue());
    }
}
