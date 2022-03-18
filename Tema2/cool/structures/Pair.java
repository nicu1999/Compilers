package cool.structures;

import java.util.ArrayList;

public class Pair {
    public Symbol id;
    public Symbol type;

    public Pair(Symbol id, Symbol type){
        this.id = id;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}