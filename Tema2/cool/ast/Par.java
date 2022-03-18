package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Par extends Expr {
    public Expr expr;
    public Par(Expr expr, ParserRuleContext ctx, Token token) {
        super(ctx, token);
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
