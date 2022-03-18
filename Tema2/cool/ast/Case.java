package cool.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class Case extends Expr {
    public Expr expr;
    public List<Expr> ls;
    public List<String> lid;
    public List<String> lt;
    public List<ParserRuleContext> caseCtx;
    public List<Token> caseTokensId;
    public List<Token> caseTokensType;

    public Case(Expr expr, List<Expr> ls, List<String> lid, List<String> lt, ParserRuleContext ctx,
                List<ParserRuleContext> caseCtx, List<Token> caseTokensId, List<Token> caseTokensType, Token token) {
        super(ctx, token);
        this.caseCtx = caseCtx;
        this.caseTokensId = caseTokensId;
        this.caseTokensType = caseTokensType;
        this.ls = ls;
        this.lid = lid;
        this.lt = lt;
        this.expr = expr;
    }

    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
