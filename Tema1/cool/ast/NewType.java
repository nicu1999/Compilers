package cool.ast;

import org.antlr.v4.runtime.Token;

public class NewType extends Expr{
    public String Type;
    public NewType(String Type ,Token token) {
        super(token);
        this.Type = Type;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
