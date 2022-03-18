package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Case extends Expr {
    public Expr expr;
    public List<Expr> ls;
    public List<String> lid;
    public List<String> lt;

    public Case(Expr expr, List<Expr> ls, List<String> lid, List<String> lt, Token token) {
        super(token);
        this.ls = ls;
        this.lid = lid;
        this.lt = lt;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
