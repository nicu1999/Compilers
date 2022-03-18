package cool.ast;

import org.antlr.v4.runtime.Token;

public class Min extends Expr {
    public Expr expr1;
    public Expr expr2;
    public Min(Expr expr1, Expr expr2, Token token) {
        super(token);
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
