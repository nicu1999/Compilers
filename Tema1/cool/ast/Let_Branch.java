package cool.ast;

import org.antlr.v4.runtime.Token;

public class Let_Branch extends ASTNode{
    public String id;
    public String type;
    public Expr expr;
    public Let_Branch(String id, String type, Expr expr, Token token) {
        super(token);
        this.expr = expr;
        this.type = type;
        this.id = id;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
