package cool.ast;

import cool.structures.Symbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

// Rădăcina ierarhiei de clase reprezentând nodurile arborelui de sintaxă
// abstractă (AST). Singura metodă permite primirea unui visitor.
public abstract class ASTNode {
    public ParserRuleContext ctx;
    public Token token;
    public Symbol returnType;

    public ASTNode(ParserRuleContext ctx, Token token) {
        this.ctx = ctx;
        this.token = token;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }
}

