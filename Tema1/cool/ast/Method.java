package cool.ast;

import org.antlr.v4.runtime.Token;

import java.util.List;

public class Method extends Feature {
    public String id;
    public List<Formal> ls;
    public String type;
    public Expr expr;
    public Method(String id, List<Formal> ls, String type, Expr expr, Token token) {
        super(token);
        this.id = id;
        this.ls = ls;
        this.type = type;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
