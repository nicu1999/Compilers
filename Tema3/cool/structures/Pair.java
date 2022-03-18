package cool.structures;

import java.util.ArrayList;

public class Pair <T1, T2> {
    public T1 id;
    public T2 type;

    public Pair(T1 id, T2 type){
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