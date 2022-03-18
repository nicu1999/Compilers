package cool.compiler;

import cool.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;

import java.beans.Expression;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Compiler {
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

    static String getCtxName(ParserRuleContext ctx) {
        String str = ctx.getClass().getName();
        str = str.substring(str.indexOf("$")+1,str.lastIndexOf("Context"));
        str = str.toLowerCase();
        return str;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file(s) given");
            return;
        }
        
        CoolLexer lexer = null;
        CommonTokenStream tokenStream = null;
        CoolParser parser = null;
        ParserRuleContext globalTree = null;
        
        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;
        
        // Parse each input file and build one big parse tree out of
        // individual parse trees.
        for (var fileName : args) {
            var input = CharStreams.fromFileName(fileName);
            
            // Lexer
            if (lexer == null)
                lexer = new CoolLexer(input);
            else
                lexer.setInputStream(input);

            // Token stream
            if (tokenStream == null)
                tokenStream = new CommonTokenStream(lexer);
            else
                tokenStream.setTokenSource(lexer);
                

            // Test lexer only.
            /*tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });*/

            
            // Parser
            if (parser == null)
                parser = new CoolParser(tokenStream);
            else
                parser.setTokenStream(tokenStream);
            
            // Customized error listener, for including file names in error
            // messages.
            var errorListener = new BaseErrorListener() {
                public boolean errors = false;
                
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line, int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    String newMsg = "\"" + new File(fileName).getName() + "\", line " +
                                        line + ":" + (charPositionInLine + 1) + ", ";
                    
                    Token token = (Token)offendingSymbol;
                    if (token.getType() == CoolLexer.ERROR)
                        newMsg += "Lexical error: " + token.getText();
                    else
                        newMsg += "Syntax error: " + msg;
                    
                    System.err.println(newMsg);
                    errors = true;
                }
            };
            
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            
            // Actual parsing
            var tree = parser.program();
            if (globalTree == null)
                globalTree = tree;
            else
                // Add the current parse tree's children to the global tree.
                for (int i = 0; i < tree.getChildCount(); i++)
                    globalTree.addAnyChild(tree.getChild(i));
                    
            // Annotate class nodes with file names, to be used later
            // in semantic error messages.
            for (int i = 0; i < tree.getChildCount(); i++) {
                var child = tree.getChild(i);
                // The only ParserRuleContext children of the program node
                // are class nodes.
                if (child instanceof ParserRuleContext)
                    fileNames.put(child, fileName);
            }
            
            // Record any lexical or syntax errors.
            lexicalSyntaxErrors |= errorListener.errors;
        }

        // Stop before semantic analysis phase, in case errors occurred.
        if (lexicalSyntaxErrors) {
            System.err.println("Compilation halted");
            return;
        }
        
        // TODO Print tree

        var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>(){

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new Id(ctx.getStart());
            }

            @Override
            public ASTNode visitInt(CoolParser.IntContext ctx) {
                return new Int(ctx.getStart());
            }

            @Override
            public ASTNode visitString(CoolParser.StringContext ctx) {
                return new StringCool(ctx.getStart());
            }

            @Override
            public ASTNode visitTrue(CoolParser.TrueContext ctx) {
                return new True(ctx.getStart());
            }

            @Override
            public ASTNode visitFalse(CoolParser.FalseContext ctx) {
                return new False(ctx.getStart());
            }

            @Override
            public ASTNode visitNot(CoolParser.NotContext ctx) {
                return new Not((Expr) visitExpression(ctx.expr()),
                                ctx.getStart());
            }

            @Override
            public ASTNode visitAssign(CoolParser.AssignContext ctx) {
                return new Assign(  ctx.ID().getText(),
                                    (Expr) visitExpression(ctx.expr()),
                                    ctx.getStart());
            }

            @Override
            public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
                return new IsVoid((Expr) visitExpression(ctx.expr()), ctx.getStart());
            }

            @Override
            public ASTNode visitNew_type(CoolParser.New_typeContext ctx) {
                return new NewType(ctx.TYPE().getText(), ctx.getStart());
            }

            @Override
            public ASTNode visitMul(CoolParser.MulContext ctx) {
                return new Mul( (Expr) visitExpression(ctx.expr(0)),
                                (Expr) visitExpression(ctx.expr(1)),
                                ctx.getStart());
            }

            @Override
            public ASTNode visitDiv(CoolParser.DivContext ctx) {
                return new Div( (Expr) visitExpression(ctx.expr(0)),
                                (Expr) visitExpression(ctx.expr(1)),
                                ctx.getStart());
            }

            @Override
            public ASTNode visitPlus(CoolParser.PlusContext ctx) {
                return new Plus( (Expr) visitExpression(ctx.expr(0)),
                                 (Expr) visitExpression(ctx.expr(1)),
                                 ctx.getStart());
            }

            @Override
            public ASTNode visitMin(CoolParser.MinContext ctx) {
                return new Min( (Expr) visitExpression(ctx.expr(0)),
                                (Expr) visitExpression(ctx.expr(1)),
                                ctx.getStart());
            }

            @Override
            public ASTNode visitLess(CoolParser.LessContext ctx) {
                return new Less( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitLesseq(CoolParser.LesseqContext ctx) {
                return new LessEq( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitEq(CoolParser.EqContext ctx) {
                return new Eq( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitBrace(CoolParser.BraceContext ctx) {
                List<Expr> ls =  new ArrayList<>();
                for (CoolParser.ExprContext expr : ctx.expr()) {
                    ls.add((Expr)visitExpression(expr));
                }
                return new Braces(ls, ctx.getStart());
            }

            @Override
            public ASTNode visitPar(CoolParser.ParContext ctx) {
                return new Par( (Expr) visitExpression(ctx.expr()), ctx.getStart());
            }

            @Override
            public ASTNode visitCase(CoolParser.CaseContext ctx) {
                List<Expr> ls =  new ArrayList<>();
                List<String> lid =  new ArrayList<>(); //list id
                List<String> lt =  new ArrayList<>(); // list type
                Expr e = (Expr) visitExpression(ctx.expr(0));
                for(int i = 1; i < ctx.expr().size(); i++){
                    ls.add((Expr)visitExpression(ctx.expr(i)));
                    lid.add(ctx.ID(i - 1).getText());
                    lt.add(ctx.TYPE(i - 1).getText());
                }
                return new Case(e, ls, lid, lt, ctx.getStart());
            }

            @Override
            public ASTNode visitLet(CoolParser.LetContext ctx) {
                List<Let_Branch> ls = new ArrayList<>();
                for (CoolParser.Let_branchContext let_b : ctx.let_branch()) {
                    ls.add((Let_Branch)visitLet_branch(let_b));
                }
                return new Let(ls, (Expr)visitExpression(ctx.expr()), ctx.getStart());
            }

            @Override
            public ASTNode visitLet_branch(CoolParser.Let_branchContext ctx) {
                if(ctx.expr() != null)//problemele apar cand e un singur expr?, arrays sunt ok
                    return new Let_Branch( ctx.ID().getText() ,
                            ctx.TYPE().getText(),
                            (Expr)visitExpression(ctx.expr()),
                            ctx.getStart());
                return new Let_Branch( ctx.ID().getText(),
                        ctx.TYPE().getText(),
                        null,
                        ctx.getStart());
            }

            @Override
            public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                return new While( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitIf(CoolParser.IfContext ctx) {
                return new If( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        (Expr) visitExpression(ctx.expr(2)),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                return new Formal( ctx.ID().getText(), ctx.TYPE().getText(), ctx.getStart());
            }

            @Override
            public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                //System.out.println(ctx.getText());
                List<Formal> ls = new ArrayList<>();
                String id = ctx.ID().getText();
                for (CoolParser.FormalContext form : ctx.formal()) {
                    ls.add((Formal) visitFormal(form));
                }
                return new Method(id, ls, ctx.TYPE().getText(), (Expr) visitExpression(ctx.expr()),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitAtribute(CoolParser.AtributeContext ctx) {
                if(ctx.expr() == null){
                    return new Atribute(
                            ctx.ID().getText(),
                            ctx.TYPE().getText(),
                            null,
                            ctx.getStart());
                }
                return new Atribute(
                            ctx.ID().getText(),
                            ctx.TYPE().getText(),
                            (Expr)visitExpression(ctx.expr()),
                            ctx.getStart());
            }

            @Override
            public ASTNode visitClass1(CoolParser.Class1Context ctx) {
                List<Feature> ls = new ArrayList<>();
                List<String> lt = new ArrayList<>();

                for (CoolParser.FeatureContext f: ctx.value) {
                    //System.out.println(getCtxName(f));
                    if(getCtxName(f).equals("method")){
                        //System.out.println("m");
                        ls.add( (Method) visitMethod( (CoolParser.MethodContext) f));
                    }
                    if(getCtxName(f).equals("atribute")){
                        //System.out.println("a");
                        ls.add( (Atribute) visitAtribute( (CoolParser.AtributeContext) f));
                    }
                }

                for (TerminalNode terminalNode : ctx.TYPE()) {
                    lt.add(terminalNode.getText());
                }

                return new Class1(ls, lt, ctx.getStart());
            }

            @Override
            public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                List<Class1> ls = new ArrayList<>();
                for (CoolParser.Class1Context f : ctx.class1()) {
                    ls.add( (Class1) visitClass1(f));
                }
                return new Program(ls, ctx.getStart());
            }

            @Override
            public ASTNode visitDisp(CoolParser.DispContext ctx) {
                List<Expr> ls = new ArrayList<>();
                String id = ctx.ID().getText();
                String type = null;
                if ( ctx.TYPE() != null )
                    type = ctx.TYPE().getText();
                Expr expr = (Expr) visitExpression(ctx.pre);
                for(int i = 0; i < ctx.expr().size(); i++){
                    ls.add((Expr) visitExpression(ctx.expr(i)));
                }
                return new Disp(id, type,  expr, ls, ctx.getStart());
            }

            @Override
            public ASTNode visitIndisp(CoolParser.IndispContext ctx) {
                List<Expr> ls = new ArrayList<>();
                String id =ctx.ID().getText();
                for(CoolParser.ExprContext e: ctx.expr()){
                    ls.add((Expr) visitExpression(e));
                }
                return new Indisp(id, ls, ctx.getStart());
            }

            @Override
            public ASTNode visitNeg(CoolParser.NegContext ctx) {
                return new Neg((Expr) visitExpression(ctx.expr()), ctx.getStart());
            }

            public ASTNode visitExpression(CoolParser.ExprContext ctx) {
                String label = getCtxName(ctx);
                if(label.equals("indisp"))
                    return visitIndisp( (CoolParser.IndispContext) ctx);
                if(label.equals("disp"))
                    return visitDisp( (CoolParser.DispContext) ctx);
                if(label.equals("if"))
                    return visitIf( (CoolParser.IfContext) ctx);
                if(label.equals("let"))
                    return visitLet( (CoolParser.LetContext) ctx);
                if(label.equals("case"))
                    return visitCase( (CoolParser.CaseContext) ctx);
                if(label.equals("isvoid"))
                    return visitIsvoid( (CoolParser.IsvoidContext) ctx);
                if(label.equals("new_type"))
                    return visitNew_type( (CoolParser.New_typeContext) ctx);
                if(label.equals("par"))
                    return visitPar( (CoolParser.ParContext) ctx);
                if(label.equals("neg"))
                    return visitNeg( (CoolParser.NegContext) ctx);
                if(label.equals("id"))
                    return visitId( (CoolParser.IdContext) ctx);
                if(label.equals("int"))
                    return visitInt( (CoolParser.IntContext) ctx);
                if(label.equals("string"))
                    return visitString( (CoolParser.StringContext) ctx);
                if(label.equals("true"))
                    return visitTrue( (CoolParser.TrueContext) ctx);
                if(label.equals("false"))
                    return visitFalse( (CoolParser.FalseContext) ctx);
                if(label.equals("mul"))
                    return visitMul( (CoolParser.MulContext) ctx);
                if(label.equals("div"))
                    return visitDiv( (CoolParser.DivContext) ctx);
                if(label.equals("plus"))
                    return visitPlus( (CoolParser.PlusContext) ctx);
                if(label.equals("min"))
                    return visitMin( (CoolParser.MinContext) ctx);
                if(label.equals("less"))
                    return visitLess( (CoolParser.LessContext) ctx);
                if(label.equals("lesseq"))
                    return visitLesseq( (CoolParser.LesseqContext) ctx);
                if(label.equals("eq"))
                    return visitEq( (CoolParser.EqContext) ctx);
                if(label.equals("assign"))
                    return visitAssign( (CoolParser.AssignContext) ctx);
                if(label.equals("not"))
                    return visitNot( (CoolParser.NotContext) ctx);
                if(label.equals("while"))
                    return visitWhile( (CoolParser.WhileContext) ctx);
                if(label.equals("brace"))
                    return visitBrace( (CoolParser.BraceContext) ctx);
                return null;
            }
        };

        var ast = astConstructionVisitor.visit(globalTree);

        var printVisitor = new ASTVisitor<Void>() {
            int indent = 0;

            @Override
            public Void visit(StringCool stringCool) {
                String str = stringCool.token.getText();
                printIndent(str.substring(1, str.length() - 1));
                return null;
            }

            @Override
            public Void visit(True truee) {
                printIndent("true");
                return null;
            }

            @Override
            public Void visit(False falsee) {
                printIndent("false");
                return null;
            }

            @Override
            public Void visit(Id id) {
                printIndent(id.token.getText());
                return null;
            }

            @Override
            public Void visit(Int intt) {
                printIndent(intt.token.getText());
                return null;
            }

            @Override
            public Void visit(Not not) {
                printIndent("not");
                indent++;
                not.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Neg neg) {
                printIndent("~");
                indent++;
                neg.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Assign assign) {
                printIndent("<-");
                indent++;
                printIndent(assign.id);
                //assign.id.accept(this);
                assign.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(IsVoid isVoid) {
                printIndent("isvoid");
                indent++;
                isVoid.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(NewType newType) {
                printIndent("new");
                indent++;
                printIndent(newType.Type);
                indent--;
                return null;
            }

            @Override
            public Void visit(Mul mul) {
                printIndent("*");
                indent++;
                mul.expr1.accept(this);
                mul.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Div div) {
                printIndent("/");
                indent++;
                div.expr1.accept(this);
                div.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Plus plus) {
                printIndent("+");
                indent++;
                plus.expr1.accept(this);
                plus.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Min min) {
                printIndent("-");
                indent++;
                min.expr1.accept(this);
                min.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Less less) {
                printIndent("<");
                indent++;
                less.expr1.accept(this);
                less.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(LessEq lesseq) {
                printIndent("<=");
                indent++;
                lesseq.expr1.accept(this);
                lesseq.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Eq eq) {
                printIndent("=");
                indent++;
                eq.expr1.accept(this);
                eq.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Par par) {
                par.expr.accept(this);
                return null;
            }

            @Override
            public Void visit(Braces braces) {
                printIndent("block");
                indent++;
                for(Expr e: braces.ls)
                    e.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Case casee) {
                printIndent("case");
                indent++;
                casee.expr.accept(this);
                for(int i = 0; i < casee.lid.size(); i++){
                    printIndent("case branch");
                    indent++;
                    printIndent(casee.lid.get(i));
                    printIndent(casee.lt.get(i));
                    casee.ls.get(i).accept(this);
                    indent--;
                }
                indent--;
                return null;
            }

            @Override
            public Void visit(Let let) {
                printIndent("let");
                indent++;
                for(Let_Branch lb : let.ls){
                    lb.accept(this);
                }
                let.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Let_Branch let_branch) {
                printIndent("local");
                indent++;
                printIndent(let_branch.id);
                printIndent(let_branch.type);
                if(let_branch.expr != null)
                    let_branch.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(While whilee) {
                printIndent("while");
                indent++;
                whilee.expr1.accept(this);
                whilee.expr2.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(If iff) {
                printIndent("if");
                indent++;
                iff.expr1.accept(this);
                iff.expr2.accept(this);
                iff.expr3.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Program program) {
                printIndent("program");
                indent++;
                for (Class1 l : program.ls) {
                    l.accept(this);
                }
                indent--;
                return null;
            }

            @Override
            public Void visit(Class1 class1) {
                printIndent("class");
                indent++;
                if(class1.lt.size() == 2){
                    printIndent(class1.lt.get(0));
                    printIndent(class1.lt.get(1));
                } else
                {
                    printIndent(class1.lt.get(0));
                }
                for (Feature l : class1.ls) {
                    l.accept(this);
                }

                indent--;
                return null;
            }

            @Override
            public Void visit(Feature feature) { //nu
                return null;
            }

            @Override
            public Void visit(Method method) {
                printIndent("method");
                indent++;
                printIndent(method.id);
                for(Formal f : method.ls) {
                    f.accept(this);
                }
                printIndent(method.type);
                method.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Atribute atribute) {
                printIndent("attribute");
                indent++;
                printIndent(atribute.id);
                //atribute.id.accept(this);
                printIndent(atribute.Type);
                if(atribute.expr != null)
                    atribute.expr.accept(this);
                indent--;
                return null;
            }

            @Override
            public Void visit(Formal formal) {
                printIndent("formal");
                indent++;
                printIndent(formal.id);
                //formal.id.accept(this);
                printIndent(formal.Type);
                indent--;
                return null;
            }

            @Override
            public Void visit(Expr expr) {  //nu
                return null;
            }

            @Override
            public Void visit(Indisp indisp) {
                printIndent("implicit dispatch");
                indent++;
                printIndent(indisp.id);
                for(Expr e : indisp.ls) {
                    e.accept(this);
                }
                indent--;
                return null;
            }

            @Override
            public Void visit(Disp disp) {
                printIndent(".");
                indent++;
                if(disp.expr!= null)
                    disp.expr.accept(this);
                if(disp.type != null)
                    printIndent(disp.type);
                if(disp.id != null)
                    printIndent(disp.id);
                for (int i = 1; i < disp.ls.size() ; i++) {
                    disp.ls.get(i).accept(this);
                }
                indent--;
                return null;
            }

            void printIndent(String str) {
                for (int i = 0; i < indent; i++)
                    System.out.print("  ");
                System.out.println(str);
            }
        };

        ast.accept(printVisitor);
    }
}
