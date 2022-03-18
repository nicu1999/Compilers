package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Not extends Expr {
    public Expr expr;
    public Token t;
    public Not(Expr expr, ParserRuleContext ctx, Token t, Token token) {
        super(ctx, token);
        this.t = t;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
