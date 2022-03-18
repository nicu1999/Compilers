// Generated from /home/student/COMPILATOARE/TEMA3/src/cool/lexer/CoolLexer.g4 by ANTLR 4.8

    package cool.lexer;	

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CoolLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, IF=2, THEN=3, ELSE=4, FI=5, WHILE=6, LOOP=7, POOL=8, LET=9, IN=10, 
		NEW=11, CLASS=12, INHERITS=13, CASE=14, OF=15, ESAC=16, ISVOID=17, NOT=18, 
		MUL=19, DIV=20, EQ=21, AT=22, DOT=23, SC=24, COL=25, LESS=26, LESSEQ=27, 
		RA=28, COMMA=29, ASSIGN=30, MIN=31, NEG=32, LPAREN=33, RPAREN=34, LBRACE=35, 
		RBRACE=36, PLUS=37, TRUE=38, FALSE=39, INT=40, TYPE=41, ID=42, STRING=43, 
		BLOCK_COMMENT=44, LINE_COMMENT=45, WS=46, INVALID=47;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "IF", "THEN", 
			"ELSE", "FI", "WHILE", "LOOP", "POOL", "LET", "IN", "NEW", "CLASS", "INHERITS", 
			"CASE", "OF", "ESAC", "ISVOID", "NOT", "MUL", "DIV", "EQ", "AT", "DOT", 
			"SC", "COL", "LESS", "LESSEQ", "RA", "COMMA", "ASSIGN", "MIN", "NEG", 
			"LPAREN", "RPAREN", "LBRACE", "RBRACE", "PLUS", "TRUE", "FALSE", "LOWERCASE", 
			"UPPERCASE", "LETTER", "LETTERS", "DIGIT", "INT", "TYPE", "ID", "ID_NAME", 
			"STRING", "BLOCK_COMMENT", "LINE_COMMENT", "WS", "INVALID"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "'*'", "'/'", "'='", "'@'", 
			"'.'", "';'", "':'", "'<'", "'<='", "'=>'", "','", "'<-'", "'-'", "'~'", 
			"'('", "')'", "'{'", "'}'", "'+'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ERROR", "IF", "THEN", "ELSE", "FI", "WHILE", "LOOP", "POOL", "LET", 
			"IN", "NEW", "CLASS", "INHERITS", "CASE", "OF", "ESAC", "ISVOID", "NOT", 
			"MUL", "DIV", "EQ", "AT", "DOT", "SC", "COL", "LESS", "LESSEQ", "RA", 
			"COMMA", "ASSIGN", "MIN", "NEG", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"PLUS", "TRUE", "FALSE", "INT", "TYPE", "ID", "STRING", "BLOCK_COMMENT", 
			"LINE_COMMENT", "WS", "INVALID"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	    
	    private void raiseError(String msg) {
	        setText(msg);
	        setType(ERROR);
	    }


	public CoolLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CoolLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\61\u01b8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\37\3"+
		"\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3#\3#\3"+
		"#\3#\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3"+
		"\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3"+
		"+\3,\3,\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63"+
		"\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66\3\67\3\67\38\38\38\39\39\3:\3"+
		":\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3@\3@\3@\3A\3A\3A\3A\3A\3A\3B\3"+
		"B\3C\3C\3D\3D\5D\u0160\nD\3E\3E\6E\u0164\nE\rE\16E\u0165\3F\3F\3G\6G\u016b"+
		"\nG\rG\16G\u016c\3H\3H\7H\u0171\nH\fH\16H\u0174\13H\3I\3I\7I\u0178\nI"+
		"\fI\16I\u017b\13I\3J\3J\3J\6J\u0180\nJ\rJ\16J\u0181\3K\3K\3K\3K\7K\u0188"+
		"\nK\fK\16K\u018b\13K\3K\3K\3L\3L\3L\3L\3L\7L\u0194\nL\fL\16L\u0197\13"+
		"L\3L\3L\3L\3L\3L\3M\3M\3M\3M\7M\u01a2\nM\fM\16M\u01a5\13M\3M\5M\u01a8"+
		"\nM\3M\3M\5M\u01ac\nM\3M\3M\3N\6N\u01b1\nN\rN\16N\u01b2\3N\3N\3O\3O\5"+
		"\u0189\u0195\u01a3\2P\3\2\5\2\7\2\t\2\13\2\r\2\17\2\21\2\23\2\25\2\27"+
		"\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\49\5"+
		";\6=\7?\bA\tC\nE\13G\fI\rK\16M\17O\20Q\21S\22U\23W\24Y\25[\26]\27_\30"+
		"a\31c\32e\33g\34i\35k\36m\37o q!s\"u#w$y%{&}\'\177(\u0081)\u0083\2\u0085"+
		"\2\u0087\2\u0089\2\u008b\2\u008d*\u008f+\u0091,\u0093\2\u0095-\u0097."+
		"\u0099/\u009b\60\u009d\61\3\2 \4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGg"+
		"g\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2"+
		"PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4"+
		"\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\3\2c|\3\2C\\\3\2\62;\5\2\13\f\16\17\""+
		"\"\2\u01a8\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2"+
		"A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3"+
		"\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2"+
		"\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2"+
		"g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3"+
		"\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3"+
		"\2\2\2\2\u0081\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2"+
		"\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d"+
		"\3\2\2\2\3\u009f\3\2\2\2\5\u00a1\3\2\2\2\7\u00a3\3\2\2\2\t\u00a5\3\2\2"+
		"\2\13\u00a7\3\2\2\2\r\u00a9\3\2\2\2\17\u00ab\3\2\2\2\21\u00ad\3\2\2\2"+
		"\23\u00af\3\2\2\2\25\u00b1\3\2\2\2\27\u00b3\3\2\2\2\31\u00b5\3\2\2\2\33"+
		"\u00b7\3\2\2\2\35\u00b9\3\2\2\2\37\u00bb\3\2\2\2!\u00bd\3\2\2\2#\u00bf"+
		"\3\2\2\2%\u00c1\3\2\2\2\'\u00c3\3\2\2\2)\u00c5\3\2\2\2+\u00c7\3\2\2\2"+
		"-\u00c9\3\2\2\2/\u00cb\3\2\2\2\61\u00cd\3\2\2\2\63\u00cf\3\2\2\2\65\u00d1"+
		"\3\2\2\2\67\u00d3\3\2\2\29\u00d6\3\2\2\2;\u00db\3\2\2\2=\u00e0\3\2\2\2"+
		"?\u00e3\3\2\2\2A\u00e9\3\2\2\2C\u00ee\3\2\2\2E\u00f3\3\2\2\2G\u00f7\3"+
		"\2\2\2I\u00fa\3\2\2\2K\u00fe\3\2\2\2M\u0104\3\2\2\2O\u010d\3\2\2\2Q\u0112"+
		"\3\2\2\2S\u0115\3\2\2\2U\u011a\3\2\2\2W\u0121\3\2\2\2Y\u0125\3\2\2\2["+
		"\u0127\3\2\2\2]\u0129\3\2\2\2_\u012b\3\2\2\2a\u012d\3\2\2\2c\u012f\3\2"+
		"\2\2e\u0131\3\2\2\2g\u0133\3\2\2\2i\u0135\3\2\2\2k\u0138\3\2\2\2m\u013b"+
		"\3\2\2\2o\u013d\3\2\2\2q\u0140\3\2\2\2s\u0142\3\2\2\2u\u0144\3\2\2\2w"+
		"\u0146\3\2\2\2y\u0148\3\2\2\2{\u014a\3\2\2\2}\u014c\3\2\2\2\177\u014e"+
		"\3\2\2\2\u0081\u0153\3\2\2\2\u0083\u0159\3\2\2\2\u0085\u015b\3\2\2\2\u0087"+
		"\u015f\3\2\2\2\u0089\u0163\3\2\2\2\u008b\u0167\3\2\2\2\u008d\u016a\3\2"+
		"\2\2\u008f\u016e\3\2\2\2\u0091\u0175\3\2\2\2\u0093\u017f\3\2\2\2\u0095"+
		"\u0183\3\2\2\2\u0097\u018e\3\2\2\2\u0099\u019d\3\2\2\2\u009b\u01b0\3\2"+
		"\2\2\u009d\u01b6\3\2\2\2\u009f\u00a0\t\2\2\2\u00a0\4\3\2\2\2\u00a1\u00a2"+
		"\t\3\2\2\u00a2\6\3\2\2\2\u00a3\u00a4\t\4\2\2\u00a4\b\3\2\2\2\u00a5\u00a6"+
		"\t\5\2\2\u00a6\n\3\2\2\2\u00a7\u00a8\t\6\2\2\u00a8\f\3\2\2\2\u00a9\u00aa"+
		"\t\7\2\2\u00aa\16\3\2\2\2\u00ab\u00ac\t\b\2\2\u00ac\20\3\2\2\2\u00ad\u00ae"+
		"\t\t\2\2\u00ae\22\3\2\2\2\u00af\u00b0\t\n\2\2\u00b0\24\3\2\2\2\u00b1\u00b2"+
		"\t\13\2\2\u00b2\26\3\2\2\2\u00b3\u00b4\t\f\2\2\u00b4\30\3\2\2\2\u00b5"+
		"\u00b6\t\r\2\2\u00b6\32\3\2\2\2\u00b7\u00b8\t\16\2\2\u00b8\34\3\2\2\2"+
		"\u00b9\u00ba\t\17\2\2\u00ba\36\3\2\2\2\u00bb\u00bc\t\20\2\2\u00bc \3\2"+
		"\2\2\u00bd\u00be\t\21\2\2\u00be\"\3\2\2\2\u00bf\u00c0\t\22\2\2\u00c0$"+
		"\3\2\2\2\u00c1\u00c2\t\23\2\2\u00c2&\3\2\2\2\u00c3\u00c4\t\24\2\2\u00c4"+
		"(\3\2\2\2\u00c5\u00c6\t\25\2\2\u00c6*\3\2\2\2\u00c7\u00c8\t\26\2\2\u00c8"+
		",\3\2\2\2\u00c9\u00ca\t\27\2\2\u00ca.\3\2\2\2\u00cb\u00cc\t\30\2\2\u00cc"+
		"\60\3\2\2\2\u00cd\u00ce\t\31\2\2\u00ce\62\3\2\2\2\u00cf\u00d0\t\32\2\2"+
		"\u00d0\64\3\2\2\2\u00d1\u00d2\t\33\2\2\u00d2\66\3\2\2\2\u00d3\u00d4\5"+
		"\23\n\2\u00d4\u00d5\5\r\7\2\u00d58\3\2\2\2\u00d6\u00d7\5)\25\2\u00d7\u00d8"+
		"\5\21\t\2\u00d8\u00d9\5\13\6\2\u00d9\u00da\5\35\17\2\u00da:\3\2\2\2\u00db"+
		"\u00dc\5\13\6\2\u00dc\u00dd\5\31\r\2\u00dd\u00de\5\'\24\2\u00de\u00df"+
		"\5\13\6\2\u00df<\3\2\2\2\u00e0\u00e1\5\r\7\2\u00e1\u00e2\5\23\n\2\u00e2"+
		">\3\2\2\2\u00e3\u00e4\5/\30\2\u00e4\u00e5\5\21\t\2\u00e5\u00e6\5\23\n"+
		"\2\u00e6\u00e7\5\31\r\2\u00e7\u00e8\5\13\6\2\u00e8@\3\2\2\2\u00e9\u00ea"+
		"\5\31\r\2\u00ea\u00eb\5\37\20\2\u00eb\u00ec\5\37\20\2\u00ec\u00ed\5!\21"+
		"\2\u00edB\3\2\2\2\u00ee\u00ef\5!\21\2\u00ef\u00f0\5\37\20\2\u00f0\u00f1"+
		"\5\37\20\2\u00f1\u00f2\5\31\r\2\u00f2D\3\2\2\2\u00f3\u00f4\5\31\r\2\u00f4"+
		"\u00f5\5\13\6\2\u00f5\u00f6\5)\25\2\u00f6F\3\2\2\2\u00f7\u00f8\5\23\n"+
		"\2\u00f8\u00f9\5\35\17\2\u00f9H\3\2\2\2\u00fa\u00fb\5\35\17\2\u00fb\u00fc"+
		"\5\13\6\2\u00fc\u00fd\5/\30\2\u00fdJ\3\2\2\2\u00fe\u00ff\5\7\4\2\u00ff"+
		"\u0100\5\31\r\2\u0100\u0101\5\3\2\2\u0101\u0102\5\'\24\2\u0102\u0103\5"+
		"\'\24\2\u0103L\3\2\2\2\u0104\u0105\5\23\n\2\u0105\u0106\5\35\17\2\u0106"+
		"\u0107\5\21\t\2\u0107\u0108\5\13\6\2\u0108\u0109\5%\23\2\u0109\u010a\5"+
		"\23\n\2\u010a\u010b\5)\25\2\u010b\u010c\5\'\24\2\u010cN\3\2\2\2\u010d"+
		"\u010e\5\7\4\2\u010e\u010f\5\3\2\2\u010f\u0110\5\'\24\2\u0110\u0111\5"+
		"\13\6\2\u0111P\3\2\2\2\u0112\u0113\5\37\20\2\u0113\u0114\5\r\7\2\u0114"+
		"R\3\2\2\2\u0115\u0116\5\13\6\2\u0116\u0117\5\'\24\2\u0117\u0118\5\3\2"+
		"\2\u0118\u0119\5\7\4\2\u0119T\3\2\2\2\u011a\u011b\5\23\n\2\u011b\u011c"+
		"\5\'\24\2\u011c\u011d\5-\27\2\u011d\u011e\5\37\20\2\u011e\u011f\5\23\n"+
		"\2\u011f\u0120\5\t\5\2\u0120V\3\2\2\2\u0121\u0122\5\35\17\2\u0122\u0123"+
		"\5\37\20\2\u0123\u0124\5)\25\2\u0124X\3\2\2\2\u0125\u0126\7,\2\2\u0126"+
		"Z\3\2\2\2\u0127\u0128\7\61\2\2\u0128\\\3\2\2\2\u0129\u012a\7?\2\2\u012a"+
		"^\3\2\2\2\u012b\u012c\7B\2\2\u012c`\3\2\2\2\u012d\u012e\7\60\2\2\u012e"+
		"b\3\2\2\2\u012f\u0130\7=\2\2\u0130d\3\2\2\2\u0131\u0132\7<\2\2\u0132f"+
		"\3\2\2\2\u0133\u0134\7>\2\2\u0134h\3\2\2\2\u0135\u0136\7>\2\2\u0136\u0137"+
		"\7?\2\2\u0137j\3\2\2\2\u0138\u0139\7?\2\2\u0139\u013a\7@\2\2\u013al\3"+
		"\2\2\2\u013b\u013c\7.\2\2\u013cn\3\2\2\2\u013d\u013e\7>\2\2\u013e\u013f"+
		"\7/\2\2\u013fp\3\2\2\2\u0140\u0141\7/\2\2\u0141r\3\2\2\2\u0142\u0143\7"+
		"\u0080\2\2\u0143t\3\2\2\2\u0144\u0145\7*\2\2\u0145v\3\2\2\2\u0146\u0147"+
		"\7+\2\2\u0147x\3\2\2\2\u0148\u0149\7}\2\2\u0149z\3\2\2\2\u014a\u014b\7"+
		"\177\2\2\u014b|\3\2\2\2\u014c\u014d\7-\2\2\u014d~\3\2\2\2\u014e\u014f"+
		"\7v\2\2\u014f\u0150\5%\23\2\u0150\u0151\5+\26\2\u0151\u0152\5\13\6\2\u0152"+
		"\u0080\3\2\2\2\u0153\u0154\7h\2\2\u0154\u0155\5\3\2\2\u0155\u0156\5\31"+
		"\r\2\u0156\u0157\5\'\24\2\u0157\u0158\5\13\6\2\u0158\u0082\3\2\2\2\u0159"+
		"\u015a\t\34\2\2\u015a\u0084\3\2\2\2\u015b\u015c\t\35\2\2\u015c\u0086\3"+
		"\2\2\2\u015d\u0160\5\u0083B\2\u015e\u0160\5\u0085C\2\u015f\u015d\3\2\2"+
		"\2\u015f\u015e\3\2\2\2\u0160\u0088\3\2\2\2\u0161\u0164\5\u0083B\2\u0162"+
		"\u0164\5\u0085C\2\u0163\u0161\3\2\2\2\u0163\u0162\3\2\2\2\u0164\u0165"+
		"\3\2\2\2\u0165\u0163\3\2\2\2\u0165\u0166\3\2\2\2\u0166\u008a\3\2\2\2\u0167"+
		"\u0168\t\36\2\2\u0168\u008c\3\2\2\2\u0169\u016b\5\u008bF\2\u016a\u0169"+
		"\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016d"+
		"\u008e\3\2\2\2\u016e\u0172\5\u0085C\2\u016f\u0171\5\u0093J\2\u0170\u016f"+
		"\3\2\2\2\u0171\u0174\3\2\2\2\u0172\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173"+
		"\u0090\3\2\2\2\u0174\u0172\3\2\2\2\u0175\u0179\5\u0083B\2\u0176\u0178"+
		"\5\u0093J\2\u0177\u0176\3\2\2\2\u0178\u017b\3\2\2\2\u0179\u0177\3\2\2"+
		"\2\u0179\u017a\3\2\2\2\u017a\u0092\3\2\2\2\u017b\u0179\3\2\2\2\u017c\u0180"+
		"\5\u008dG\2\u017d\u0180\5\u0089E\2\u017e\u0180\7a\2\2\u017f\u017c\3\2"+
		"\2\2\u017f\u017d\3\2\2\2\u017f\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181"+
		"\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0094\3\2\2\2\u0183\u0189\7$"+
		"\2\2\u0184\u0185\7^\2\2\u0185\u0188\7$\2\2\u0186\u0188\13\2\2\2\u0187"+
		"\u0184\3\2\2\2\u0187\u0186\3\2\2\2\u0188\u018b\3\2\2\2\u0189\u018a\3\2"+
		"\2\2\u0189\u0187\3\2\2\2\u018a\u018c\3\2\2\2\u018b\u0189\3\2\2\2\u018c"+
		"\u018d\7$\2\2\u018d\u0096\3\2\2\2\u018e\u018f\7*\2\2\u018f\u0190\7,\2"+
		"\2\u0190\u0195\3\2\2\2\u0191\u0194\5\u0097L\2\u0192\u0194\13\2\2\2\u0193"+
		"\u0191\3\2\2\2\u0193\u0192\3\2\2\2\u0194\u0197\3\2\2\2\u0195\u0196\3\2"+
		"\2\2\u0195\u0193\3\2\2\2\u0196\u0198\3\2\2\2\u0197\u0195\3\2\2\2\u0198"+
		"\u0199\7,\2\2\u0199\u019a\7+\2\2\u019a\u019b\3\2\2\2\u019b\u019c\bL\2"+
		"\2\u019c\u0098\3\2\2\2\u019d\u019e\7/\2\2\u019e\u019f\7/\2\2\u019f\u01a3"+
		"\3\2\2\2\u01a0\u01a2\13\2\2\2\u01a1\u01a0\3\2\2\2\u01a2\u01a5\3\2\2\2"+
		"\u01a3\u01a4\3\2\2\2\u01a3\u01a1\3\2\2\2\u01a4\u01ab\3\2\2\2\u01a5\u01a3"+
		"\3\2\2\2\u01a6\u01a8\7\17\2\2\u01a7\u01a6\3\2\2\2\u01a7\u01a8\3\2\2\2"+
		"\u01a8\u01a9\3\2\2\2\u01a9\u01ac\7\f\2\2\u01aa\u01ac\7\2\2\3\u01ab\u01a7"+
		"\3\2\2\2\u01ab\u01aa\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01ae\bM\2\2\u01ae"+
		"\u009a\3\2\2\2\u01af\u01b1\t\37\2\2\u01b0\u01af\3\2\2\2\u01b1\u01b2\3"+
		"\2\2\2\u01b2\u01b0\3\2\2\2\u01b2\u01b3\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4"+
		"\u01b5\bN\2\2\u01b5\u009c\3\2\2\2\u01b6\u01b7\13\2\2\2\u01b7\u009e\3\2"+
		"\2\2\23\2\u015f\u0163\u0165\u016c\u0172\u0179\u017f\u0181\u0187\u0189"+
		"\u0193\u0195\u01a3\u01a7\u01ab\u01b2\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}