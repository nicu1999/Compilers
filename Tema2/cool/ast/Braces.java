package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Braces extends Expr {
    public List<Expr> ls;

    public Braces(List<Expr> ls, ParserRuleContext ctx, Token token) {
        super(ctx, token);
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
