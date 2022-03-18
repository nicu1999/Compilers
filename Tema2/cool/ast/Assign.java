package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Assign extends Expr {
    public String id;
    public Expr expr;
    public Token exprToken;
    public Assign(String id, Expr expr, ParserRuleContext ctx, Token exprToken, Token token) {
        super(ctx, token);
        this.exprToken = exprToken;
        this.id = id;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
