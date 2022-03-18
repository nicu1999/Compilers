package cool.ast;

import cool.parser.*;

public interface ASTVisitor<T> {
    T visit(Program program);
    T visit(Class1 class1);
    T visit(Feature feature);
    T visit(Method method);
    T visit(Atribute atribute);
    T visit(Formal formal);
    T visit(Expr expr);
    T visit(Indisp indisp);
    T visit(Disp disp);
    T visit(If iff);
    T visit(While whilee);
    T visit(Let let);
    T visit(Case casee);
    T visit(IsVoid isVoid);
    T visit(NewType newType);
    T visit(Par par);
    T visit(Braces braces);
    T visit(Neg neg);
    T visit(Id id);
    T visit(Int intt);
    T visit(StringCool stringCool);
    T visit(True truee); //bestie
    T visit(False falsee);
    T visit(Mul mul);
    T visit(Div div);
    T visit(Plus plus);
    T visit(Min min);
    T visit(Less less);
    T visit(LessEq lesseq);
    T visit(Eq eq);
    T visit(Assign assign);
    T visit(Not not);
    T visit(Let_Branch let_branch);
}