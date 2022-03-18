// Generated from C:/Users/Nicu/Downloads/Tema1/src/cool/lexer\CoolLexer.g4 by ANTLR 4.9.2

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
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ERROR=1, IF=2, THEN=3, ELSE=4, FI=5, WHILE=6, LOOP=7, POOL=8, LET=9, IN=10, 
		NEW=11, CLASS=12, INHERITS=13, CASE=14, OF=15, ESAC=16, ISVOID=17, NOT=18, 
		MUL=19, DIV=20, EQ=21, AT=22, DOT=23, SC=24, COL=25, LESS=26, LESSEQ=27, 
		RA=28, COMMA=29, ASSIGN=30, MIN=31, NEG=32, LPAREN=33, RPAREN=34, LBRACE=35, 
		RBRACE=36, PLUS=37, TRUE=38, FALSE=39, INT=40, REAL=41, TYPE=42, ID=43, 
		STRING=44, BLOCK_COMMENT=45, LINE_COMMENT=46, WS=47, INVALID=48;
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
			"UPPERCASE", "LETTER", "LETTERS", "DIGIT", "INT", "FRACTION", "EXPONENT", 
			"REAL", "TYPE", "ID", "ID_NAME", "STRING", "BLOCK_COMMENT", "LINE_COMMENT", 
			"WS", "INVALID"
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
			"PLUS", "TRUE", "FALSE", "INT", "REAL", "TYPE", "ID", "STRING", "BLOCK_COMMENT", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\62\u01cf\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\3\2\3\2\3\3"+
		"\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23"+
		"\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36"+
		"\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3\"\3\"\3\""+
		"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3\'\3\'\3"+
		"\'\3\'\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3*\3*\3*\3*\3*\3+\3"+
		"+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62"+
		"\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\3\66\3\66\3\66\3\67\3\67\38\3"+
		"8\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3@\3@\3@\3A\3A\3"+
		"A\3A\3A\3A\3B\3B\3C\3C\3D\3D\5D\u0166\nD\3E\3E\6E\u016a\nE\rE\16E\u016b"+
		"\3F\3F\3G\6G\u0171\nG\rG\16G\u0172\3H\3H\5H\u0177\nH\5H\u0179\nH\3I\3"+
		"I\5I\u017d\nI\3I\5I\u0180\nI\3J\3J\3J\3J\3K\3K\7K\u0188\nK\fK\16K\u018b"+
		"\13K\3L\3L\7L\u018f\nL\fL\16L\u0192\13L\3M\3M\3M\6M\u0197\nM\rM\16M\u0198"+
		"\3N\3N\3N\3N\7N\u019f\nN\fN\16N\u01a2\13N\3N\3N\3O\3O\3O\3O\3O\7O\u01ab"+
		"\nO\fO\16O\u01ae\13O\3O\3O\3O\3O\3O\3P\3P\3P\3P\7P\u01b9\nP\fP\16P\u01bc"+
		"\13P\3P\5P\u01bf\nP\3P\3P\5P\u01c3\nP\3P\3P\3Q\6Q\u01c8\nQ\rQ\16Q\u01c9"+
		"\3Q\3Q\3R\3R\5\u01a0\u01ac\u01ba\2S\3\2\5\2\7\2\t\2\13\2\r\2\17\2\21\2"+
		"\23\2\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2"+
		"\65\2\67\49\5;\6=\7?\bA\tC\nE\13G\fI\rK\16M\17O\20Q\21S\22U\23W\24Y\25"+
		"[\26]\27_\30a\31c\32e\33g\34i\35k\36m\37o q!s\"u#w$y%{&}\'\177(\u0081"+
		")\u0083\2\u0085\2\u0087\2\u0089\2\u008b\2\u008d*\u008f\2\u0091\2\u0093"+
		"+\u0095,\u0097-\u0099\2\u009b.\u009d/\u009f\60\u00a1\61\u00a3\62\3\2!"+
		"\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2K"+
		"Kkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4"+
		"\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\"+
		"||\3\2c|\3\2C\\\3\2\62;\4\2--//\5\2\13\f\16\17\"\"\2\u01c1\2\67\3\2\2"+
		"\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2"+
		"E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3"+
		"\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2"+
		"\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2"+
		"k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3"+
		"\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2"+
		"\2\u008d\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u009b"+
		"\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2"+
		"\2\3\u00a5\3\2\2\2\5\u00a7\3\2\2\2\7\u00a9\3\2\2\2\t\u00ab\3\2\2\2\13"+
		"\u00ad\3\2\2\2\r\u00af\3\2\2\2\17\u00b1\3\2\2\2\21\u00b3\3\2\2\2\23\u00b5"+
		"\3\2\2\2\25\u00b7\3\2\2\2\27\u00b9\3\2\2\2\31\u00bb\3\2\2\2\33\u00bd\3"+
		"\2\2\2\35\u00bf\3\2\2\2\37\u00c1\3\2\2\2!\u00c3\3\2\2\2#\u00c5\3\2\2\2"+
		"%\u00c7\3\2\2\2\'\u00c9\3\2\2\2)\u00cb\3\2\2\2+\u00cd\3\2\2\2-\u00cf\3"+
		"\2\2\2/\u00d1\3\2\2\2\61\u00d3\3\2\2\2\63\u00d5\3\2\2\2\65\u00d7\3\2\2"+
		"\2\67\u00d9\3\2\2\29\u00dc\3\2\2\2;\u00e1\3\2\2\2=\u00e6\3\2\2\2?\u00e9"+
		"\3\2\2\2A\u00ef\3\2\2\2C\u00f4\3\2\2\2E\u00f9\3\2\2\2G\u00fd\3\2\2\2I"+
		"\u0100\3\2\2\2K\u0104\3\2\2\2M\u010a\3\2\2\2O\u0113\3\2\2\2Q\u0118\3\2"+
		"\2\2S\u011b\3\2\2\2U\u0120\3\2\2\2W\u0127\3\2\2\2Y\u012b\3\2\2\2[\u012d"+
		"\3\2\2\2]\u012f\3\2\2\2_\u0131\3\2\2\2a\u0133\3\2\2\2c\u0135\3\2\2\2e"+
		"\u0137\3\2\2\2g\u0139\3\2\2\2i\u013b\3\2\2\2k\u013e\3\2\2\2m\u0141\3\2"+
		"\2\2o\u0143\3\2\2\2q\u0146\3\2\2\2s\u0148\3\2\2\2u\u014a\3\2\2\2w\u014c"+
		"\3\2\2\2y\u014e\3\2\2\2{\u0150\3\2\2\2}\u0152\3\2\2\2\177\u0154\3\2\2"+
		"\2\u0081\u0159\3\2\2\2\u0083\u015f\3\2\2\2\u0085\u0161\3\2\2\2\u0087\u0165"+
		"\3\2\2\2\u0089\u0169\3\2\2\2\u008b\u016d\3\2\2\2\u008d\u0170\3\2\2\2\u008f"+
		"\u0178\3\2\2\2\u0091\u017f\3\2\2\2\u0093\u0181\3\2\2\2\u0095\u0185\3\2"+
		"\2\2\u0097\u018c\3\2\2\2\u0099\u0196\3\2\2\2\u009b\u019a\3\2\2\2\u009d"+
		"\u01a5\3\2\2\2\u009f\u01b4\3\2\2\2\u00a1\u01c7\3\2\2\2\u00a3\u01cd\3\2"+
		"\2\2\u00a5\u00a6\t\2\2\2\u00a6\4\3\2\2\2\u00a7\u00a8\t\3\2\2\u00a8\6\3"+
		"\2\2\2\u00a9\u00aa\t\4\2\2\u00aa\b\3\2\2\2\u00ab\u00ac\t\5\2\2\u00ac\n"+
		"\3\2\2\2\u00ad\u00ae\t\6\2\2\u00ae\f\3\2\2\2\u00af\u00b0\t\7\2\2\u00b0"+
		"\16\3\2\2\2\u00b1\u00b2\t\b\2\2\u00b2\20\3\2\2\2\u00b3\u00b4\t\t\2\2\u00b4"+
		"\22\3\2\2\2\u00b5\u00b6\t\n\2\2\u00b6\24\3\2\2\2\u00b7\u00b8\t\13\2\2"+
		"\u00b8\26\3\2\2\2\u00b9\u00ba\t\f\2\2\u00ba\30\3\2\2\2\u00bb\u00bc\t\r"+
		"\2\2\u00bc\32\3\2\2\2\u00bd\u00be\t\16\2\2\u00be\34\3\2\2\2\u00bf\u00c0"+
		"\t\17\2\2\u00c0\36\3\2\2\2\u00c1\u00c2\t\20\2\2\u00c2 \3\2\2\2\u00c3\u00c4"+
		"\t\21\2\2\u00c4\"\3\2\2\2\u00c5\u00c6\t\22\2\2\u00c6$\3\2\2\2\u00c7\u00c8"+
		"\t\23\2\2\u00c8&\3\2\2\2\u00c9\u00ca\t\24\2\2\u00ca(\3\2\2\2\u00cb\u00cc"+
		"\t\25\2\2\u00cc*\3\2\2\2\u00cd\u00ce\t\26\2\2\u00ce,\3\2\2\2\u00cf\u00d0"+
		"\t\27\2\2\u00d0.\3\2\2\2\u00d1\u00d2\t\30\2\2\u00d2\60\3\2\2\2\u00d3\u00d4"+
		"\t\31\2\2\u00d4\62\3\2\2\2\u00d5\u00d6\t\32\2\2\u00d6\64\3\2\2\2\u00d7"+
		"\u00d8\t\33\2\2\u00d8\66\3\2\2\2\u00d9\u00da\5\23\n\2\u00da\u00db\5\r"+
		"\7\2\u00db8\3\2\2\2\u00dc\u00dd\5)\25\2\u00dd\u00de\5\21\t\2\u00de\u00df"+
		"\5\13\6\2\u00df\u00e0\5\35\17\2\u00e0:\3\2\2\2\u00e1\u00e2\5\13\6\2\u00e2"+
		"\u00e3\5\31\r\2\u00e3\u00e4\5\'\24\2\u00e4\u00e5\5\13\6\2\u00e5<\3\2\2"+
		"\2\u00e6\u00e7\5\r\7\2\u00e7\u00e8\5\23\n\2\u00e8>\3\2\2\2\u00e9\u00ea"+
		"\5/\30\2\u00ea\u00eb\5\21\t\2\u00eb\u00ec\5\23\n\2\u00ec\u00ed\5\31\r"+
		"\2\u00ed\u00ee\5\13\6\2\u00ee@\3\2\2\2\u00ef\u00f0\5\31\r\2\u00f0\u00f1"+
		"\5\37\20\2\u00f1\u00f2\5\37\20\2\u00f2\u00f3\5!\21\2\u00f3B\3\2\2\2\u00f4"+
		"\u00f5\5!\21\2\u00f5\u00f6\5\37\20\2\u00f6\u00f7\5\37\20\2\u00f7\u00f8"+
		"\5\31\r\2\u00f8D\3\2\2\2\u00f9\u00fa\5\31\r\2\u00fa\u00fb\5\13\6\2\u00fb"+
		"\u00fc\5)\25\2\u00fcF\3\2\2\2\u00fd\u00fe\5\23\n\2\u00fe\u00ff\5\35\17"+
		"\2\u00ffH\3\2\2\2\u0100\u0101\5\35\17\2\u0101\u0102\5\13\6\2\u0102\u0103"+
		"\5/\30\2\u0103J\3\2\2\2\u0104\u0105\5\7\4\2\u0105\u0106\5\31\r\2\u0106"+
		"\u0107\5\3\2\2\u0107\u0108\5\'\24\2\u0108\u0109\5\'\24\2\u0109L\3\2\2"+
		"\2\u010a\u010b\5\23\n\2\u010b\u010c\5\35\17\2\u010c\u010d\5\21\t\2\u010d"+
		"\u010e\5\13\6\2\u010e\u010f\5%\23\2\u010f\u0110\5\23\n\2\u0110\u0111\5"+
		")\25\2\u0111\u0112\5\'\24\2\u0112N\3\2\2\2\u0113\u0114\5\7\4\2\u0114\u0115"+
		"\5\3\2\2\u0115\u0116\5\'\24\2\u0116\u0117\5\13\6\2\u0117P\3\2\2\2\u0118"+
		"\u0119\5\37\20\2\u0119\u011a\5\r\7\2\u011aR\3\2\2\2\u011b\u011c\5\13\6"+
		"\2\u011c\u011d\5\'\24\2\u011d\u011e\5\3\2\2\u011e\u011f\5\7\4\2\u011f"+
		"T\3\2\2\2\u0120\u0121\5\23\n\2\u0121\u0122\5\'\24\2\u0122\u0123\5-\27"+
		"\2\u0123\u0124\5\37\20\2\u0124\u0125\5\23\n\2\u0125\u0126\5\t\5\2\u0126"+
		"V\3\2\2\2\u0127\u0128\5\35\17\2\u0128\u0129\5\37\20\2\u0129\u012a\5)\25"+
		"\2\u012aX\3\2\2\2\u012b\u012c\7,\2\2\u012cZ\3\2\2\2\u012d\u012e\7\61\2"+
		"\2\u012e\\\3\2\2\2\u012f\u0130\7?\2\2\u0130^\3\2\2\2\u0131\u0132\7B\2"+
		"\2\u0132`\3\2\2\2\u0133\u0134\7\60\2\2\u0134b\3\2\2\2\u0135\u0136\7=\2"+
		"\2\u0136d\3\2\2\2\u0137\u0138\7<\2\2\u0138f\3\2\2\2\u0139\u013a\7>\2\2"+
		"\u013ah\3\2\2\2\u013b\u013c\7>\2\2\u013c\u013d\7?\2\2\u013dj\3\2\2\2\u013e"+
		"\u013f\7?\2\2\u013f\u0140\7@\2\2\u0140l\3\2\2\2\u0141\u0142\7.\2\2\u0142"+
		"n\3\2\2\2\u0143\u0144\7>\2\2\u0144\u0145\7/\2\2\u0145p\3\2\2\2\u0146\u0147"+
		"\7/\2\2\u0147r\3\2\2\2\u0148\u0149\7\u0080\2\2\u0149t\3\2\2\2\u014a\u014b"+
		"\7*\2\2\u014bv\3\2\2\2\u014c\u014d\7+\2\2\u014dx\3\2\2\2\u014e\u014f\7"+
		"}\2\2\u014fz\3\2\2\2\u0150\u0151\7\177\2\2\u0151|\3\2\2\2\u0152\u0153"+
		"\7-\2\2\u0153~\3\2\2\2\u0154\u0155\7v\2\2\u0155\u0156\5%\23\2\u0156\u0157"+
		"\5+\26\2\u0157\u0158\5\13\6\2\u0158\u0080\3\2\2\2\u0159\u015a\7h\2\2\u015a"+
		"\u015b\5\3\2\2\u015b\u015c\5\31\r\2\u015c\u015d\5\'\24\2\u015d\u015e\5"+
		"\13\6\2\u015e\u0082\3\2\2\2\u015f\u0160\t\34\2\2\u0160\u0084\3\2\2\2\u0161"+
		"\u0162\t\35\2\2\u0162\u0086\3\2\2\2\u0163\u0166\5\u0083B\2\u0164\u0166"+
		"\5\u0085C\2\u0165\u0163\3\2\2\2\u0165\u0164\3\2\2\2\u0166\u0088\3\2\2"+
		"\2\u0167\u016a\5\u0083B\2\u0168\u016a\5\u0085C\2\u0169\u0167\3\2\2\2\u0169"+
		"\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2"+
		"\2\2\u016c\u008a\3\2\2\2\u016d\u016e\t\36\2\2\u016e\u008c\3\2\2\2\u016f"+
		"\u0171\5\u008bF\2\u0170\u016f\3\2\2\2\u0171\u0172\3\2\2\2\u0172\u0170"+
		"\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u008e\3\2\2\2\u0174\u0176\7\60\2\2"+
		"\u0175\u0177\5\u008dG\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177"+
		"\u0179\3\2\2\2\u0178\u0174\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u0090\3\2"+
		"\2\2\u017a\u017c\7g\2\2\u017b\u017d\t\37\2\2\u017c\u017b\3\2\2\2\u017c"+
		"\u017d\3\2\2\2\u017d\u017e\3\2\2\2\u017e\u0180\5\u008dG\2\u017f\u017a"+
		"\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u0092\3\2\2\2\u0181\u0182\5\u008dG"+
		"\2\u0182\u0183\5\u008fH\2\u0183\u0184\5\u0091I\2\u0184\u0094\3\2\2\2\u0185"+
		"\u0189\5\u0085C\2\u0186\u0188\5\u0099M\2\u0187\u0186\3\2\2\2\u0188\u018b"+
		"\3\2\2\2\u0189\u0187\3\2\2\2\u0189\u018a\3\2\2\2\u018a\u0096\3\2\2\2\u018b"+
		"\u0189\3\2\2\2\u018c\u0190\5\u0083B\2\u018d\u018f\5\u0099M\2\u018e\u018d"+
		"\3\2\2\2\u018f\u0192\3\2\2\2\u0190\u018e\3\2\2\2\u0190\u0191\3\2\2\2\u0191"+
		"\u0098\3\2\2\2\u0192\u0190\3\2\2\2\u0193\u0197\5\u008dG\2\u0194\u0197"+
		"\5\u0089E\2\u0195\u0197\7a\2\2\u0196\u0193\3\2\2\2\u0196\u0194\3\2\2\2"+
		"\u0196\u0195\3\2\2\2\u0197\u0198\3\2\2\2\u0198\u0196\3\2\2\2\u0198\u0199"+
		"\3\2\2\2\u0199\u009a\3\2\2\2\u019a\u01a0\7$\2\2\u019b\u019c\7^\2\2\u019c"+
		"\u019f\7$\2\2\u019d\u019f\13\2\2\2\u019e\u019b\3\2\2\2\u019e\u019d\3\2"+
		"\2\2\u019f\u01a2\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a0\u019e\3\2\2\2\u01a1"+
		"\u01a3\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a3\u01a4\7$\2\2\u01a4\u009c\3\2"+
		"\2\2\u01a5\u01a6\7*\2\2\u01a6\u01a7\7,\2\2\u01a7\u01ac\3\2\2\2\u01a8\u01ab"+
		"\5\u009dO\2\u01a9\u01ab\13\2\2\2\u01aa\u01a8\3\2\2\2\u01aa\u01a9\3\2\2"+
		"\2\u01ab\u01ae\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01af"+
		"\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b0\7,\2\2\u01b0\u01b1\7+\2\2\u01b1"+
		"\u01b2\3\2\2\2\u01b2\u01b3\bO\2\2\u01b3\u009e\3\2\2\2\u01b4\u01b5\7/\2"+
		"\2\u01b5\u01b6\7/\2\2\u01b6\u01ba\3\2\2\2\u01b7\u01b9\13\2\2\2\u01b8\u01b7"+
		"\3\2\2\2\u01b9\u01bc\3\2\2\2\u01ba\u01bb\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb"+
		"\u01c2\3\2\2\2\u01bc\u01ba\3\2\2\2\u01bd\u01bf\7\17\2\2\u01be\u01bd\3"+
		"\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c3\7\f\2\2\u01c1"+
		"\u01c3\7\2\2\3\u01c2\u01be\3\2\2\2\u01c2\u01c1\3\2\2\2\u01c3\u01c4\3\2"+
		"\2\2\u01c4\u01c5\bP\2\2\u01c5\u00a0\3\2\2\2\u01c6\u01c8\t \2\2\u01c7\u01c6"+
		"\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca"+
		"\u01cb\3\2\2\2\u01cb\u01cc\bQ\2\2\u01cc\u00a2\3\2\2\2\u01cd\u01ce\13\2"+
		"\2\2\u01ce\u00a4\3\2\2\2\27\2\u0165\u0169\u016b\u0172\u0176\u0178\u017c"+
		"\u017f\u0189\u0190\u0196\u0198\u019e\u01a0\u01aa\u01ac\u01ba\u01be\u01c2"+
		"\u01c9\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}