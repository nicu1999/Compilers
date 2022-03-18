lexer grammar CoolLexer;

tokens { ERROR }

@header{
    package cool.lexer;	
}

@members{    
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}
fragment A : [aA]; // match either an 'a' or 'A'
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

IF: I F;
THEN: T H E N;
ELSE: E L S E;
FI: F I;
WHILE: W H I L E;
LOOP: L O O P;
POOL: P O O L;
LET: L E T;
IN: I N;
NEW: N E W;
CLASS: C L A S S;
INHERITS: I N H E R I T S;
CASE: C A S E;
OF: O F;
ESAC: E S A C;
ISVOID: I S V O I D;
NOT: N O T;
MUL: '*';
DIV: '/';
EQ: '=';
AT: '@';
DOT: '.';
SC: ';';
COL: ':';
LESS: '<';
LESSEQ: '<=';
RA: '=>';
COMMA: ',';
ASSIGN: '<-';
MIN: '-';
NEG: '~';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
PLUS: '+';


TRUE : 't' R U E;
FALSE : 'f' A L S E;

fragment LOWERCASE: [a-z];
fragment UPPERCASE: [A-Z];
fragment LETTER : (LOWERCASE | UPPERCASE);
fragment LETTERS : (LOWERCASE | UPPERCASE)+;

fragment DIGIT : [0-9];
INT : DIGIT+;

TYPE : UPPERCASE ID_NAME*;
ID : LOWERCASE ID_NAME*;
fragment ID_NAME : (INT | LETTERS | '_')+;


STRING : '"' ('\\"'|.)*? '"';

BLOCK_COMMENT:'(*' (BLOCK_COMMENT | .)*? '*)' -> skip;
LINE_COMMENT: '--' .*? ('\r'? '\n' | EOF) -> skip;

WS : [ \n\f\r\t]+ -> skip;

INVALID: . ;