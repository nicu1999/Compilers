package cool.ast;

import org.antlr.v4.runtime.Token;

public class Atribute extends Feature {
    public String id;
    public String Type;
    public Expr expr;
    public Atribute(String id, String Type, Expr expr, Token token) {
        super(token);
        this.id = id;
        this.Type = Type;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
