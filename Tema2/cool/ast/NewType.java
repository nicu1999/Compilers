package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class NewType extends Expr{
    public String Type;
    public Token tokenType;
    public NewType(String Type, ParserRuleContext ctx, Token tokenType, Token token) {
        super(ctx, token);
        this.tokenType = tokenType;
        this.Type = Type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
