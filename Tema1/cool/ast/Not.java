package cool.ast;

import org.antlr.v4.runtime.Token;

public class Not extends Expr {
    public Expr expr;
    public Not(Expr expr,Token token) {
        super(token);
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
