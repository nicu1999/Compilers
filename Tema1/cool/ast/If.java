package cool.ast;

import org.antlr.v4.runtime.Token;

public class If extends Expr {
    public Expr expr1;
    public Expr expr2;
    public Expr expr3;
    public If(Expr expr1, Expr expr2, Expr expr3, Token token) {
        super(token);
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
