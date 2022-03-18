package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Class1 extends ASTNode {
    public List<Feature> ls;
    public List<String> lt; //daca e mai mult de 1 este un inheritance
    public Token type;
    public Token inheritType;
    public Class1(List<Feature> ls, List<String> lt, ParserRuleContext ctx,
                  Token type, Token inheritType, Token token) {
        super(ctx, token);
        this.type = type;
        this.inheritType = inheritType;
        this.ls = ls;
        this.lt = lt;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
