package cool.structures;
import cool.ast.*;

import java.util.ArrayList;
import java.util.List;


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

    static public List<String> getAttributes(ClassNode node) {
        if (node == null) {
            // Reached a null parent
            return new ArrayList<>();
        }

        // Get the list from parents
        List<String> attributes = getAttributes(node.parent);

        // Add the current node's attributes
        for (var symbol : node.s.symbols.values()) {
            attributes.add(symbol.getName());
        }

        // Return the list
        return attributes;
    }

}
