package cool.compiler;

import cool.ast.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import cool.lexer.*;
import cool.parser.*;
import cool.structures.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


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
                
            /*
            // Test lexer only.
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();
            tokens.stream().forEach(token -> {
                var text = token.getText();
                var name = CoolLexer.VOCABULARY.getSymbolicName(token.getType());
                
                System.out.println(text + " : " + name);
                //System.out.println(token);
            });
            */
            
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
        
        // TODO Semantic analysis

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
                else
                    id.s = classScope;
                return null;
            }

            @Override
            public Type visit(Int intt) {
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
                structScope = new DefaultScope(structScope, "Struct");
                casee.expr.accept(this);
                for(int i = 0; i < casee.lid.size(); i++){
                    structScope = new DefaultScope(structScope, "Case");

                    if(casee.lid.get(i).equals("self")){
                        SymbolTable.error(casee.caseCtx.get(i), casee.caseTokensId.get(i),
                                "Case variable has illegal name self");
                    }else if(casee.lt.get(i).equals("SELF_TYPE")){
                        SymbolTable.error(casee.caseCtx.get(i), casee.caseTokensType.get(i),
                                "Case variable " + casee.lid.get(i) + " has illegal type SELF_TYPE");
                    }

                    casee.ls.get(i).accept(this);
                    structScope = structScope.getParent();
                }
                structScope = structScope.getParent();
                return null;
            }

            @Override
            public Type visit(Let let) {//pt fiecare let_branch se creeaza un scope imbricat
                int depth = 0;
                for(Let_Branch lb : let.ls){
                    lb.accept(this);

                    boolean error = false;
                    structScope = new DefaultScope(structScope, "Let");
                    depth++;
                    if(lb.id.equals("self")){
                        SymbolTable.error(lb.ctx, lb.token,
                                "Let variable has illegal name self");
                        error = true;
                    }

                    if(!error){
                        structScope.structId = new Symbol(lb.id);
                        structScope.structType = new Symbol(lb.type);
                    }

                    lb.s = structScope;
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
                    SymbolTable.error(class1.ctx, class1.type,
                            "Class has illegal name SELF_TYPE");
                    error = true;
                }

                ClassNode node = SymbolTable.lookupClass(name);
                if(node != null) {
                    SymbolTable.error(class1.ctx, class1.type,"Class " +  name + " is redefined");
                    error = true;
                }

                if(parent.equals("SELF_TYPE")){
                    SymbolTable.error(class1.ctx, class1.inheritType,"Class " + name + " has illegal parent SELF_TYPE");
                    parent = null;
                }

                var nodeParent = SymbolTable.lookupClass(parent);
                if(nodeParent != null)
                    if(!nodeParent.inheritable){
                        SymbolTable.error(class1.ctx, class1.inheritType, "Class " + name + " has illegal parent " + parent);
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

                //classScope = null;
                //methodScope = null;
                //structScope = null;


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
                        SymbolTable.error(method.ctx, f.token,
                                "Method " + method.id + " of class " + classScope.currentClass.name +
                                        " has formal parameter with illegal name self");
                        errorId = true;
                    }

                    if(f.Type.equals("SELF_TYPE")){
                        SymbolTable.error(method.ctx, f.typeToken,
                                "Method " + method.id + " of class " + classScope.currentClass.name +
                                        " has formal parameter " + f.id +" with illegal type SELF_TYPE");
                        errorId = true; //WILL PRODUCE BUGS nr(id) != nr(Types)
                    }

                    for(Symbol sym : argsId){
                        if(sym.getName().equals(f.id)){
                            SymbolTable.error(method.ctx, f.token,
                                    "Method " + method.id + " of class " + classScope.currentClass.name
                                            + " redefines formal parameter " + f.id);
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
                    SymbolTable.error(method.ctx, method.token,
                            "Class " + classScope.currentClass.name + " redefines method " +  tid.id.getName());
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
                    SymbolTable.error(atribute.ctx, atribute.idToken, "Class " + classScope.currentClass.name +
                            " has attribute with illegal name self");
                    atribute.error = true;
                } else if(classScope.symbols.get(atribute.id) != null){ //redefinit "local"
                    SymbolTable.error(atribute.ctx, atribute.idToken, "Class " + classScope.currentClass.name +
                            " redefines attribute " + atribute.id);
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
                return new Symbol("String");
            }

            @Override
            public Symbol visit(True truee) {
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(False falsee) {
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(Id id) {
                if(id.token.getText().equals("self")){
                    return new Symbol(curClass);
                }
                if( id.s.existsId(id.token.getText()) ){
                    if(id.s.getType(id.token.getText()).getName().equals("SELF_TYPE")){
                        return new Symbol(curClass);
                    }
                }
                if(!(id.token.getText().equals("self") || id.s.existsId(id.token.getText()) || id.s.scopeType.equals("Class"))){
                    SymbolTable.error(id.ctx, id.token, "Undefined identifier " + id.token.getText());
                } else{
                    return id.s.getType(id.token.getText());
                }
                return null;
            }

            @Override
            public Symbol visit(Int intt) {
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Not not) {
                Symbol s = not.expr.accept(this);
                if(!s.getName().equals("Bool")){
                    SymbolTable.error(not.ctx, not.t,
                            "Operand of not has type " + s.getName() + " instead of Bool");
                }
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(Neg neg) {
                Symbol s = neg.expr.accept(this);
                if(!s.getName().equals("Int")){
                    SymbolTable.error(neg.ctx, neg.t,
                            "Operand of ~ has type " + s.getName() + " instead of Int");
                }
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Assign assign) {
                if(assign.id.equals("self")){
                    SymbolTable.error(assign.ctx, assign.token, "Cannot assign to self");
                }
                Symbol s1 = assign.s.getType(assign.id);
                Symbol s2 = assign.expr.accept(this);

                if(s1 != null && s2 != null) {
                    ClassNode c1 = SymbolTable.lookupClass(s1.getName());
                    ClassNode c2 = SymbolTable.lookupClass(s2.getName());
                    if(c1 != null && c2 != null)
                        if(!ClassNode.isSubType(c2, c1)){
                            SymbolTable.error(assign.ctx, assign.exprToken,
                                    "Type " + c2.name + " of assigned" +
                                            " expression is incompatible with declared type " +
                                            c1.name +" of identifier " + assign.id);
                        }
                }
                return s2;
            }

            @Override
            public Symbol visit(IsVoid isVoid) {
                isVoid.expr.accept(this);
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(NewType newType) {
                ClassNode c =  SymbolTable.lookupClass(newType.Type);
                if(c == null){
                    SymbolTable.error(newType.ctx, newType.tokenType,
                            "new is used with undefined type " + newType.Type);
                }
                return new Symbol(newType.Type);
            }

            @Override
            public Symbol visit(Mul mul) {
                Symbol s1 = mul.expr1.accept(this);
                Symbol s2 = mul.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(mul.ctx, mul.t1,
                            "Operand of * has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(mul.ctx, mul.t2,
                            "Operand of * has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Div div) {
                Symbol s1 = div.expr1.accept(this);
                Symbol s2 = div.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(div.ctx, div.t1,
                            "Operand of / has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(div.ctx, div.t2,
                            "Operand of / has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Plus plus) {
                Symbol s1 = plus.expr1.accept(this);
                Symbol s2 = plus.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(plus.ctx, plus.t1,
                            "Operand of + has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(plus.ctx, plus.t2,
                            "Operand of + has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Min min) {
                Symbol s1 = min.expr1.accept(this);
                Symbol s2 = min.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(min.ctx, min.t1,
                            "Operand of - has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(min.ctx, min.t2,
                            "Operand of - has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Int");
            }

            @Override
            public Symbol visit(Less less) {
                Symbol s1 = less.expr1.accept(this);
                Symbol s2 = less.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(less.ctx, less.t1,
                            "Operand of < has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(less.ctx, less.t2,
                            "Operand of < has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(LessEq lesseq) {
                Symbol s1 = lesseq.expr1.accept(this);
                Symbol s2 = lesseq.expr2.accept(this);
                if(!s1.getName().equals("Int")){
                    SymbolTable.error(lesseq.ctx, lesseq.t1,
                            "Operand of <= has type " + s1.getName() + " instead of Int");
                }
                if(!s2.getName().equals("Int")){
                    SymbolTable.error(lesseq.ctx, lesseq.t2,
                            "Operand of <= has type " + s2.getName() + " instead of Int");
                }
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(Eq eq) {
                Symbol s1 = eq.expr1.accept(this);
                Symbol s2 = eq.expr2.accept(this);
                if(s1.getName().equals("Int") || s1.getName().equals("Bool") || s1.getName().equals("String")){
                    if(!s1.getName().equals(s2.getName())){
                        SymbolTable.error(eq.ctx, eq.eq,
                                "Cannot compare " + s1.getName() + " with " + s2.getName());
                    }
                }
                return new Symbol("Bool");
            }

            @Override
            public Symbol visit(Par par) {
                return par.expr.accept(this);
            }

            @Override
            public Symbol visit(Braces braces) {
                Symbol s = null;
                for(Expr e: braces.ls)
                    s = e.accept(this);

                return s;
            }

            @Override
            public Symbol visit(Case casee) {
                ArrayList<ClassNode> ls = new ArrayList<>();
                casee.expr.accept(this);
                for(int i = 0; i < casee.lid.size(); i++){
                    ClassNode c1 = SymbolTable.lookupClass(casee.lt.get(i));
                    if(c1 == null && !casee.lt.get(i).equals("SELF_TYPE")){
                        SymbolTable.error(casee.caseCtx.get(i), casee.caseTokensType.get(i),
                                "Case variable " +  casee.lid.get(i) + " has undefined type " + casee.lt.get(i));
                    }
                    Symbol s = casee.ls.get(i).accept(this);
                    ClassNode c2 = SymbolTable.lookupClass(s.getName());
                    ls.add(c2);
                }
                return new Symbol(ClassNode.greatestCommonAncestorFold(ls, null, SymbolTable.lookupClass(curClass)).name);
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
                            SymbolTable.error(lb.ctx, lb.typeToken,
                                    "Let variable " + lb.id + " has undefined type " + lb.type);
                        }
                        Symbol s = lb.accept(this);
                        if(s != null){
                            ClassNode exprType = SymbolTable.lookupClass(s.getName());
                            if(idType != null && exprType != null)
                                if(!ClassNode.isSubType(exprType, idType))
                                    SymbolTable.error(lb.ctx, lb.tokenE,
                                            "Type " + s.getName() + " of initialization expression of identifier "
                                                    + lb.id + " is incompatible with declared type " + lb.type);

                        }

                    } else {
                        lb.accept(this);
                    }
                }
                return let.expr.accept(this);
            }

            @Override
            public Symbol visit(Let_Branch let_branch) {
                if(let_branch.expr != null)
                    return let_branch.expr.accept(this);
                return null;
            }

            @Override
            public Symbol visit(While whilee) {
                Symbol s1 =  whilee.expr1.accept(this);
                Symbol s2 = whilee.expr2.accept(this);
                if(!s1.getName().equals("Bool")){
                    SymbolTable.error(whilee.ctx, whilee.tokenE1,
                            "While condition has type " + s1.getName() + " instead of Bool");
                }

                return new Symbol("Object");
            }

            @Override
            public Symbol visit(If iff) {
                Symbol s1 = iff.expr1.accept(this);
                if(!s1.getName().equals("Bool")){
                    SymbolTable.error(iff.ctx, iff.tokenE1,
                            "If condition has type " + s1.getName() + " instead of Bool");
                }

                Symbol s2 = iff.expr2.accept(this);
                ClassNode c2 = SymbolTable.lookupClass(s2.getName());
                Symbol s3 = iff.expr3.accept(this);
                ClassNode c3 = SymbolTable.lookupClass(s3.getName());

                return new Symbol(ClassNode.greatestCommonAncestor(c2, c3, SymbolTable.lookupClass(curClass)).name);
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
                            SymbolTable.error(l.ctx, l.type,
                                    "Inheritance cycle for class " + name);
                        }
                        else{
                            if(c.temporaryParent != null &&  l.lt.size() > 1){
                                ClassNode parent = SymbolTable.classMap.get(c.temporaryParent);
                                if(parent == null){
                                    SymbolTable.error(l.ctx, l.inheritType,
                                            "Class " + name + " has undefined parent " + l.lt.get(1));
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
                        SymbolTable.error(method.ctx, f.typeToken,
                                "Method " + method.id + " of class " + c.name +
                                        " has formal parameter " + f.id +" with undefined type " + f.Type);
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
                        SymbolTable.error(method.ctx, method.token,
                                "Class " + c.name + " overrides method " + method.id +
                                        " with different number of formal parameters");
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
                            SymbolTable.error(method.ctx, method.ls.get(i).typeToken,
                                    "Class " + c.name + " overrides method " + method.id +
                                            " but changes type of formal parameter " + method.ls.get(i).id +
                                            " from " + t.args.get(i + 1).getName() + " to " + method.ls.get(i).Type);
                        }
                    }

                    if(!method.type.equals(t.args.get(0).getName())){
                        SymbolTable.error(method.ctx, method.typeToken,
                                "Class " + c.name + " overrides method " + method.id +
                                        " but changes return type from " + t.args.get(0).getName() + " to " + method.type);
                    }
                }


                for(Formal f : method.ls) {
                    f.accept(this);
                }


                Symbol s = method.expr.accept(this);
                if(s != null){
                    ClassNode idType = SymbolTable.lookupClass(method.type);
                    ClassNode exprType = SymbolTable.lookupClass(s.getName());
                    if(idType != null && exprType != null)
                        if(!ClassNode.isSubType(exprType, idType))
                            SymbolTable.error(method.ctx, method.exprToken,
                                    "Type " + s.getName() + " of the body of method " + method.id +
                                            " is incompatible with declared return type " + method.type);

                }

                return new Symbol(method.type);
            }

            @Override
            public Symbol visit(Atribute atribute) {
                ClassNode c = SymbolTable.classMap.get(curClass);

                DefaultScope ds = SymbolTable.scopeMap.get(c.parent);
                Symbol s = null;
                if(ds != null)
                    s =  ds.lookup(atribute.id);

                if(s != null){ //redefinit inherited
                    SymbolTable.error(atribute.ctx, atribute.idToken, "Class " + curClass +
                            " redefines inherited attribute " + atribute.id);
                }

                if(SymbolTable.lookupClass(atribute.Type) == null && !atribute.error && !atribute.Type.equals("SELF_TYPE")){
                    SymbolTable.error(atribute.ctx, atribute.typeToken, "Class " + curClass +
                            " has attribute y with undefined type " + atribute.Type);
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
                    if(idType != null && exprType != null)
                        if(!ClassNode.isSubType(exprType, idType))
                            SymbolTable.error(atribute.ctx, atribute.exprToken,
                                    "Type " + sym.getName() + " of initialization expression of attribute "
                                            + atribute.id + " is incompatible with declared type " + atribute.Type);
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
                            SymbolTable.error(indisp.ctx, indisp.ls.get(0).token,
                                    "Method " + indisp.id + " of class " + curClass +
                                            " is applied to wrong number of arguments");
                        } else {
                            for(int i = 0; i < paramTypes.size(); i++){
                                ClassNode c1 = SymbolTable.lookupClass(ftypes.args.get(i + 1).getName());
                                ClassNode c2 = SymbolTable.lookupClass(paramTypes.get(i).getName());
                                if( !ClassNode.isSubType(c2, c1) ){
                                    SymbolTable.error(indisp.ctx, indisp.ls.get(i).token,
                                            "In call to method " + indisp.id +
                                                    " of class " + curClass +
                                                    ", actual type " + paramTypes.get(i).getName() +
                                                    " of formal parameter " + fids.args.get(i).getName() +
                                                    " is incompatible with declared type "
                                                    + ftypes.args.get(i + 1).getName());
                                }
                            }
                        }
                        return new Symbol(ftypes.args.get(0).getName());
                    }else {
                        SymbolTable.error(indisp.ctx, indisp.idToken,
                                "Undefined method " + indisp.id + " in class " + curClass);
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
                if(s!=null)
                    exprType = SymbolTable.lookupClass(s.getName());

                if(exprType != null){
                    ClassNode staticType = null;

                    if(disp.type != null) {
                        if (disp.type.equals("SELF_TYPE")) {
                            SymbolTable.error(disp.ctx, disp.staticToken,
                                    "Type of static dispatch cannot be SELF_TYPE");
                        }
                        else{
                            staticType = SymbolTable.lookupClass(disp.type);
                            if(staticType == null){
                                SymbolTable.error(disp.ctx, disp.staticToken,
                                        "Type " + disp.type + " of static dispatch is undefined");
                            } else {
                                if(!ClassNode.isSubType(exprType, staticType)){
                                    staticType = null;
                                    SymbolTable.error(disp.ctx, disp.staticToken,
                                            "Type "+ disp.type +
                                                    " of static dispatch is not a superclass of type " + s.getName());
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
                            SymbolTable.error(disp.ctx, disp.idToken,
                                    "Method " + disp.id + " of class " + s.getName() +
                                            " is applied to wrong number of arguments");
                        } else {
                            for(int i = 1; i < ftypes.args.size(); i++){
                                ClassNode c1 = SymbolTable.lookupClass(ftypes.args.get(i).getName());
                                ClassNode c2 = SymbolTable.lookupClass(paramTypes.get(i-1).getName());
                                if( !ClassNode.isSubType(c2, c1) ){
                                    SymbolTable.error(disp.ctx, disp.ls.get(i).token,
                                            "In call to method " + disp.id +
                                                    " of class " + s.getName() +
                                                    ", actual type " + paramTypes.get(i-1).getName() +
                                                    " of formal parameter " + fids.args.get(i - 1).getName() +
                                                    " is incompatible with declared type " + ftypes.args.get(i).getName());
                                }
                            }
                        }
                        return new Symbol(ftypes.args.get(0).getName());
                    } else {
                        if(staticType != null){
                            SymbolTable.error(disp.ctx, disp.idToken,
                                    "Undefined method " + disp.id + " in class " + disp.type);
                        } else {
                            SymbolTable.error(disp.ctx, disp.idToken,
                                    "Undefined method " + disp.id + " in class " + s.getName());
                        }
                    }
                } else {
                    System.out.println("class not found");
                }

                return null;
            }
        };

        ast.accept(definitionPassVisitor);
        ast.accept(resolutionPassVisitor);

        if (SymbolTable.hasSemanticErrors()) {
            System.err.println("Compilation halted");
            return;
        }
    }
}
