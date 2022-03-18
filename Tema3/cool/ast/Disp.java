package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Disp extends Expr {
    public String id;
    public String type;
    public Expr expr;
    public List<Expr> ls;
    public Token exprToken;
    public Token staticToken;
    public Token idToken;
    public Disp(String id, String type, Expr expr, List<Expr> ls,
                ParserRuleContext ctx,Token exprToken, Token staticToken, Token typeToken, Token token) {
        super(ctx, token);
        this.exprToken = exprToken;
        this.staticToken = staticToken;
        this.idToken = typeToken;
        this.type = type;
        this.id = id;
        this.expr = expr;
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
