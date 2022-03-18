package cool.structures;

import java.util.ArrayList;

public class Touple {
    public Symbol id;
    public ArrayList<Symbol> args;

    public Touple(Symbol id, ArrayList<Symbol> args){
        this.id = id;
        this.args = args;
    }

    @Override
    public String toString() {
        return "Touple{" +
                "id=" + id +
                ", args=" + args +
                '}';
    }
}