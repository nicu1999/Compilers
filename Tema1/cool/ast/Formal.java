package cool.ast;

import org.antlr.v4.runtime.Token;

public class Formal extends ASTNode {
    public String id;
    public String Type;
    public Formal(String id, String Type, Token token) {
        super(token);
        this.id = id;
        this.Type = Type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
