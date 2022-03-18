package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Id extends Expr {

    public Id(ParserRuleContext ctx, Token token) {
        super(ctx, token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
