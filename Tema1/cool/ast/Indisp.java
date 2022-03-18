package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Indisp extends Expr {
    public String id;
    public List<Expr> ls;
    public Indisp(String id, List<Expr> ls, Token token) {
        super(token);
        this.id = id;
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
