package cool.ast;

import org.antlr.v4.runtime.Token;

public class True extends Expr {
    public True(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
