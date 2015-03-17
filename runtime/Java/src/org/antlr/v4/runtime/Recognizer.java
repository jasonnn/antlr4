package org.antlr.v4.runtime;

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNSimulator;
import org.antlr.v4.runtime.atn.ParseInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 3/16/15.
 */
public interface Recognizer<SYMBOL,ATNInterpreter extends ATNSimulator> {
    /** Used to print out token names like ID during debugging and
     *  error reporting.  The generated parsers implement a method
     *  that overrides this to point to their String[] tokenNames.
     *
     * @deprecated Use {@link #getVocabulary()} instead.
     */
    @Deprecated
    String[] getTokenNames();

    String[] getRuleNames();

    @SuppressWarnings("deprecation")
    Vocabulary getVocabulary();

    Map<String, Integer> getTokenTypeMap();

    Map<String, Integer> getRuleIndexMap();

    int getTokenType(String tokenName);

    String getSerializedATN();

    /** For debugging and other purposes, might want the grammar name.
     *  Have ANTLR generate an implementation for this method.
     */
    String getGrammarFileName();

    /**
     * Get the {@link ATN} used by the recognizer for prediction.
     *
     * @return The {@link ATN} used by the recognizer for prediction.
     */
    ATN getATN();

    ATNInterpreter getInterpreter();

    ParseInfo getParseInfo();

    void setInterpreter(ATNInterpreter interpreter);

    String getErrorHeader(RecognitionException e);

    @Deprecated
    String getTokenErrorDisplay(Token t);

    void addErrorListener(ANTLRErrorListener listener);

    void removeErrorListener(ANTLRErrorListener listener);

    void removeErrorListeners();

    List<? extends ANTLRErrorListener> getErrorListeners();

    ANTLRErrorListener getErrorListenerDispatch();

    // subclass needs to override these if there are sempreds or actions
    // that the ATN interp needs to execute
    boolean sempred(RuleContext _localctx, int ruleIndex, int actionIndex);

    boolean precpred(RuleContext localctx, int precedence);

    void action(RuleContext _localctx, int ruleIndex, int actionIndex);

    int getState();

    void setState(int atnState);

    IntStream getInputStream();

    void setInputStream(IntStream input);

    TokenFactory<?> getTokenFactory();

    void setTokenFactory(TokenFactory<?> input);
}
