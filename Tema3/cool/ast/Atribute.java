package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Atribute extends Feature {
    public String id;
    public String Type;
    public Token idToken;
    public Token typeToken;
    public Expr expr;
    public Token exprToken;
    public boolean error = false; //sa nu bage de seama la resolution pass
    public Atribute(String id, String Type, Expr expr, ParserRuleContext ctx,
                    Token idToken,  Token typeToken, Token exprToken, Token token) {
        super(ctx, token);
        this.exprToken = exprToken;
        this.typeToken = typeToken;
        this.idToken = idToken;
        this.id = id;
        this.Type = Type;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
