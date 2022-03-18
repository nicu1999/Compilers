package cool.structures;
import cool.ast.*;


public class Helper {

    public static String setClassName(Class1 class1){
        return class1.lt.get(0);
    }

    public static String setParentName(Class1 class1){
        String parent;
        if(class1.lt.size() == 2){
            parent = class1.lt.get(1);
        } else {
            parent = "Object";
        }
        return parent;
    }

}
