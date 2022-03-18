package cool.ast;

import cool.structures.DefaultScope;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Let_Branch extends ASTNode{
    public String id;
    public String type;
    public Expr expr;
    public Token typeToken;
    public Token tokenE;
    public DefaultScope s;
    public int depth;

    public Let_Branch(String id, String type, Expr expr, ParserRuleContext ctx,
                      Token typeToken,Token tokenE, Token token) {
        super(ctx, token);
        this.tokenE = tokenE;
        this.typeToken = typeToken;
        this.expr = expr;
        this.type = type;
        this.id = id;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
