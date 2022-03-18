package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Class1 extends ASTNode {
    public List<Feature> ls;
    public List<String> lt; //daca e mai mult de 1 este un inheritance
    public Class1(List<Feature> ls, List<String> lt, Token token) {
        super(token);
        this.ls = ls;
        this.lt = lt;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
