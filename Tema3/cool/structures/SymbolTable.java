package cool.structures;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;
import org.stringtemplate.v4.ST;

public class SymbolTable {
    public static DefaultScope globals;
    
    private static boolean semanticErrors;

    static public ClassNode object;
    public static Map<String, ClassNode> classMap;
    public static Map<ClassNode, DefaultScope> scopeMap; //pt fiecare clasa e un scope

    // String accumulator
    public static Integer string_count;
    // Int accumulator
    public static Integer integer_count;
    // Dispatch accumulator
    public static Integer dispatch_count;
    // Bool to see if the program name was added
    public static Boolean isProgramNameAdded;
    //
    public static Map<String, Integer> stringMap;
    //
    public static Map<Integer, Integer> integerMap;
    //
    public static Map<String, Integer> stringIntMap;
    //
    public static Map<Integer, String> tagClassMap;

    public static Map<String, ArrayList<String>> classAtributeMap;

    public static int ifTag;
    public static int tag_void;
    public static int case_void;
    public static int not_tag;

    public static void addStringFinal(String in){
        // TODO CHECK HERE LATER
        // Normally this shouldn't happen
//        String s;
//
//        if (in.length() == 0) {
//            s = null;
//        } else {
//            s = in;
//        }
//
//        if(SymbolTable.addString(s)){
//            int size = s.length();
//            Integer ret;
//            if(SymbolTable.integerMap.containsKey(new Integer(size))){
//                ret = SymbolTable.integerMap.get(new Integer(size));
//            } else {
//                SymbolTable.addInteger(new Integer(size));
//                ret = new Integer(SymbolTable.integerMap.get(new Integer(size)));
//            }
//            SymbolTable.addStringInt(s, ret);
//        }

        // Check if the string is added or
        //System.out.println(in);
        if (SymbolTable.addString(in)) {
            int size = in.length();
            SymbolTable.addInteger(size);

            int value = in.length();
            int key = 0;

            for (Integer i : SymbolTable.integerMap.keySet()) {
                if (SymbolTable.integerMap.get(i).equals(value)) {
                    key = i;
                }
            }

            SymbolTable.addStringInt(in, key);
        }
        // If it already exists, it won't be added
    }

    public static boolean addStringInt(String s, Integer i){
        if (stringIntMap.containsKey(s))
            return true;

        stringIntMap.put(s, i);

        return false;
    }

    public static boolean addString(String s){
        if (stringMap.containsKey(s))
            return false;

        stringMap.put(s, string_count);
        string_count++;

        return true;
    }

    // Adds an entry to integerMap if it's not already in it
    public static boolean addInteger(int i){
        if (integerMap.containsValue(i))
            return false;

        integerMap.put(integer_count, i);
        integer_count++;

        return true;
    }





    public static void defineBasicClasses() {
        // TODO: PUT THOSE PARTS INTO A DIFFERENT FUNCTION
        classMap = new LinkedHashMap<>();
        scopeMap = new LinkedHashMap<>();
        stringMap = new LinkedHashMap<>();
        integerMap = new LinkedHashMap<>();
        stringIntMap = new LinkedHashMap<>();
        tagClassMap = new LinkedHashMap<>();
        classAtributeMap = new LinkedHashMap<>();
        string_count = 0;
        integer_count = 0;
        dispatch_count = 0;
        isProgramNameAdded = false;

        semanticErrors = false;

        ifTag = 0;
        tag_void = 0;
        case_void = 0;
        not_tag = 0;

        object = new ClassNode("Object",null);
        object.s = new DefaultScope(null, object, "Class");


        Symbol name = new Symbol("copy");
        ArrayList<Symbol> sArgs = new ArrayList<>();
        ArrayList<Symbol> tArgs = new ArrayList<>();
        tArgs.add(new Symbol("SELF_TYPE"));
        Touple s = new Touple(name, sArgs);
        Touple t = new Touple(name, tArgs);
        object.s.addF(s);
        object.s.addFType(t);


        name = new Symbol("type_name");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        tArgs.add(new Symbol("String"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        object.s.addF(s);
        object.s.addFType(t);


        name = new Symbol("abort");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        tArgs.add(new Symbol("Object"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        object.s.addF(s);
        object.s.addFType(t);
        addClassInTable(object);


        ClassNode IO = new ClassNode("IO", object);
        IO.s = new DefaultScope(object.s, IO, "Class");
        addClassInTable(IO);


        name = new Symbol("out_string");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        sArgs.add(new Symbol("x"));
        tArgs.add(new Symbol("SELF_TYPE"));
        tArgs.add(new Symbol("String"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        IO.s.addF(s);
        IO.s.addFType(t);

        name = new Symbol("out_int");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        sArgs.add(new Symbol("x"));
        tArgs.add(new Symbol("SELF_TYPE"));
        tArgs.add(new Symbol("Int"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        IO.s.addF(s);
        IO.s.addFType(t);



        name = new Symbol("in_string");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        tArgs.add(new Symbol("String"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        IO.s.addF(s);
        IO.s.addFType(t);

        name = new Symbol("in_int");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        tArgs.add(new Symbol("Int"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        IO.s.addF(s);
        IO.s.addFType(t);



        ClassNode intt = new ClassNode("Int", object);
        intt.inheritable = false;
        intt.s = new DefaultScope(object.s, intt, "Class");
        addClassInTable(intt);

        ClassNode str = new ClassNode("String", object);
        str.inheritable = false;
        str.s = new DefaultScope(object.s, str, "Class");
        addClassInTable(str);

        name = new Symbol("length");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        tArgs.add(new Symbol("Int"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        str.s.addF(s);
        str.s.addFType(t);


        name = new Symbol("concat");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        sArgs.add(new Symbol("s"));
        tArgs.add(new Symbol("String"));
        tArgs.add(new Symbol("String"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        str.s.addF(s);
        str.s.addFType(t);

        name = new Symbol("substr");
        sArgs = new ArrayList<>();
        tArgs = new ArrayList<>();
        sArgs.add(new Symbol("i"));
        sArgs.add(new Symbol("l"));
        tArgs.add(new Symbol("String"));
        tArgs.add(new Symbol("Int"));
        tArgs.add(new Symbol("Int"));
        s = new Touple(name, sArgs);
        t = new Touple(name, tArgs);
        str.s.addF(s);
        str.s.addFType(t);


        ClassNode bool = new ClassNode("Bool", object);
        bool.inheritable = false;
        bool.s = new DefaultScope(object.s, bool, "Class");
        addClassInTable(bool);


        Integer i = 0;
        addInteger(i);
        addString(null);

        addStringInt(null, i);

    }
    public static boolean addScopeInTable(ClassNode c, DefaultScope s) {
        // Reject duplicates in the same scope.
        if (scopeMap.containsKey(c))
            return false;

        scopeMap.put(c, s);

        return true;
    }

    public static boolean addClassInTable(ClassNode c) {
        // Reject duplicates in the same scope.
        if (classMap.containsKey(c.name))
            return false;

        classMap.put(c.name, c);

        return true;
    }

    public static ClassNode lookupClass(String name) {
        var c = classMap.get(name);

        return c;
    }

    public static ClassNode removeClass(String name){
        return classMap.remove(name);
    }

    /**
     * Displays a semantic error message.
     * 
     * @param ctx Used to determine the enclosing class context of this error,
     *            which knows the file name in which the class was defined.
     * @param info Used for line and column information.
     * @param str The error message.
     */
    public static void error(ParserRuleContext ctx, Token info, String str) {
        while (! (ctx.getParent() instanceof CoolParser.ProgramContext))
            ctx = ctx.getParent();
        
        String message = "\"" + new File(Compiler.fileNames.get(ctx)).getName()
                + "\", line " + info.getLine()
                + ":" + (info.getCharPositionInLine() + 1)
                + ", Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static void error(String str) {
        String message = "Semantic error: " + str;
        
        System.err.println(message);
        
        semanticErrors = true;
    }
    
    public static boolean hasSemanticErrors() {
        return semanticErrors;
    }
}
