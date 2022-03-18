package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Program extends ASTNode {
    public List<Class1> ls;
    public Program(List<Class1> ls, ParserRuleContext ctx, Token token) {
        super(ctx, token);
        this.ls = ls;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
