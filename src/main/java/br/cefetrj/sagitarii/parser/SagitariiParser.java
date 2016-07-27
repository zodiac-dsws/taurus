package br.cefetrj.sagitarii.parser;

// Generated from d:\parser\src\main\antl4\Sagitarii.g by ANTLR 4.5.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SagitariiParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, OPERATOR=5, TO=6, AS=7, TEXT=8, WHITESPACE=9;
	public static final int
		RULE_activitySentence = 0, RULE_inputSentence = 1, RULE_executor = 2, 
		RULE_input_relation = 3, RULE_output_relation = 4, RULE_activity_name = 5;
	public static final String[] ruleNames = {
		"activitySentence", "inputSentence", "executor", "input_relation", "output_relation", 
		"activity_name"
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

	@Override
	public String getGrammarFileName() { return "Sagitarii.g"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SagitariiParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ActivitySentenceContext extends ParserRuleContext {
		public TerminalNode OPERATOR() { return getToken(SagitariiParser.OPERATOR, 0); }
		public ExecutorContext executor() {
			return getRuleContext(ExecutorContext.class,0);
		}
		public List<InputSentenceContext> inputSentence() {
			return getRuleContexts(InputSentenceContext.class);
		}
		public InputSentenceContext inputSentence(int i) {
			return getRuleContext(InputSentenceContext.class,i);
		}
		public TerminalNode TO() { return getToken(SagitariiParser.TO, 0); }
		public Output_relationContext output_relation() {
			return getRuleContext(Output_relationContext.class,0);
		}
		public TerminalNode AS() { return getToken(SagitariiParser.AS, 0); }
		public Activity_nameContext activity_name() {
			return getRuleContext(Activity_nameContext.class,0);
		}
		public List<ActivitySentenceContext> activitySentence() {
			return getRuleContexts(ActivitySentenceContext.class);
		}
		public ActivitySentenceContext activitySentence(int i) {
			return getRuleContext(ActivitySentenceContext.class,i);
		}
		public ActivitySentenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activitySentence; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterActivitySentence(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitActivitySentence(this);
		}
	}

	public final ActivitySentenceContext activitySentence() throws RecognitionException {
		ActivitySentenceContext _localctx = new ActivitySentenceContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_activitySentence);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			match(OPERATOR);
			setState(13);
			executor();
			setState(14);
			match(T__0);
			setState(15);
			inputSentence();
			setState(20);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(16);
				match(T__1);
				setState(17);
				inputSentence();
				}
				}
				setState(22);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(23);
			match(T__2);
			setState(24);
			match(TO);
			setState(25);
			output_relation();
			setState(26);
			match(AS);
			setState(27);
			activity_name();
			setState(32);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(28);
					match(T__3);
					setState(29);
					activitySentence();
					}
					} 
				}
				setState(34);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputSentenceContext extends ParserRuleContext {
		public Input_relationContext input_relation() {
			return getRuleContext(Input_relationContext.class,0);
		}
		public ActivitySentenceContext activitySentence() {
			return getRuleContext(ActivitySentenceContext.class,0);
		}
		public InputSentenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputSentence; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterInputSentence(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitInputSentence(this);
		}
	}

	public final InputSentenceContext inputSentence() throws RecognitionException {
		InputSentenceContext _localctx = new InputSentenceContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_inputSentence);
		try {
			setState(37);
			switch (_input.LA(1)) {
			case TEXT:
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				input_relation();
				}
				break;
			case OPERATOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				activitySentence();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExecutorContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(SagitariiParser.TEXT, 0); }
		public ExecutorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_executor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterExecutor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitExecutor(this);
		}
	}

	public final ExecutorContext executor() throws RecognitionException {
		ExecutorContext _localctx = new ExecutorContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_executor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			match(TEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Input_relationContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(SagitariiParser.TEXT, 0); }
		public Input_relationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterInput_relation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitInput_relation(this);
		}
	}

	public final Input_relationContext input_relation() throws RecognitionException {
		Input_relationContext _localctx = new Input_relationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_input_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(TEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Output_relationContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(SagitariiParser.TEXT, 0); }
		public Output_relationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_output_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterOutput_relation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitOutput_relation(this);
		}
	}

	public final Output_relationContext output_relation() throws RecognitionException {
		Output_relationContext _localctx = new Output_relationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_output_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(TEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Activity_nameContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(SagitariiParser.TEXT, 0); }
		public Activity_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_activity_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).enterActivity_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SagitariiListener ) ((SagitariiListener)listener).exitActivity_name(this);
		}
	}

	public final Activity_nameContext activity_name() throws RecognitionException {
		Activity_nameContext _localctx = new Activity_nameContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_activity_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(TEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\62\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\2\3\2\3\2\3\2\7\2\25\n"+
		"\2\f\2\16\2\30\13\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2!\n\2\f\2\16\2$\13"+
		"\2\3\3\3\3\5\3(\n\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\2\2\b\2\4\6\b"+
		"\n\f\2\2.\2\16\3\2\2\2\4\'\3\2\2\2\6)\3\2\2\2\b+\3\2\2\2\n-\3\2\2\2\f"+
		"/\3\2\2\2\16\17\7\7\2\2\17\20\5\6\4\2\20\21\7\3\2\2\21\26\5\4\3\2\22\23"+
		"\7\4\2\2\23\25\5\4\3\2\24\22\3\2\2\2\25\30\3\2\2\2\26\24\3\2\2\2\26\27"+
		"\3\2\2\2\27\31\3\2\2\2\30\26\3\2\2\2\31\32\7\5\2\2\32\33\7\b\2\2\33\34"+
		"\5\n\6\2\34\35\7\t\2\2\35\"\5\f\7\2\36\37\7\6\2\2\37!\5\2\2\2 \36\3\2"+
		"\2\2!$\3\2\2\2\" \3\2\2\2\"#\3\2\2\2#\3\3\2\2\2$\"\3\2\2\2%(\5\b\5\2&"+
		"(\5\2\2\2\'%\3\2\2\2\'&\3\2\2\2(\5\3\2\2\2)*\7\n\2\2*\7\3\2\2\2+,\7\n"+
		"\2\2,\t\3\2\2\2-.\7\n\2\2.\13\3\2\2\2/\60\7\n\2\2\60\r\3\2\2\2\5\26\""+
		"\'";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}