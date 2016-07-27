package br.cefetrj.sagitarii.parser;

// Generated from d:\parser\src\main\antl4\Sagitarii.g by ANTLR 4.5.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SagitariiLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, OPERATOR=5, TO=6, AS=7, TEXT=8, WHITESPACE=9;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "OPERATOR", "TO", "AS", "TEXT", "WHITESPACE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "','", "')'", "';'", null, "'to'", "'as'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, "OPERATOR", "TO", "AS", "TEXT", "WHITESPACE"
	};
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


	public SagitariiLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Sagitarii.g"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\13E\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2"+
		"\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\62\n\6\3\7\3\7\3\7\3\b\3\b\3\b"+
		"\3\t\6\t;\n\t\r\t\16\t<\3\n\6\n@\n\n\r\n\16\nA\3\n\3\n\2\2\13\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\3\2\4\5\2\62;C\\c|\5\2\13\f\16\17\""+
		"\"I\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3"+
		"\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\3\25\3\2\2\2\5\27\3\2\2"+
		"\2\7\31\3\2\2\2\t\33\3\2\2\2\13\61\3\2\2\2\r\63\3\2\2\2\17\66\3\2\2\2"+
		"\21:\3\2\2\2\23?\3\2\2\2\25\26\7*\2\2\26\4\3\2\2\2\27\30\7.\2\2\30\6\3"+
		"\2\2\2\31\32\7+\2\2\32\b\3\2\2\2\33\34\7=\2\2\34\n\3\2\2\2\35\36\7o\2"+
		"\2\36\37\7c\2\2\37\62\7r\2\2 !\7u\2\2!\"\7r\2\2\"#\7n\2\2#$\7k\2\2$\62"+
		"\7v\2\2%&\7t\2\2&\'\7g\2\2\'(\7f\2\2()\7w\2\2)*\7e\2\2*\62\7g\2\2+,\7"+
		"u\2\2,-\7g\2\2-.\7n\2\2./\7g\2\2/\60\7e\2\2\60\62\7v\2\2\61\35\3\2\2\2"+
		"\61 \3\2\2\2\61%\3\2\2\2\61+\3\2\2\2\62\f\3\2\2\2\63\64\7v\2\2\64\65\7"+
		"q\2\2\65\16\3\2\2\2\66\67\7c\2\2\678\7u\2\28\20\3\2\2\29;\t\2\2\2:9\3"+
		"\2\2\2;<\3\2\2\2<:\3\2\2\2<=\3\2\2\2=\22\3\2\2\2>@\t\3\2\2?>\3\2\2\2@"+
		"A\3\2\2\2A?\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\b\n\2\2D\24\3\2\2\2\6\2\61<"+
		"A\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}