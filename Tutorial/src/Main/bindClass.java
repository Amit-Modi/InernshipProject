package Main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by ghost on 29/5/17.
 */
public class bindClass {
    public static void main(String[]ss){
        IntegerProperty x,y;
        x=new SimpleIntegerProperty(3);
        y=new SimpleIntegerProperty();

        y.bind(x.multiply(10));
        System.out.println(x.getValue()+" "+y.getValue());
        x.setValue(10);
        System.out.println(x.getValue()+" "+y.getValue());
        y.setValue(40);
        System.out.println(x.getValue()+" "+y.getValue());
    }
}
