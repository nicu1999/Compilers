parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

program : class1+ EOF ;

class1 : CLASS TYPE (INHERITS TYPE)? LBRACE (value+=feature)* RBRACE SC;

feature : ID LPAREN (formal (COMMA formal)*)? RPAREN COL TYPE LBRACE expr RBRACE SC #method
        | ID COL TYPE (ASSIGN expr)? SC #atribute
        ;

formal : ID COL TYPE;

let_branch : ID COL TYPE (ASSIGN expr)?;

expr : ID LPAREN (expr (COMMA expr)*)? RPAREN #indisp
     | pre=expr (AT TYPE)? DOT ID LPAREN (expr (COMMA expr)*)? RPAREN #disp
     | IF expr THEN expr ELSE expr FI #if
     | WHILE expr LOOP expr POOL #while
     | LET let_branch (COMMA let_branch)* IN expr #let
     | CASE expr OF (ID COL TYPE RA expr SC)+ ESAC #case
     | ISVOID expr #isvoid
     | NEW TYPE #new_type
     | LPAREN expr RPAREN #par
     | LBRACE (expr SC)+ RBRACE #brace
     | NEG expr #neg
     | ID #id
     | INT #int
     | STRING #string
     | TRUE #true
     | FALSE #false
     | expr MUL expr #mul
     | expr DIV expr #div
     | expr PLUS expr #plus
     | expr MIN expr #min
     | expr LESS expr #less
     | expr LESSEQ expr #lesseq
     | expr EQ expr #eq
     | ID ASSIGN expr #assign
     | NOT expr #not
     ;