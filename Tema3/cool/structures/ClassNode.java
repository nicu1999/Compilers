package cool.structures;

import org.stringtemplate.v4.ST;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassNode {
    public String name;
    public ClassNode parent;
    public String temporaryParent;
    public ArrayList<ClassNode> children;

    public boolean inheritable = true;
    public DefaultScope s; //class scope

    // Important for code generation
    // Class index id
    public int tag;
    // Dispatch table list, ordered for offsets
    public List<String> dispatchList;
    // TODO EXPLAIN
    public Map<String, Integer> atributeOffsetMap;
    public Map<String, ST> atributeSTtMap;
    public Map<String, ST> methodSTtMap;

    public ClassNode(String name, ClassNode parent){
        this.name = name;
        children = new ArrayList<>();
        if(parent != null)
            parent.addChild(this);
    }

    public Pair<Integer, Integer> classTagInterval(){
        int i = 0;
        int start = -1;
        int stop = -1;
        boolean started = false;
        for(var x : SymbolTable.tagClassMap.values()){
            if(x.equals(name)) {
                start = i;
                started = true;
            }

            ClassNode c = SymbolTable.classMap.get(x);

            if(!isSubType(c, this) && started) {
                stop = i - 1;
                break;
            }
            i++;
        }
        return new Pair(start, stop);
    }

    public int getDepth(){
        if(parent == null){
            return 0;
        }
        if(SymbolTable.classAtributeMap.get(this.name) != null) {
            atributeOffsetMap = new LinkedHashMap<>();
            int parentDepth = parent.getDepth();
            ArrayList<String> atrs = SymbolTable.classAtributeMap.get(this.name);

            for(int i = 0; i < atrs.size(); i++){
                atributeOffsetMap.put(atrs.get(i),parentDepth + i);
            }

            return SymbolTable.classAtributeMap.get(this.name).size() + parentDepth;
        }
        else
            return 0;

    }

    public ClassNode(String name, String temporaryParent, DefaultScope s){
        this.name = name;
        children = new ArrayList<>();
        parent = null;
        this.temporaryParent = temporaryParent;
        this.s = s;
    }

    public Integer DFS(Integer cont, int depth){
        tag = cont;

        for(ClassNode child : children){
            cont = cont + 1;
            cont = child.DFS(cont, depth + 1);
        }

        return cont;

    }

    public void addChild(ClassNode c){
        children.add(c);
        c.parent = this;
    }

    public boolean detectCycle(){
        ArrayList<ClassNode> lt = new ArrayList<>();
        ClassNode crawler = this;
        lt.add(crawler);
        while(crawler != null) {
            crawler = crawler.parent;
            for(ClassNode n : lt){
                if(crawler == n){
                    return true;
                }
            }
            lt.add(crawler);
        }
        return false;
    }

    public static boolean isSubType(ClassNode c1, ClassNode c2) { //is c1 subtype of c2 : c1 <- .. <- c2
        if(c1 == null){
            return false;
        }
        if(c1.name.equals(c2.name)){
            return true;
        }
        return isSubType(c1.parent, c2);
    }


    public static ClassNode greatestCommonAncestorFold(ArrayList<ClassNode> ls, ClassNode acc, ClassNode currNode){
        if(ls.size() == 0){
            if(acc == null)
                return null;
            return acc;
        } else{
            if(acc == null) {
                return greatestCommonAncestorFold(new ArrayList(ls.subList(0, ls.size() - 1)), ls.get(ls.size() - 1), currNode);
            }
            else {
                acc = greatestCommonAncestor(ls.get(ls.size() - 1), acc, currNode);
                return greatestCommonAncestorFold(new ArrayList(ls.subList(0, ls.size() - 1)), acc, currNode);
            }
        }
    }

    public static ClassNode greatestCommonAncestor(ClassNode c1, ClassNode c2, ClassNode currNode){
        ArrayList<ClassNode> ls1 = new ArrayList<>();
        ArrayList<ClassNode> ls2 = new ArrayList<>();

        while(c1 != null){
            ls1.add(c1);
            c1 = c1.parent;
        }
        while(c2 != null){
            ls2.add(c2);
            c2 = c2.parent;
        }

        int greatestSize = Math.max(ls1.size(), ls2.size());

        for(int i = 0; i < greatestSize; i++){
            if(ls1.size() - 1 - i != -1 )
                c1 = ls1.get(ls1.size() - 1 - i);
            else
                return ls1.get(ls1.size() - i);
            if(ls2.size() - 1 - i != -1)
                c2 = ls2.get(ls2.size() - 1 - i);
            else
                return ls2.get(ls2.size() - i);
            if(!c1.name.equals(c2.name)){
                return ls1.get(ls1.size() - i);
            }
        }

        return ls1.get(0);

    }

    @Override
    public String toString() {
        return "ClassNode{" +
                "name='" + name + '\'' +
                ", tag=" + tag +
                '}';
    }
}
