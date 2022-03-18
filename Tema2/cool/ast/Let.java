package cool.ast;

import cool.structures.DefaultScope;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Let extends Expr {

    public List<Let_Branch> ls;
    public Expr expr;


    public Let(List<Let_Branch> ls, Expr expr, ParserRuleContext ctx, Token token) {
        super(ctx, token);
        this.ls = ls;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
