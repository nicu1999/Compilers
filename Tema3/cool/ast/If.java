package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class If extends Expr {
    public Expr expr1;
    public Expr expr2;
    public Expr expr3;
    public Token tokenE1;
    public Token tokenE2;
    public Token tokenE3;
    public If(Expr expr1, Expr expr2, Expr expr3, ParserRuleContext ctx,
              Token tokenE1, Token tokenE2, Token tokenE3, Token token) {
        super(ctx, token);
        this.tokenE1 = tokenE1;
        this.tokenE2 = tokenE2;
        this.tokenE3 = tokenE3;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
