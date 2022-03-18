package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Method extends Feature {
    public String id;
    public List<Formal> ls;
    public String type;
    public Expr expr;
    public Token typeToken;
    public Token exprToken;
    public Method(String id, List<Formal> ls, String type, Expr expr, ParserRuleContext ctx,
                  Token typeToken, Token exprToken, Token token) {
        super(ctx, token);
        this.exprToken = exprToken;
        this.typeToken = typeToken;
        this.id = id;
        this.ls = ls;
        this.type = type;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
