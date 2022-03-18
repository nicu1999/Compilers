package cool.structures;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.antlr.v4.runtime.*;

import cool.compiler.Compiler;
import cool.parser.CoolParser;

public class SymbolTable {
    public static DefaultScope globals;
    
    private static boolean semanticErrors;

    static public ClassNode object;

    public static Map<String, ClassNode> classMap;

    public static Map<ClassNode, DefaultScope> scopeMap; //pt fiecare clasa e un scope

    public static void defineBasicClasses() {
        classMap = new LinkedHashMap<>();
        scopeMap = new LinkedHashMap<>();
        semanticErrors = false;

        object = new ClassNode("Object",null);
        object.s = new DefaultScope(null, object, "Class");


        ClassNode IO = new ClassNode("IO", object);
        IO.s = new DefaultScope(object.s, IO, "Class");
        addClassInTable(IO);

        ClassNode intt = new ClassNode("Int", object);
        intt.inheritable = false;
        intt.s = new DefaultScope(object.s, intt, "Class");
        addClassInTable(intt);

        ClassNode str = new ClassNode("String", object);
        str.inheritable = false;
        str.s = new DefaultScope(object.s, str, "Class");
        addClassInTable(str);

        ClassNode bool = new ClassNode("Bool", object);
        bool.inheritable = false;
        bool.s = new DefaultScope(object.s, bool, "Class");
        addClassInTable(bool);

        addClassInTable(object);

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
