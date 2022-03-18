package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public class Formal extends ASTNode {
    public String id;
    public String Type;
    //token-ul de incept e id - ul
    public Token typeToken;
    public Formal(String id, String Type, ParserRuleContext ctx, Token typeToken, Token token) {
        super(ctx, token);
        this.typeToken = typeToken;
        this.id = id;
        this.Type = Type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
