package cool.structures;

import java.util.*;

public class DefaultScope implements Scope { // lord forgive who is about to read this code

    private DefaultScope parent;
    public DefaultScope(DefaultScope parent) {
        this.parent = parent;
    }

    //CLASS SCOPE
    public Map<String, Symbol> symbols = new LinkedHashMap<>();//atr ids
    public Map<String, Symbol> symbolsType = new LinkedHashMap<>();//atr type
    public Map<String, Touple> fSymbols = new LinkedHashMap<>();//method id, formal ids
    public Map<String, Touple> fTypeSymbols = new LinkedHashMap<>();//method id, method type, formal types
    //fTypeSymbols(0) == tipul functiei fTypeSymbols(1,n) = tipul formalilor
    public ClassNode currentClass = null;
    public String scopeType = "Class";


    //METHOD SCOPE
    public Symbol methodId;
    public ArrayList<Symbol> methodIds;//formal
    public ArrayList<Symbol> methodTypes;
    //parametrul 0 e la 12, 1 e la 16..

    //(LET, CASE, STRUCT) SCOPE
    public Symbol structId;//formal let x, y, z (scope x (scope y (...)))
    public Symbol structType;
    public int letOffset; //(in bytes) 4, 8, 12..
    public int full_ofset;

    boolean debug = false;


    public int getIdOffset(String id){
        if(scopeType.equals("Let") || scopeType.equals("Case") || scopeType.equals("Struct"))
            if(structId != null)
                if(structId.getName().equals(id))
                    return letOffset;

        if(scopeType.equals("Method")){
            for(int i = 0; i < methodIds.size(); i++){
                if(methodIds.get(i).getName().equals(id)){
                    return 12 + i * 4;
                }
            }
        }

        if(scopeType.equals("Class")){
            var list = Helper.getAttributes(currentClass);
            for(int i = 0; i <list.size(); i++){
                if(id.equals(list.get(i))){
                    return 12 + i * 4;
                }
            }
            return 0;
        }

        if(parent != null)
            return parent.getIdOffset(id);

        return -1;
    }

    public void setParent(DefaultScope s){
        this.parent = s;
    }

    public DefaultScope(DefaultScope parent, String scopeType) {
        this.parent = parent;
        this.scopeType = scopeType;
    }

    public DefaultScope(DefaultScope parent, ClassNode currentClass, String scopeType) {
        this.parent = parent;
        this.currentClass = currentClass;
        this.scopeType = scopeType;
    }

    public void addCurrentClass(ClassNode c){
        currentClass = c;
    }

    public String getIdScope(String name){
        if(scopeType.equals("Let") || scopeType.equals("Case") || scopeType.equals("Struct"))
            if(structId != null)
                if(structId.getName().equals(name))
                    return scopeType;

        if(scopeType.equals("Method")){
            for(int i = 0; i < methodIds.size(); i++){
                if(methodIds.get(i).getName().equals(name)){
                    return scopeType;
                }
            }
        }

        if(scopeType.equals("Class")){
            //Symbol s = lookupType(name);
            return scopeType;
        }

        if(parent != null)
            return parent.getIdScope(name);

        return null;
    }

    public Symbol getType(String name){
        if(scopeType.equals("Let") || scopeType.equals("Case") || scopeType.equals("Struct"))
            if(structId != null)
                if(structId.getName().equals(name))
                    return structType;

        if(scopeType.equals("Method")){
            for(int i = 0; i < methodIds.size(); i++){
                if(methodIds.get(i).getName().equals(name)){
                    return methodTypes.get(i);
                }
            }
        }

        if(scopeType.equals("Class")){
            Symbol s = lookupType(name);
            return s;
        }

        if(parent != null)
            return parent.getType(name);

        return null;
    }

    public boolean existsId(String name){
        if(debug)
            System.out.println("Sunt in " + scopeType + " caut " + name);
        if(scopeType.equals("Let") || scopeType.equals("Case")){
            if(debug) {
                System.out.println(structId);
                System.out.println(structType);
            }
            if(structId != null)
                if(structId.getName().equals(name)){
                    if(debug)
                        System.out.println(name + " gasit!!");
                    return true;
                }
        }

        if(scopeType.equals("Method")){
            if(debug) {
                System.out.println(methodId);
                System.out.println(methodIds);
                System.out.println(methodTypes);
            }
            if(methodId.name.equals(name)){
                if(debug)
                    System.out.println(name + " gasit!!");
                return true;
            }
            for( Symbol id :methodIds){
                if(id.getName().equals(name)){
                    if(debug)
                        System.out.println(name + " gasit in method ids!!");
                    return true;
                }
            }
        }

        if(scopeType.equals("Class")){
            if(debug) {
                System.out.println(symbols);
                System.out.println(symbolsType);
                System.out.println(fSymbols);
                System.out.println(fTypeSymbols);
            }
            Symbol s = lookup(name);
            if(s != null){
                if(debug)
                    System.out.println(name + " gasit!!");
                return true;
            }
            return false;
        }

        if(parent != null){
            return parent.existsId(name);
        } else {
            if(debug)
                System.out.println("Parent null");
        }
        return false;
    }

    @Override
    public boolean add(Symbol sym) {
        // Reject duplicates in the same scope.
        if (symbols.containsKey(sym.getName()))
            return false;
        
        symbols.put(sym.getName(), sym);
        
        return true;
    }

    public Symbol lookupType(String name) {
        var sym = symbolsType.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookupType(name);

        return null;
    }

    @Override
    public Symbol lookup(String name) {
        var sym = symbols.get(name);
        
        if (sym != null)
            return sym;
        
        if (parent != null)
            return parent.lookup(name);
        
        return null;
    }

    public boolean addF(Touple t) {
        // Reject duplicates in the same scope.
        if (fSymbols.containsKey(t.id.name))
            return false;
        fSymbols.put(t.id.name, t);

        return true;
    }

    public Touple lookupF(String name) {
        var sym = fSymbols.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookupF(name);

        return null;
    }

    public boolean addFType(Touple t) {
        // Reject duplicates in the same scope.
        if (fTypeSymbols.containsKey(t.id.name))
            return false;

        fTypeSymbols.put(t.id.name, t);

        return true;
    }

    public Touple lookupFType(String name) {
        var sym = fTypeSymbols.get(name);

        if (sym != null)
            return sym;

        if (parent != null)
            return parent.lookupFType(name);

        return null;
    }

    public DefaultScope getParent() {
        return parent;
    }
    
    @Override
    public String toString() {
        if(scopeType.equals("Class")) {
            return "{" + symbols.values() + " " + symbolsType.values() + "}"
                    + "{" + fSymbols.values() + " " + fSymbols.values() + "}";
        } else if(scopeType.equals("Method")){
            return methodId + " " + methodIds + " " + methodTypes;
        } else {
            return structId + " " + structType;
        }
    }

}
