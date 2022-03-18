package cool.ast;

import org.antlr.v4.runtime.Token;

public class Int extends Expr {
    public Int(Token token) {
        super(token);
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
