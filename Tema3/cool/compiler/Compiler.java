package cool.compiler;

import cool.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import cool.lexer.*;
import cool.parser.*;
import cool.structures.*;

import java.io.*;
import java.util.*;


public class Compiler {
    static String getCtxName(ParserRuleContext ctx) {
        String str = ctx.getClass().getName();
        str = str.substring(str.indexOf("$")+1,str.lastIndexOf("Context"));
        str = str.toLowerCase();
        return str;
    }
    // Annotates class nodes with the names of files where they are defined.
    public static ParseTreeProperty<String> fileNames = new ParseTreeProperty<>();

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
        
        // Populate global scope.
        SymbolTable.defineBasicClasses();

        var astConstructionVisitor = new CoolParserBaseVisitor<ASTNode>(){

            @Override
            public ASTNode visitId(CoolParser.IdContext ctx) {
                return new Id(ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitInt(CoolParser.IntContext ctx) {
                return new Int(ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitString(CoolParser.StringContext ctx) {
                return new StringCool(ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitTrue(CoolParser.TrueContext ctx) {
                return new True(ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitFalse(CoolParser.FalseContext ctx) {
                return new False(ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitNot(CoolParser.NotContext ctx) {
                return new Not((Expr) visitExpression(ctx.expr()), ctx, ctx.expr().start,
                        ctx.getStart());
            }

            @Override
            public ASTNode visitAssign(CoolParser.AssignContext ctx) {
                return new Assign(  ctx.ID().getText(),
                        (Expr) visitExpression(ctx.expr()), ctx,
                        ctx.expr().getStart(),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitIsvoid(CoolParser.IsvoidContext ctx) {
                return new IsVoid((Expr) visitExpression(ctx.expr()), ctx,  ctx.expr().start, ctx.getStart());
            }

            @Override
            public ASTNode visitNew_type(CoolParser.New_typeContext ctx) {
                return new NewType(ctx.TYPE().getText(), ctx, ctx.TYPE().getSymbol(), ctx.getStart());
            }

            @Override
            public ASTNode visitMul(CoolParser.MulContext ctx) {
                return new Mul( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitDiv(CoolParser.DivContext ctx) {
                return new Div( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitPlus(CoolParser.PlusContext ctx) {
                return new Plus( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitMin(CoolParser.MinContext ctx) {
                return new Min( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitLess(CoolParser.LessContext ctx) {
                return new Less( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitLesseq(CoolParser.LesseqContext ctx) {
                return new LessEq( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.getStart());
            }

            @Override
            public ASTNode visitEq(CoolParser.EqContext ctx) {
                return new Eq( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).start, ctx.expr(1).start, ctx.EQ().getSymbol(), ctx.getStart());
            }

            @Override
            public ASTNode visitBrace(CoolParser.BraceContext ctx) {
                List<Expr> ls =  new ArrayList<>();
                for (CoolParser.ExprContext expr : ctx.expr()) {
                    ls.add((Expr)visitExpression(expr));
                }
                return new Braces(ls, ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitPar(CoolParser.ParContext ctx) {
                return new Par( (Expr) visitExpression(ctx.expr()), ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitCase(CoolParser.CaseContext ctx) {
                List<Expr> ls =  new ArrayList<>();
                List<String> lid =  new ArrayList<>(); //list id
                List<String> lt =  new ArrayList<>(); // list type
                List<ParserRuleContext> caseCtx = new ArrayList<>();
                List<Token> caseTokensId = new ArrayList<>();
                List<Token> caseTokensType = new ArrayList<>();
                Expr e = (Expr) visitExpression(ctx.expr(0));
                for(int i = 1; i < ctx.expr().size(); i++){
                    ls.add((Expr)visitExpression(ctx.expr(i)));
                    lid.add(ctx.ID(i - 1).getText());
                    lt.add(ctx.TYPE(i - 1).getText());
                    caseCtx.add(ctx.expr(i - 1));
                    caseTokensId.add(ctx.ID(i - 1).getSymbol());
                    caseTokensType.add(ctx.TYPE(i - 1).getSymbol());
                }
                return new Case(e, ls, lid, lt, ctx, caseCtx, caseTokensId, caseTokensType, ctx.getStart());
            }

            @Override
            public ASTNode visitLet(CoolParser.LetContext ctx) {
                List<Let_Branch> ls = new ArrayList<>();
                for (CoolParser.Let_branchContext let_b : ctx.let_branch()) {
                    ls.add((Let_Branch)visitLet_branch(let_b));
                }
                return new Let(ls, (Expr)visitExpression(ctx.expr()), ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitLet_branch(CoolParser.Let_branchContext ctx) {
                if(ctx.expr() != null)//problemele apar cand e un singur expr?, arrays sunt ok
                    return new Let_Branch( ctx.ID().getText() ,
                            ctx.TYPE().getText(),
                            (Expr)visitExpression(ctx.expr()),
                            ctx, ctx.TYPE().getSymbol(), ctx.expr().getStart(), ctx.getStart());
                return new Let_Branch( ctx.ID().getText(),
                        ctx.TYPE().getText(),
                        null,
                        ctx,ctx.TYPE().getSymbol(), null, ctx.getStart());
            }

            @Override
            public ASTNode visitWhile(CoolParser.WhileContext ctx) {
                return new While( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        ctx, ctx.expr(0).getStart(), ctx.expr(1).getStart(), ctx.getStart());
            }

            @Override
            public ASTNode visitIf(CoolParser.IfContext ctx) {
                return new If( (Expr) visitExpression(ctx.expr(0)),
                        (Expr) visitExpression(ctx.expr(1)),
                        (Expr) visitExpression(ctx.expr(2)),
                        ctx, ctx.expr(0).getStart(),
                        ctx.expr(1).getStart(), ctx.expr(2).getStart(),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitFormal(CoolParser.FormalContext ctx) {
                return new Formal( ctx.ID().getText(), ctx.TYPE().getText(),
                        ctx, ctx.TYPE().getSymbol() ,ctx.getStart());
            }

            @Override
            public ASTNode visitMethod(CoolParser.MethodContext ctx) {
                List<Formal> ls = new ArrayList<>();
                String id = ctx.ID().getText();
                for (CoolParser.FormalContext form : ctx.formal()) {
                    ls.add((Formal) visitFormal(form));
                }
                return new Method(id, ls, ctx.TYPE().getText(), (Expr) visitExpression(ctx.expr()),
                        ctx, ctx.TYPE().getSymbol(), ctx.expr().getStart(), ctx.getStart());
            }

            @Override
            public ASTNode visitAtribute(CoolParser.AtributeContext ctx) {
                if(ctx.expr() == null){
                    return new Atribute(
                            ctx.ID().getText(),
                            ctx.TYPE().getText(),
                            null, ctx,
                            ctx.ID().getSymbol(),
                            ctx.TYPE().getSymbol(),
                            null,
                            ctx.getStart());
                }
                return new Atribute(
                        ctx.ID().getText(),
                        ctx.TYPE().getText(),
                        (Expr)visitExpression(ctx.expr()),
                        ctx,
                        ctx.ID().getSymbol(),
                        ctx.TYPE().getSymbol(),
                        ctx.expr().getStart(),
                        ctx.getStart());
            }

            @Override
            public ASTNode visitClass1(CoolParser.Class1Context ctx) {
                List<Feature> ls = new ArrayList<>();
                List<String> lt = new ArrayList<>();

                for (CoolParser.FeatureContext f: ctx.value) {
                    if(getCtxName(f).equals("method")){
                        ls.add( (Method) visitMethod( (CoolParser.MethodContext) f));
                    }
                    if(getCtxName(f).equals("atribute")){
                        ls.add( (Atribute) visitAtribute( (CoolParser.AtributeContext) f));
                    }
                }

                for (TerminalNode terminalNode : ctx.TYPE()) {
                    lt.add(terminalNode.getText());
                }

                Token type, inheritedType = null;
                type = ctx.TYPE(0).getSymbol();
                if(ctx.TYPE().size() > 1){
                    inheritedType = ctx.TYPE(1).getSymbol();
                }
                return new Class1(ls, lt, ctx, type, inheritedType, ctx.getStart());
            }

            @Override
            public ASTNode visitProgram(CoolParser.ProgramContext ctx) {
                List<Class1> ls = new ArrayList<>();
                for (CoolParser.Class1Context f : ctx.class1()) {
                    ls.add( (Class1) visitClass1(f));
                }
                return new Program(ls, ctx, ctx.getStart());
            }

            @Override
            public ASTNode visitDisp(CoolParser.DispContext ctx) {
                List<Expr> ls = new ArrayList<>();
                String id = ctx.ID().getText();
                String type = null;
                Token typeSymbol = null;
                if ( ctx.TYPE() != null ) {
                    type = ctx.TYPE().getText();
                    typeSymbol = ctx.TYPE().getSymbol();
                }
                Expr expr = (Expr) visitExpression(ctx.pre);
                for(int i = 0; i < ctx.expr().size(); i++){
                    ls.add((Expr) visitExpression(ctx.expr(i)));
                }
                return new Disp(id, type,  expr, ls, ctx, ctx.expr(0).getStart(),
                        typeSymbol, ctx.ID().getSymbol(), ctx.getStart());
            }

            @Override
            public ASTNode visitIndisp(CoolParser.IndispContext ctx) {
                List<Expr> ls = new ArrayList<>();
                String id = ctx.ID().getText();
                for(CoolParser.ExprContext e: ctx.expr()){
                    ls.add((Expr) visitExpression(e));
                }
                return new Indisp(id, ls, ctx, ctx.ID().getSymbol(), ctx.getStart());
            }

            @Override
            public ASTNode visitNeg(CoolParser.NegContext ctx) {
                return new Neg((Expr) visitExpression(ctx.expr()), ctx, ctx.expr().start, ctx.getStart());
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

        DefaultScope classScope;

        var definitionPassVisitor = new ASTVisitor<Type>(){
            DefaultScope classScope;
            DefaultScope methodScope;
            DefaultScope structScope; //let sau case

            @Override
            public Type visit(StringCool stringCool) {

                SymbolTable.addStringFinal(stringCool.token.getText()
                        .substring(1, stringCool.token.getText().length() - 1));

                return null;
            }

            @Override
            public Type visit(True truee) {
                return null;
            }

            @Override
            public Type visit(False falsee) {
                return null;
            }

            @Override
            public Type visit(Id id) {
                if(structScope != null)
                    id.s = structScope;
                else if( methodScope != null)
                    id.s = methodScope;
                else {
                    id.s = classScope;
                }
                return null;
            }

            @Override
            public Type visit(Int intt) {
                SymbolTable.addInteger(Integer.parseInt(intt.token.getText()));
                return null;
            }

            @Override
            public Type visit(Not not) {
                not.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(Neg neg) {
                neg.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(Assign assign) {
                if(structScope != null)
                    assign.s = structScope;
                else if( methodScope != null)
                    assign.s = methodScope;
                else {
                    assign.s = classScope;
                }
                assign.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(IsVoid isVoid) {
                isVoid.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(NewType newType) {
                return null;
            }

            @Override
            public Type visit(Mul mul) {
                mul.expr1.accept(this);
                mul.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Div div) {
                div.expr1.accept(this);
                div.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Plus plus) {
                plus.expr1.accept(this);
                plus.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Min min) {
                min.expr1.accept(this);
                min.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Less less) {
                less.expr1.accept(this);
                less.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(LessEq lesseq) {
                lesseq.expr1.accept(this);
                lesseq.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Eq eq) {
                eq.expr1.accept(this);
                eq.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(Par par) {
                par.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(Braces braces) {
                for(Expr e: braces.ls)
                    e.accept(this);
                return null;
            }

            @Override
            public Type visit(Case casee) {//pt fiecare case branche se face un scope

                //System.out.println("Ultimul inde let/case " +  casee.s.getLetOffset());


                structScope = new DefaultScope(structScope, "Struct");
                //casee.s = structScope;
                casee.expr.accept(this);
                for(int i = 0; i < casee.lid.size(); i++){
                    structScope = new DefaultScope(structScope, "Case");

                    if(casee.lid.get(i).equals("self")){
                    }else if(casee.lt.get(i).equals("SELF_TYPE")){
                    }

                    structScope.structId = new Symbol(casee.lid.get(i));
                    structScope.structType = new Symbol(casee.lt.get(i));

                    casee.ls.get(i).accept(this);

                    //System.out.println("Ultimul inde let/case " + structScope.getLetOffset());

                    structScope = structScope.getParent();
                }
                structScope = structScope.getParent();
                return null;
            }

            @Override
            public Type visit(Let let) {//pt fiecare let_branch se creeaza un scope imbricat
                int depth = 0;
                for(Let_Branch lb : let.ls){
                    lb.depth = depth;
                    let.maxDepth = depth;
                    lb.accept(this);

                    boolean error = false;
                    structScope = new DefaultScope(structScope, "Let");
                    structScope.letOffset = -(4 + 4 * depth);
                    depth++;
                    if(lb.id.equals("self")){
                        error = true;
                    }

                    if(!error){
                        structScope.structId = new Symbol(lb.id);
                        structScope.structType = new Symbol(lb.type);
                    }

                    lb.s = structScope;
                }

                for(Let_Branch lb : let.ls){
                    lb.s.full_ofset = 4 * depth;
                }

                let.expr.accept(this);

                for(int i = 0; i < depth; i++){
                    structScope = structScope.getParent();
                }
                return null;
            }

            @Override
            public Type visit(Let_Branch let_branch) {
                if(let_branch.expr != null)
                    let_branch.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(While whilee) {
                whilee.expr1.accept(this);
                whilee.expr2.accept(this);
                return null;
            }

            @Override
            public Type visit(If iff) {
                iff.expr1.accept(this);
                iff.expr2.accept(this);
                iff.expr3.accept(this);
                return null;
            }

            @Override
            public Type visit(Program program) {
                for (Class1 l : program.ls) {
                    l.accept(this);
                }
                return null;
            }

            @Override
            public Type visit(Class1 class1) {
                boolean error = false;
                classScope = new DefaultScope(null);
                String name = Helper.setClassName(class1);
                String parent = Helper.setParentName(class1);

                if(name.equals("SELF_TYPE")){
                    error = true;
                }

                ClassNode node = SymbolTable.lookupClass(name);
                if(node != null) {
                    error = true;
                }

                if(parent.equals("SELF_TYPE")){
                    parent = null;
                }

                var nodeParent = SymbolTable.lookupClass(parent);
                if(nodeParent != null)
                    if(!nodeParent.inheritable){
                        error = true;
                    }

                ClassNode c = new ClassNode(name, parent, classScope);//parent "name" not actual parent
                if(!error)
                    SymbolTable.addClassInTable(c);

                classScope.addCurrentClass(c);

                SymbolTable.addScopeInTable(c, classScope);

                for (Feature l : class1.ls) {
                    l.accept(this);
                }

                return null;
            }

            @Override
            public Type visit(Feature feature) { //nu
                return null;
            }

            @Override
            public Type visit(Method method) {
                boolean errorId = false;
                boolean errorType = false;

                methodScope = new DefaultScope(classScope, "Method");
                Symbol sId = new Symbol(method.id);
                methodScope.methodId = sId;

                ArrayList<Symbol> argsId = new ArrayList<>();
                ArrayList<Symbol> argsTypeId = new ArrayList<>();
                argsTypeId.add(new Symbol(method.type));

                for(Formal f : method.ls){
                    if(f.id.equals("self")){
                        errorId = true;
                    }

                    if(f.Type.equals("SELF_TYPE")){
                        errorId = true; //WILL PRODUCE BUGS nr(id) != nr(Types)
                    }

                    for(Symbol sym : argsId){
                        if(sym.getName().equals(f.id)){
                            errorId = true;
                        }
                    }
                    if(!errorId) {
                        argsId.add(new Symbol(f.id));
                        if(!errorType)
                            argsTypeId.add(new Symbol(f.Type));
                    }
                    errorId = false;
                    errorType = false;
                }


                Touple tid = new Touple(sId, argsId);
                Touple ttype = new Touple(sId, argsTypeId);

                methodScope.methodIds = argsId;
                methodScope.methodTypes = new ArrayList<>(argsTypeId.subList(1, argsTypeId.size()));

                if(!classScope.addF(tid)){
                }
                classScope.addFType(ttype);

                for(Formal f : method.ls) {
                    f.accept(this);
                }
                structScope = new DefaultScope(methodScope, "Struct");
                method.expr.accept(this);
                return null;
            }

            @Override
            public Type visit(Atribute atribute) {

                if(atribute.id.equals("self")){
                    atribute.error = true;
                } else if(classScope.symbols.get(atribute.id) != null){ //redefinit "local"
                    atribute.error = true;
                }
                classScope.add(new Symbol(atribute.id));
                classScope.symbolsType.put(atribute.id, new Symbol(atribute.Type));
                if(atribute.expr != null) {
                    atribute.expr.accept(this);
                }
                return null;
            }

            @Override
            public Type visit(Formal formal) {
                return null;
            }

            @Override
            public Type visit(Expr expr) {  //nu
                return null;
            }

            @Override
            public Type visit(Indisp indisp) {
                if(structScope != null)
                    indisp.s = structScope;
                else if( methodScope != null)
                    indisp.s = methodScope;
                else {
                    indisp.s = classScope;
                }
                for(Expr e : indisp.ls) {
                    e.accept(this);
                }
                return null;
            }

            @Override
            public Type visit(Disp disp) {
                if(structScope != null)
                    disp.s = structScope;
                else if( methodScope != null)
                    disp.s = methodScope;
                else {
                    disp.s = classScope;
                }
                if(disp.expr!= null) {
                    disp.expr.accept(this);
                }
                for (int i = 1; i < disp.ls.size() ; i++) {
                    disp.ls.get(i).accept(this);
                }
                return null;
            }
        };

        //~~~~~~~~~~~~~~~|
        //RESOLUTION PASS|
        //~~~~~~~~~~~~~~~|

        ParserRuleContext finalGlobalTree = globalTree;
        var resolutionPassVisitor = new ASTVisitor<Symbol>(){

            String curClass;

            @Override
            public Symbol visit(StringCool stringCool) {
                stringCool.returnType = new Symbol("String");
                return stringCool.returnType;
            }

            @Override
            public Symbol visit(True truee) {
                truee.returnType = new Symbol("Bool");
                return truee.returnType;
            }

            @Override
            public Symbol visit(False falsee) {
                falsee.returnType = new Symbol("Bool");
                return falsee.returnType;
            }

            @Override
            public Symbol visit(Id id) {
                if(id.token.getText().equals("self")){
                    id.returnType = new Symbol(curClass);
                    return id.returnType;
                }

                if( id.s.existsId(id.token.getText()) ){
                    if(id.s.getType(id.token.getText()).getName().equals("SELF_TYPE")){
                        id.returnType = new Symbol(curClass);
                        return id.returnType;
                    }
                }
                if(!(id.token.getText().equals("self") || id.s.existsId(id.token.getText()) || id.s.scopeType.equals("Class"))){
                } else{
                    id.returnType = id.s.getType(id.token.getText());
                    return id.returnType;
                }
                return null;
            }

            @Override
            public Symbol visit(Int intt) {
                intt.returnType = new Symbol("Int");
                return intt.returnType;
            }

            @Override
            public Symbol visit(Not not) {
                Symbol s = not.expr.accept(this);
                if(!s.getName().equals("Bool")){
                }
                not.returnType = new Symbol("Bool");
                return not.returnType;
            }

            @Override
            public Symbol visit(Neg neg) {
                Symbol s = neg.expr.accept(this);
                if(!s.getName().equals("Int")){
                }

                neg.returnType = new Symbol("Int");
                return neg.returnType;
            }

            @Override
            public Symbol visit(Assign assign) {
                if(assign.id.equals("self")){
                }
                Symbol s1 = assign.s.getType(assign.id);
                Symbol s2 = assign.expr.accept(this);

                if(s1 != null && s2 != null) {
                    ClassNode c1 = SymbolTable.lookupClass(s1.getName());
                    ClassNode c2 = SymbolTable.lookupClass(s2.getName());
                    if(c1 != null && c2 != null)
                        if(!ClassNode.isSubType(c2, c1)){
                        }
                }

                assign.returnType = s2;
                return assign.returnType;
            }

            @Override
            public Symbol visit(IsVoid isVoid) {
                isVoid.expr.accept(this);
                isVoid.returnType = new Symbol("Bool");

                return isVoid.returnType;
            }

            @Override
            public Symbol visit(NewType newType) {
                ClassNode c =  SymbolTable.lookupClass(newType.Type);
                if(c == null){
                }
                newType.returnType = new Symbol(newType.Type);
                return newType.returnType;
            }

            @Override
            public Symbol visit(Mul mul) {
                Symbol s1 = mul.expr1.accept(this);
                Symbol s2 = mul.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                mul.returnType = new Symbol("Int");
                return mul.returnType;
            }

            @Override
            public Symbol visit(Div div) {
                Symbol s1 = div.expr1.accept(this);
                Symbol s2 = div.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                div.returnType = new Symbol("Int");
                return div.returnType;
            }

            @Override
            public Symbol visit(Plus plus) {
                Symbol s1 = plus.expr1.accept(this);
                Symbol s2 = plus.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                plus.returnType = new Symbol("Int");
                return plus.returnType;
            }

            @Override
            public Symbol visit(Min min) {
                Symbol s1 = min.expr1.accept(this);
                Symbol s2 = min.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                min.returnType = new Symbol("Int");
                return min.returnType;
            }

            @Override
            public Symbol visit(Less less) {
                Symbol s1 = less.expr1.accept(this);
                Symbol s2 = less.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                less.returnType = new Symbol("Bool");
                return less.returnType;
            }

            @Override
            public Symbol visit(LessEq lesseq) {
                Symbol s1 = lesseq.expr1.accept(this);
                Symbol s2 = lesseq.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                }
                if(!s2.getName().equals("Int")){
                }
                lesseq.returnType = new Symbol("Bool");
                return lesseq.returnType;
            }

            @Override
            public Symbol visit(Eq eq) {
                Symbol s1 = eq.expr1.accept(this);
                Symbol s2 = eq.expr2.accept(this);
                if(s1.getName().equals("Int") || s1.getName().equals("Bool") || s1.getName().equals("String")){
                    if(!s1.getName().equals(s2.getName())){
                    }
                }
                eq.returnType = new Symbol("Bool");
                return eq.returnType;
            }

            @Override
            public Symbol visit(Par par) {
                par.returnType = par.expr.accept(this);
                return par.returnType;
            }

            @Override
            public Symbol visit(Braces braces) {
                Symbol s = null;
                for(Expr e: braces.ls)
                    s = e.accept(this);

                braces.returnType = s;
                return braces.returnType;
            }

            @Override
            public Symbol visit(Case casee) {


                ArrayList<ClassNode> ls = new ArrayList<>();
                casee.expr.accept(this);
                for(int i = 0; i < casee.lid.size(); i++){
                    ClassNode c1 = SymbolTable.lookupClass(casee.lt.get(i));
                    if(c1 == null && !casee.lt.get(i).equals("SELF_TYPE")){
                    }
                    Symbol s = casee.ls.get(i).accept(this);
                    ClassNode c2;
                    if(s.getName().equals("SELF_TYPE")){
                        c2 = SymbolTable.lookupClass(curClass);
                    } else{
                        c2 = SymbolTable.lookupClass(s.getName());
                    }
                    ls.add(c2);
                }
                casee.returnType = new Symbol(ClassNode.greatestCommonAncestorFold(ls,
                        null, SymbolTable.lookupClass(curClass)).name);
                return casee.returnType;
            }

            @Override
            public Symbol visit(Let let) {
                for(Let_Branch lb : let.ls){
                    if(!lb.id.equals("self")){
                        ClassNode idType = null;
                        if(lb.type.equals("SELF_TYPE")){
                            idType = SymbolTable.lookupClass(curClass);
                        } else{
                            idType = SymbolTable.lookupClass(lb.type);
                        }
                        if(idType == null){
                        }
                        Symbol s = lb.accept(this);
                        if(s != null){
                            ClassNode exprType = SymbolTable.lookupClass(s.getName());
                        }

                    } else {
                        lb.accept(this);
                    }
                }
                let.returnType = let.expr.accept(this);
                return let.returnType;
            }

            @Override
            public Symbol visit(Let_Branch let_branch) {
                // TODO MIGHT NEED TO ADD A CUSTOM TYPE LATER
                if(let_branch.expr != null)
                    return let_branch.expr.accept(this);
                return null;
            }

            @Override
            public Symbol visit(While whilee) {
                Symbol s1 =  whilee.expr1.accept(this);
                Symbol s2 = whilee.expr2.accept(this);
                if(!s1.getName().equals("Bool")){
                }

                // TODO UNKNOWN IF TRUE
                whilee.returnType = new Symbol("Object");
                return whilee.returnType;
            }

            @Override
            public Symbol visit(If iff) {
                Symbol s1 = iff.expr1.accept(this);
                if(!s1.getName().equals("Bool")){
                }

                Symbol s2 = iff.expr2.accept(this);
                ClassNode c2;

                if(s2.getName().equals("SELF_TYPE")){
                    c2 = SymbolTable.lookupClass(curClass);
                } else{
                    c2 = SymbolTable.lookupClass(s2.getName());
                }

                Symbol s3 = iff.expr3.accept(this);
                ClassNode c3;


                if(s3.getName().equals("SELF_TYPE")){
                    c3 = SymbolTable.lookupClass(curClass);
                } else{
                    c3 = SymbolTable.lookupClass(s3.getName());
                }

                iff.returnType = new Symbol(ClassNode.greatestCommonAncestor(c2, c3, SymbolTable.lookupClass(curClass)).name);
                return iff.returnType;
            }

            @Override
            public Symbol visit(Program program) {
                for (Class1 l : program.ls) {
                    String name = l.lt.get(0);
                    ClassNode node = SymbolTable.lookupClass(name);
                    if(node != null){
                        ClassNode parent = SymbolTable.lookupClass(node.temporaryParent);
                        if( parent != null) {
                            if (parent.inheritable) {
                                parent.addChild(node); //creeaza arborele de tipuri
                                node.s.setParent(parent.s);//leaga si scopurile
                            }
                        }
                    }
                }

                for (Class1 l : program.ls) {
                    var name = l.lt.get(0);
                    ClassNode c = SymbolTable.classMap.get(name);
                    if(c != null){
                        if(c.detectCycle()){
                        }
                        else{
                            if(c.temporaryParent != null &&  l.lt.size() > 1){
                                ClassNode parent = SymbolTable.classMap.get(c.temporaryParent);
                                if(parent == null){
                                }
                            }
                        }
                    }
                }
                for (Class1 l : program.ls) {
                    l.accept(this);
                }

                return null;
            }

            @Override
            public Symbol visit(Class1 class1) {
                String name = class1.lt.get(0);
                curClass = name;
                for (Feature l : class1.ls) {
                    l.accept(this);
                }
                return null;
            }

            @Override
            public Symbol visit(Feature feature) { //nu
                return null;
            }

            @Override
            public Symbol visit(Method method) {
                ClassNode c = SymbolTable.classMap.get(curClass);

                for(Formal f : method.ls){
                        if(SymbolTable.classMap.get(f.Type) == null && !f.Type.equals("SELF_TYPE")){
                    }
                }

                int size = method.ls.size();

                Touple t = null;
                if(c.parent != null){
                    if(c.parent.s != null){
                        t = c.parent.s.lookupF(method.id);
                    }
                }

                if(t != null){
                    if(t.args.size() != size){
                    }
                }

                t = null;

                if(c.parent != null){
                    if(c.parent.s != null){
                        t = c.parent.s.lookupFType(method.id);
                    }
                }

                if(t != null){
                    for(int i = 0; i < method.ls.size(); i++){
                        if(!method.ls.get(i).Type.equals(t.args.get(i + 1).getName())){
                        }
                    }

                    if(!method.type.equals(t.args.get(0).getName())){
                    }
                }


                for(Formal f : method.ls) {
                    f.accept(this);
                }


                Symbol s = method.expr.accept(this);
                if(s != null){
                    ClassNode idType = SymbolTable.lookupClass(method.type);
                    ClassNode exprType = SymbolTable.lookupClass(s.getName());
                }

                method.returnType = new Symbol(method.type);
                return method.returnType;
            }

            @Override
            public Symbol visit(Atribute atribute) {
                ClassNode c = SymbolTable.classMap.get(curClass);

                DefaultScope ds = SymbolTable.scopeMap.get(c.parent);
                Symbol s = null;
                if(ds != null)
                    s =  ds.lookup(atribute.id);

                if(s != null){ //redefinit inherited
                }

                if(SymbolTable.lookupClass(atribute.Type) == null && !atribute.error && !atribute.Type.equals("SELF_TYPE")){
                }


                if(atribute.expr != null) {
                    Symbol sym = atribute.expr.accept(this);
                    ClassNode idType = null;
                    ClassNode exprType = null;
                    if(atribute.Type.equals("SELF_TYPE")){
                        idType = c;
                    } else {
                        idType = SymbolTable.lookupClass(atribute.Type);
                    }
                    if(sym != null)
                        exprType = SymbolTable.lookupClass(sym.getName());
                }
                return null;
            }

            @Override
            public Symbol visit(Formal formal) {
                return null;
            }

            @Override
            public Symbol visit(Expr expr) {  //nu
                return null;
            }

            @Override
            public Symbol visit(Indisp indisp) {
                ArrayList<Symbol> paramTypes = new ArrayList<>();
                for(Expr e : indisp.ls) {
                    paramTypes.add(e.accept(this));
                }
                ClassNode c = SymbolTable.lookupClass(curClass);
                if(c != null){
                    Touple fids = c.s.lookupF(indisp.id);
                    Touple ftypes = c.s.lookupFType(indisp.id);
                    if(fids != null) {
                        if(indisp.ls.size() != fids.args.size()){
                        } else {
                            for(int i = 0; i < paramTypes.size(); i++){
                                ClassNode c1 = SymbolTable.lookupClass(ftypes.args.get(i + 1).getName());
                                ClassNode c2 = SymbolTable.lookupClass(paramTypes.get(i).getName());
                                if( !ClassNode.isSubType(c2, c1) ){
                                }
                            }
                        }
                        //TODO UNSURE IF RIGHT
                        indisp.returnType = new Symbol(ftypes.args.get(0).getName());
                        return indisp.returnType;
                    }else {
                    }
                }   else {
                    System.out.println("class not found");
                }
                return null;
            }

            @Override
            public Symbol visit(Disp disp) {
                ArrayList<Symbol> paramTypes = new ArrayList<>();
                for (int i = 1; i < disp.ls.size() ; i++) {
                    paramTypes.add(disp.ls.get(i).accept(this));
                }

                Symbol s = disp.expr.accept(this);
                ClassNode exprType = null;
                if(s!=null) {
                    if(s.getName().equals("SELF_TYPE")){
                        exprType = SymbolTable.lookupClass(curClass);
                    } else{
                        exprType = SymbolTable.lookupClass(s.getName());
                    }
                }

                if(exprType != null){
                    ClassNode staticType = null;

                    if(disp.type != null) {
                        if (disp.type.equals("SELF_TYPE")) {
                        }
                        else{
                            staticType = SymbolTable.lookupClass(disp.type);
                            if(staticType == null){
                            } else {
                                if(!ClassNode.isSubType(exprType, staticType)){
                                    staticType = null;
                                }
                            }
                        }
                    }

                    Touple fids = null;
                    Touple ftypes = null;

                    if(staticType != null) {
                        fids = staticType.s.lookupF(disp.id);
                        ftypes = staticType.s.lookupFType(disp.id);
                    } else {
                        fids = exprType.s.lookupF(disp.id);
                        ftypes = exprType.s.lookupFType(disp.id);
                    }

                    if(fids != null) {
                        if(disp.ls.size() - 1 != fids.args.size()){
                        } else {
                            for(int i = 1; i < ftypes.args.size(); i++){
                                ClassNode c1 = SymbolTable.lookupClass(ftypes.args.get(i).getName());
                                ClassNode c2 = SymbolTable.lookupClass(paramTypes.get(i-1).getName());
                                if( !ClassNode.isSubType(c2, c1) ){
                                }
                            }
                        }

                        disp.returnType = new Symbol(ftypes.args.get(0).getName());
                        return disp.returnType;
                    } else {
                        if(staticType != null){
                        } else {
                        }
                    }
                } else {
                    System.out.println("class not found " + s.getName());
                }
                return null;
            }
        };

        var stgVisitor = new ASTVisitor<ST>(){
            final STGroupFile templates = new STGroupFile("gen.stg");


            String curClass;
            ArrayList<String> atributesToInit;
            Map<String, ST> atributesExpressions;
            Map<String, ST> methodSTtMap;

            // Gives the default value for a given data type, as a string.
            public String getDefaultFieldValue(String type) {
                switch (type) {
                    case "Int" -> {
                        return "int_const0";
                    }
                    case "Bool" -> {
                        return "bool_const0";
                    }
                    case "String" -> {
                        return "str_const0";
                    }
                    default -> {
                        return "0";
                    }
                }
            }

            // Returns a list with all the default field values.
            // Reaches the top-most parent and then goes down to the input node, adding every field.
            public List<String> getDefaultFieldValues(ClassNode node) {
                if (node == null) {
                    // Reached a null parent
                    return new ArrayList<>();
                }

                // Get the list from parents
                List<String> attributeValues = getDefaultFieldValues(node.parent);

                // Add the current node's fields
                for (var symbol : node.s.symbolsType.values()) {
                    attributeValues.add(getDefaultFieldValue(symbol.getName()));
                }

                // Return the list
                return attributeValues;
            }

            // Returns a list with all the attributes for a class.
            // It looks up the parent classes too.

            // Returns a list with all the functions from the top-most parent to the current node,
            // with the class names too.
            public List<Pair<String, Touple>> getFunctions(ClassNode node) {

                // Get the list the normal way with overridden functions and reverse it.
                Map<String, Pair<String, Touple>> functionsMap = new LinkedHashMap<>();
                List<Pair<String, Touple>> functionsList = new ArrayList<>();

                while (node != null) {
                    List<Pair<String, Touple>> tmp = new ArrayList<>();

                    for (var tuple : node.s.fSymbols.values()) {
                        if (!functionsMap.containsKey(tuple.id.getName())) {
                            Pair<String, Touple> tmpPair = new Pair<>(node.name, tuple);
                            functionsMap.put(tuple.id.getName(), tmpPair);
                            tmp.add(tmpPair);
                        }
                    }

                    Collections.reverse(tmp);
                    functionsList.addAll(tmp);

                    node = node.parent;
                }

                Collections.reverse(functionsList);

                //System.out.println("# MAP = " + functionsMap.values());
                //System.out.println("# LIST = " + functionsList);

                return functionsList;
            }

            // Adds the program name as a constant string instance once.
            public String getProgramName() {
                // Get the file name.
                String programName = null;
                if(args[0].lastIndexOf("/") == -1){
                    programName = args[0].substring(args[0].lastIndexOf("\\") + 1);
                }
                else{
                    programName = args[0].substring(args[0].lastIndexOf("/") + 1);
                }

                //String programName = args[0].substring(args[0].lastIndexOf("/") + 1);

                // Check if it's not present yet.
                if (!SymbolTable.isProgramNameAdded) {
                    SymbolTable.addStringFinal(programName);
                    SymbolTable.isProgramNameAdded = true;
                }

                // Return it.
                return "str_const" + SymbolTable.stringMap.get(programName);
            }

            public Object getKeyFromValue(Map hm, Object value) {
                for (Object o : hm.keySet()) {
                    if (hm.get(o).equals(value)) {
                        return o;
                    }
                }
                return null;
            }




            // MAIN PROGRAM VISIT
            @Override
            public ST visit(Program program) {
                // Global tags
                int itag = 0, stag = 0, btag = 0;
                for (var c : SymbolTable.classMap.values()){
                    switch (c.name) {
                        case "String" -> stag = c.tag;
                        case "Int" -> itag = c.tag;
                        case "Bool" -> btag = c.tag;
                    }
                }



                // Class dispatch tables
                List<ST> dispatchTables = new ArrayList<>();

                for (var classNode : SymbolTable.classMap.values()) {
                    var functionInfo = getFunctions(classNode);
                    List<String> functionNames = new ArrayList<>();
                    for (var pair : functionInfo) {
                        functionNames.add(pair.id + "." + pair.type.id.getName());
                    }

                    // Store the list for later usage.
                    classNode.dispatchList = functionNames;

                    // Create the string for the entire part
                    dispatchTables.add(templates.getInstanceOf("disptab_entry")
                            .add("name", classNode.name)
                            .add("functions", functionNames));
                }



                // Setup for class indexes
                for(var key : SymbolTable.classMap.keySet()){
                    SymbolTable.addStringFinal(key);
                }

                System.out.println("# SYMBOL TABLES STRINGMAP:");
                System.out.println("# " + SymbolTable.stringMap.values());
                System.out.println("# " + SymbolTable.stringMap.keySet());
                List<ClassNode> temp = new ArrayList<>(SymbolTable.classMap.values());
                temp.sort(Comparator.comparingInt(o -> o.tag));
                for(var node : temp)
                    SymbolTable.tagClassMap.put(node.tag, node.name);

                // VISIT CLASSES
                for (var x:program.ls)
                    x.accept(this);



                // String constants
                List<ST> stringconsts = new ArrayList<>();
                for(var key :SymbolTable.stringMap.keySet()){
                    var val = SymbolTable.stringMap.get(key);
                    //TODO maybe process the string?
                    int lenght = 0;
                    if (key != null)
                        lenght = key.length();
                    ST stringConst = templates.getInstanceOf("constant_string_template").add("id", val)
                            .add("string_class_index", stag)
                            .add("size",  5 + (lenght / 4))
                            .add("int_id", SymbolTable.stringIntMap.get(key))
                            .add("text", key);
                    stringconsts.add(stringConst);
                }


                // Int constants
                List<ST> intConsts = new ArrayList<>();
                for(var key :SymbolTable.integerMap.keySet()){
                    var val = SymbolTable.integerMap.get(key);
                    ST intConst = templates.getInstanceOf("constant_int_template")
                            .add("id", key)
                            .add("int_class_index", itag)
                            .add("int_value", val);
                    intConsts.add(intConst);
                }

                // Class name table
                List<ST> classNameTabs = new ArrayList<>();
                for(var name : SymbolTable.tagClassMap.values()){
                    System.out.println("# NAMETAB ENTRY " + "str_const" + SymbolTable.stringMap.get(name) + ": " + name);
                    ST cst = templates.getInstanceOf("class_nametab_entry")
                            .add("id", SymbolTable.stringMap.get(name));
                    classNameTabs.add(cst);
                }

                // Object table
                List<ST> objTabs = new ArrayList<>();
                for(var name : SymbolTable.tagClassMap.values()){
                    System.out.println("# NAME " + name + " WITH KEY " + getKeyFromValue(SymbolTable.tagClassMap, name));
                    ST cst = templates.getInstanceOf("class_objtab_entry")
                            .add("name", name);
                    objTabs.add(cst);
                }

                // Prototype objects list
                // It will contain the default field values, ordered correctly
                List<ST> prototypeObjects = new ArrayList<>();
                for (var classNode : SymbolTable.classMap.values()) {

                    // Check for basic classes first
                    if (classNode.name.equals("String")) {
                        // Case specific for String
                        prototypeObjects.add(templates.getInstanceOf("string_protobj_entry")
                                .add("class_index", classNode.tag));

                    } else {

                        // Generic case, with default field value list
                        List<String> defaultFieldValues = null;
                        switch (classNode.name) {
                            case "Int", "Bool" -> {
                                defaultFieldValues = new ArrayList<>();
                                defaultFieldValues.add("0");
                            }

                            default -> defaultFieldValues = getDefaultFieldValues(classNode);
                        }

                        // Create the string for the entire part
                        // Size is integrated
                        prototypeObjects.add(templates.getInstanceOf("protobj_entry")
                                .add("name", classNode.name)
                                .add("class_index", classNode.tag)
                                .add("size", 3 + defaultFieldValues.size())
                                .add("fields", defaultFieldValues));
                    }
                }



                // Initialization function definitions for each class
                List<ST> inits = new ArrayList<>();

                for (var classNode : SymbolTable.classMap.values()) {
                    String parentName = null;
                    if (classNode.parent != null) {
                        parentName = classNode.parent.name;
                    }

                    // Get the fields
                    List<String> AttributeValues = new ArrayList<>();
                    List<String> AttributeOffsets = new ArrayList<>();
                    classNode.getDepth();

                    if(classNode.atributeOffsetMap != null){
                        for(var x : classNode.atributeOffsetMap.keySet()){
                            AttributeValues.add(classNode.atributeSTtMap.get(x).render());
                            AttributeOffsets.add(String.valueOf(classNode.atributeOffsetMap.get(x)*4 + 12));
                        }
                    }

                    inits.add(templates.getInstanceOf("init_class_template")
                            .add("name", classNode.name)
                            .add("parent_name", parentName)
                            .add("attr_init_values", AttributeValues)
                            .add("attr_init_offsets", AttributeOffsets));
                }



                // Other function definitions
                List<ST> functions = new ArrayList<>();
                for (var classNode : SymbolTable.classMap.values()) {
                    // If it's a basic class, skip it; the methods are already defined.
                    switch (classNode.name) {
                        case "Object", "Int", "Bool", "String", "IO" -> {
                            continue;
                        }
                    }

                    // Get the functions
                    for (var functionSymbol : classNode.s.fSymbols.values()) {
                        // TODO REMOVE THIS MAKESHIFT CODE

                        functions.add(templates.getInstanceOf("function_template")
                                .add("class_name", classNode.name)
                                .add("method_name", functionSymbol.id.getName())
                                .add("code", classNode.methodSTtMap.get(functionSymbol.id.getName()))
                                .add("full_offset", functionSymbol.args.size()*4)
                        );
                    }
                }
                //System.out.println(SymbolTable.tagClassMap);

                return templates.getInstanceOf("program")
                        .add("int_class_index", itag)
                        .add("string_class_index", stag)
                        .add("bool_class_index", btag)
                        .add("string_constants", stringconsts)
                        .add("int_constants", intConsts)
                        .add("nametabs", classNameTabs)
                        .add("objtabs", objTabs)
                        .add("protobjs", prototypeObjects)
                        .add("disptabs", dispatchTables)
                        .add("inits", inits)
                        .add("functions", functions);
            }





            @Override
            public ST visit(Class1 class1) {
                String name = class1.lt.get(0);
                curClass = name;
                atributesToInit = new ArrayList<>();
                atributesExpressions = new LinkedHashMap<>();
                methodSTtMap = new LinkedHashMap<>();
                //System.out.println(name);
                //class1.
                for(var x :class1.ls)
                    x.accept(this);

                //System.out.println(atributesToInit);

                SymbolTable.classAtributeMap.put(name, atributesToInit);
                SymbolTable.classMap.get(name).atributeSTtMap = atributesExpressions;
                SymbolTable.classMap.get(name).methodSTtMap = methodSTtMap;

                return null;
            }

            @Override
            public ST visit(Feature feature) {
                feature.accept(this);
                return null;
            }

            @Override
            public ST visit(Method method) {
                /*System.out.println(method.id);
                for(var x : method.ls){
                    System.out.println(x.id);
                }*/

                //System.out.println(c.s.getIdScope(atribute.id));

                // TODO ADD FORMALS BEING ABLE TO BE ACCESSED
                ST s = method.expr.accept(this);
                methodSTtMap.put(method.id, s);
                return null;
            }

            @Override
            public ST visit(Atribute atribute) {
                //System.out.println(curClass);
                ClassNode c = SymbolTable.classMap.get(curClass);

                //System.out.println(c.s.getIdScope(atribute.id));

                //System.out.println(id.s.getIdScope(id.token.getText()));

                // Check if attribute is initialized
                if (atribute.expr != null){
                    atributesToInit.add(atribute.id);
                    ST s = atribute.expr.accept(this);

                    // THE ST IS STORED IN THE CURRENT atributesExpressions !!!
                    atributesExpressions.put(atribute.id, s);
                    //System.out.println(s.render());

                    //System.out.println("Atribut init: " + atribute.id);
                    //System.out.println("# Offset for " + atribute.id + " is " + c.s.getIdOffset(atribute.id));

                }
                return null;
            }

            @Override
            public ST visit(Formal formal) {
                //TODO
                return null;
            }

            @Override
            public ST visit(Expr expr) {
                // Left alone
                return null;
            }

            @Override
            public ST visit(Indisp indisp) {

                // Add the parameters for the dispatch
                List<ST> ParametersSTList = new ArrayList<>();
                for (var x: indisp.ls) {
                    ParametersSTList.add(templates.getInstanceOf("store_to_stack_template")
                            .add("expr", x.accept(this)));
                }
                // Parameters were taken from the first to last, must be reversed now
                Collections.reverse(ParametersSTList);

                // For the dispatch label name, we use a static accumulator to make unique dispatch labels.
                Integer dispatch_id = SymbolTable.dispatch_count;
                SymbolTable.dispatch_count++;

                // Get the program name label.
                String programName = getProgramName();

                // Get the dispatch line.
                String instructionLine = Integer.toString(indisp.ctx.start.getLine());

                // Get the dispatching instance's corresponding method offset.
                // Must look up the instance's methods and parent's methods.
                // Since it's implicit, it will use only the current class.
                var classNode = SymbolTable.classMap.get(curClass);

                int functionIndex = -1;
                String className = null;
                String functionName = null;

                while (functionIndex == -1 && classNode != null) {
                    // At the beginning of loop
                    className = classNode.name;
                    functionName = className + '.' + indisp.id;

                    // In loop
                    functionIndex = classNode.dispatchList.indexOf(functionName);

                    // At the end of loop
                    classNode = classNode.parent;
                }

                //System.out.println("FUNCTION " + functionName + " FOUND IN CLASS " + curClass + " AT INDEX " + functionIndex);
                String methodOffset = Integer.toString(functionIndex * 4);

                ST dispatchST = templates.getInstanceOf("dispatch_template")
                        .add("parameters_expr", ParametersSTList)
                        .add("dispatch_expr", templates.getInstanceOf("self_template"))
                        .add("dispatch_id", dispatch_id)
                        .add("program_name_label", programName)
                        .add("instruction_line", instructionLine)
                        .add("method_offset", methodOffset);

                return dispatchST;
            }

            @Override
            public ST visit(Disp disp) {

                // Grab the dispatching expression ST.
                ST dispatcherST = disp.expr.accept(this);

                // Next expressions are the function parameters.
                Boolean skippedFirst = false;
                List<ST> ParametersSTList = new ArrayList<>();
                for (var x: disp.ls) {
                    if (!skippedFirst) {
                        skippedFirst = true;
                    } else {
                        ParametersSTList.add(templates.getInstanceOf("store_to_stack_template")
                                .add("expr", x.accept(this)));
                    }
                }
                // Parameters were taken from the first to last, must be reversed now
                Collections.reverse(ParametersSTList);

                // For the dispatch label name, we use a static accumulator to make unique dispatch labels.
                Integer dispatch_id = SymbolTable.dispatch_count;
                SymbolTable.dispatch_count++;

                // Get the program name label.
                String programName = getProgramName();

                // Get the dispatch line.
                String instructionLine = Integer.toString(disp.ctx.start.getLine());

                // Get the dispatching instance's corresponding method offset.
                // Must look up the instance's methods and parent's methods.
                // However, must take in consideration the static dispatch class too, if necessary.
                // Plus, the correct class must be picked.
                if (disp.expr.returnType.getName().equals("SELF_TYPE")) {
                    disp.expr.returnType = new Symbol(curClass);
                }

                var classNode = SymbolTable.classMap.get(disp.expr.returnType.getName());

                int functionIndex = -1;
                String className = disp.type;
                String functionName = null;

                while (functionIndex == -1 && classNode != null) {
                    // At the beginning of loop
                    if (disp.type == null) {
                        className = classNode.name;
                    }
                    functionName = className + '.' + disp.id;

                    // In loop
                    functionIndex = classNode.dispatchList.indexOf(functionName);

                    // At the end of loop
                    classNode = classNode.parent;
                }

                //System.out.println("FUNCTION " + functionName + " FOUND IN CLASS " + curClass + " AT INDEX " + functionIndex);
                String methodOffset = Integer.toString(functionIndex * 4);

                ST dispatchST = templates.getInstanceOf("dispatch_template")
                        .add("parameters_expr", ParametersSTList)
                        .add("dispatch_expr", dispatcherST)
                        .add("dispatch_id", dispatch_id)
                        .add("program_name_label", programName)
                        .add("instruction_line", instructionLine)
                        .add("static_class", disp.type)
                        .add("method_offset", methodOffset);

                return dispatchST;
            }

            @Override
            public ST visit(If iff) {
                //TODO
                return templates.getInstanceOf("if_template")
                        .add("cond_expr", iff.expr1.accept(this))
                        .add("then_expr", iff.expr2.accept(this))
                        .add("else_expr", iff.expr3.accept(this))
                        .add("tag_id", ++SymbolTable.ifTag);
            }

            @Override
            public ST visit(While whilee) {
                //TODO
                whilee.expr1.accept(this);
                whilee.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Let let) {
                ArrayList<ST> exprs = new ArrayList<ST>();
                ArrayList<Integer> offsets = new ArrayList<Integer>();

                int i = 1;

                for(var x : let.ls){
                    if(x.expr!=null)
                        exprs.add(x.expr.accept(this));
                    else{
                        //System.out.println(x.type);
                        switch(x.type) {
                            case "String":
                                exprs.add( templates.getInstanceOf("load_constant_value_template")
                                        .add("constant_value", "str_const0"));
                                break;
                            case "Bool":
                                exprs.add( templates.getInstanceOf("load_constant_value_template")
                                        .add("constant_value", "bool_const0"));
                                break;
                            case "Int":
                                exprs.add( templates.getInstanceOf("load_constant_value_template")
                                    .add("constant_value", "int_const0"));
                                break;
                            default:
                                exprs.add( templates.getInstanceOf("load_constant_value_template")
                                        .add("constant_value", "0"));
                        }
                    }

                    offsets.add(-(i*4));
                    i++;
                }

                ST letST = templates.getInstanceOf("let_template")
                        .add("full_offset", (let.maxDepth + 1)*4)
                        .add("params_init", exprs)
                        .add("params_offset", offsets)
                        .add("let_body", let.expr.accept(this));
                return letST;
            }

            @Override
            public ST visit(Case casee) {
                //TODO

                int case_body_id = SymbolTable.case_void;

                //casee.expr.accept(this);
                ArrayList<ST> exprs = new ArrayList<ST>();

                ArrayList<Pair<Pair<Integer, Integer>, Expr>> aux = new ArrayList<>();
                ArrayList<Integer> temp = new ArrayList<>();

                for(int i = 0; i < casee.lt.size(); i++) {
                    Pair<Integer, Integer> p = SymbolTable.classMap.get(casee.lt.get(i)).classTagInterval();
                    aux.add(new Pair(p, casee.ls.get(i)));
                    //temp.add(p.id);
                }
                Comparator<Pair<Pair<Integer, Integer>, Expr>> byName =
                        (Pair<Pair<Integer, Integer>, Expr> o1,
                         Pair<Pair<Integer, Integer>, Expr> o2)
                                ->o1.id.id.compareTo(o2.id.id);
                aux.sort(byName);
                Collections.reverse(aux);


                //rrayList<Pair<>>

                //System.out.println(aux);

                for(int i = 0; i < aux.size(); i++){
                    if(i == 0){
                        exprs.add(templates.getInstanceOf("case_branch_first_template")
                                .add("next_case_branch_id", case_body_id + 1)
                                .add("case_body_id", case_body_id)
                                .add("lowest_class_index", aux.get(i).id.id)
                                .add("highest_class_index", aux.get(i).id.type)
                                .add("expr", aux.get(i).type.accept(this)));
                    } else {
                        exprs.add(templates.getInstanceOf("case_branch_general_template")
                                .add("case_branch_id", SymbolTable.case_void)
                                .add("next_case_branch_id", SymbolTable.case_void + 1)
                                .add("case_body_id", case_body_id)
                                .add("lowest_class_index", aux.get(i).id.id)
                                .add("highest_class_index", aux.get(i).id.type)
                                .add("expr", aux.get(i).type.accept(this)));
                        SymbolTable.case_void++;
                    }

                }

                exprs.add(templates.getInstanceOf("case_branch_last_template")
                        .add("case_branch_id", SymbolTable.case_void)
                        .add("case_local_offset", 4));

                return templates.getInstanceOf("case_body_template")
                        .add("expr", casee.expr.accept(this))
                        .add("case_body_id", case_body_id)
                        .add("program_name", getProgramName())
                        .add("instruction_line", casee.token.getLine()).
                        add("cases_code", exprs);
            }

            @Override
            public ST visit(IsVoid isVoid) {
                //TODO
                //isVoid.expr.accept(this);
                return templates.getInstanceOf("isvoid_template")
                        .add("expr", isVoid.expr.accept(this))
                        .add("tag_void", ++SymbolTable.tag_void);
            }

            @Override
            public ST visit(NewType newType) {

                ST nt = null;

                if(!newType.Type.equals("SELF_TYPE")){
                    nt = templates.getInstanceOf("new_template").add("class_name", newType.Type);
                }else{
                    nt = templates.getInstanceOf("new_template_self");
                }

                return nt;
            }

            @Override
            public ST visit(Par par) {
                //TODO
                par.expr.accept(this);
                return null;
            }

            @Override
            public ST visit(Braces braces) {
                // Visit each instruction.
                // Each instruction probably leaves the output in $a0, meaning that the last instruction will always
                // be the one that controls what $a0 will be left with.
                List<ST> instructionsST = new ArrayList<>();
                for (var x: braces.ls){
                    instructionsST.add(x.accept(this));
                }

                return templates.getInstanceOf("block_template").add("instructions", instructionsST);
            }

            @Override
            public ST visit(Neg neg) {
                //TODO
                neg.expr.accept(this);
                return null;
            }

            @Override
            public ST visit(Id id) {
                String idName = id.token.getText();
                // DEBUGGING

                // Check the id name and generate code for it.
                if (idName.equals("self")) {
                    // Id self will use self template.
                    return templates.getInstanceOf("self");
                } else {

                    switch (id.s.getIdScope(idName)) {
                        case "Class" -> {
                            return templates.getInstanceOf("class_attribute_template")
                                    .add("offset", id.s.getIdOffset(id.token.getText()));
                        }
                        case "Let" -> {
                            //System.out.println("Let full ofset " + id.s.full_ofset);
                            return templates.getInstanceOf("let_acces_template")
                                    .add("offset", id.s.getIdOffset(id.token.getText()));
                        }
                        case "Method" -> {
                            return templates.getInstanceOf("let_acces_template")
                                    .add("offset", id.s.getIdOffset(id.token.getText()));
                        }

                        case "Case" -> {
                            //return templates.getInstanceOf("class_attribute_template")
                            //        .add("offset", id.s.getIdOffset(id.token.getText()));
                        }
                        default -> {
                            System.out.println("# UNKNOWN SCOPE TYPE " + id.s.getIdScope(idName));
                        }
                    }
                }
                return null;
            }

            @Override
            public ST visit(Int intt) {
                int value = Integer.parseInt(intt.token.getText());
                int key = 0;

                for (Integer i : SymbolTable.integerMap.keySet()) {
                    if (SymbolTable.integerMap.get(i).equals(value)) {
                        key = i;
                    }
                }
                return templates.getInstanceOf("load_constant_value_template")
                        .add("constant_value", "int_const" + key);
            }

            @Override
            public ST visit(StringCool stringCool) {
                String value = stringCool.token.getText().substring(1, stringCool.token.getText().length() - 1);
                return templates.getInstanceOf("load_constant_value_template")
                        .add("constant_value", "str_const" + SymbolTable.stringMap
                                .get(value));
            }

            @Override
            public ST visit(True truee) {
                return templates.getInstanceOf("load_constant_value_template")
                        .add("constant_value", "bool_const1");
            }

            @Override
            public ST visit(False falsee) {
                return templates.getInstanceOf("load_constant_value_template")
                        .add("constant_value", "bool_const0");
            }

            @Override
            public ST visit(Mul mul) {
                //TODO
                mul.expr1.accept(this);
                mul.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Div div) {
                //TODO
                div.expr1.accept(this);
                div.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Plus plus) {
                //TODO
                plus.expr1.accept(this);
                plus.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Min min) {
                //TODO
                min.expr1.accept(this);
                min.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Less less) {
                //TODO
                less.expr1.accept(this);
                less.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(LessEq lesseq) {
                //TODO
                lesseq.expr1.accept(this);
                lesseq.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Eq eq) {
                //TODO
                eq.expr1.accept(this);
                eq.expr2.accept(this);
                return null;
            }

            @Override
            public ST visit(Assign assign) {
                // Get the string template from the expression.

                String idName = assign.id;
                ST exprST = assign.expr.accept(this);
                ST assingST = templates.getInstanceOf("assign_template")
                        .add("expr", exprST);

                switch (assign.s.getIdScope(idName)) {
                    case "Class" -> {
                        assingST.add("store_location", assign.s.getIdOffset(idName)+"($s0)");
                    }
                    case "Let" -> {
                        assingST.add("store_location", assign.s.getIdOffset(idName)+"($fp)");
                    }
                    case "Method" -> {
                        assingST.add("store_location", assign.s.getIdOffset(idName)+"($fp)");
                    }
                    case "Case" -> {

                    }
                    default ->
                        System.out.println("# UNKNOWN SCOPE TYPE " + assign.s.getIdScope(idName));
                }

                return assingST;
            }

            @Override
            public ST visit(Not not) {
                //TODO
                if(not.expr != null)
                    not.expr.accept(this);
                return null;
            }

            @Override
            public ST visit(Let_Branch let_branch) {
                //TODO
                if(let_branch.expr != null)
                    let_branch.expr.accept(this);
                return null;
            }


        };

        ast.accept(definitionPassVisitor);
        ast.accept(resolutionPassVisitor);

        //Integer cont = new Integer(0);
        Integer cont = Integer.valueOf(0);
        SymbolTable.object.DFS(cont, 0);

        ST program = ast.accept(stgVisitor);

        System.out.println(program.render());




        if (SymbolTable.hasSemanticErrors()) {
            //System.err.println("Compilation halted");
            return;
        }


    }
}
