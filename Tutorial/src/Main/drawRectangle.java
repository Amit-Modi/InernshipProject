package Main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

/**
 * Created by ghost on 1/6/17.
 */
public class drawRectangle extends Application{
    CheckBox drawButtonCheckBox;

    public static void main(String[] args) {
        launch(args);
    }


    Pane root;

    @Override
    public void start(Stage primaryStage) {

        root = new Pane();
        root.setStyle("-fx-background-color:white");
        root.setPrefSize(1024, 768);

        drawButtonCheckBox = new CheckBox( "Draw Button");

        root.getChildren().add( drawButtonCheckBox);

        primaryStage.setScene(new Scene(root, root.getWidth(), root.getHeight()));
        primaryStage.show();

        new RubberBandSelection(root);

    }


    public class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect;

        Pane group;

        public RubberBandSelection( Pane group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                dragContext.mouseAnchorX = event.getSceneX();
                dragContext.mouseAnchorY = event.getSceneY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);

            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                // get coordinates
                double x = rect.getX();
                double y = rect.getY();
                double w = rect.getWidth();
                double h = rect.getHeight();

                if( drawButtonCheckBox.isSelected()) {

                    // create button
                    Button node = new Button();
                    node.setDefaultButton(false);
                    node.setPrefSize(w, h);
                    node.setText("Button");
                    node.setLayoutX(x);
                    node.setLayoutY(y);
                    root.getChildren().add( node);


                } else {
                    // create rectangle
                    Rectangle node = new Rectangle( 0, 0, w, h);
                    node.setStroke( Color.BLACK);
                    node.setFill( Color.BLACK.deriveColor(0, 0, 0, 0.3));
                    node.setLayoutX( x);
                    node.setLayoutY( y);
                    root.getChildren().add( node);
                }


                // remove rubberband
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);


            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                double offsetX = event.getSceneX() - dragContext.mouseAnchorX;
                double offsetY = event.getSceneY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getSceneX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getSceneY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };

        private final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }
}
