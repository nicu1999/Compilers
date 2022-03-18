package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Disp extends Expr {
    public String id;
    public String type;
    public Expr expr;
    public List<Expr> ls;
    public Disp(String id, String type, Expr expr, List<Expr> ls, Token token) {
        super(token);
        this.type = type;
        this.id = id;
        this.expr = expr;
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
