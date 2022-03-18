package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Less extends Expr {
    public Expr expr1;
    public Expr expr2;
    public Token t1;
    public Token t2;
    public Less(Expr expr1, Expr expr2, ParserRuleContext ctx, Token t1, Token t2, Token token) {
        super(ctx, token);
        this.t1 = t1;
        this.t2 = t2;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
