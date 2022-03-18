package cool.structures;

public class StructScope extends ScopeBase{

    public boolean add(Symbol sym) {
        return false;
    }


    public Symbol lookup(String str) {
        return null;
    }


    public Scope getParent() {
        return null;
    }
}
