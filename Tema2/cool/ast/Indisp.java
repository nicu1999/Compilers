package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Indisp extends Expr {
    public String id;
    public List<Expr> ls;
    public Token exprToken;
    public Token idToken;
    public Indisp(String id, List<Expr> ls, ParserRuleContext ctx, Token idToken,  Token token) {
        super(ctx, token);
        //this.exprToken = exprToken;
        this.idToken = idToken;
        this.id = id;
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
