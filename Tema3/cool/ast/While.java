package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class While extends Expr {
    public Expr expr1;
    public Expr expr2;
    public Token tokenE1;
    public Token tokenE2;
    public While(Expr expr1, Expr expr2, ParserRuleContext ctx, Token tokenE1,Token tokenE2, Token token) {
        super(ctx, token);
        this.tokenE1 = tokenE1;
        this.tokenE2 = tokenE2;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
