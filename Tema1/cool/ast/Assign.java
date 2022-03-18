package cool.ast;

import org.antlr.v4.runtime.Token;

public class Assign extends Expr {
    public String id;
    public Expr expr;

    public Assign(String id, Expr expr, Token token) {
        super(token);
        this.id = id;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
