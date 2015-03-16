package org.antlr.v4.runtime;

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.misc.IntervalSet;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.util.List;

/**
 * Created by jason on 3/16/15.
 */
public interface IParser extends IRecognizer<Token, ParserATNSimulator> {
    void notifyErrorListeners(Token offendingToken, String message, RecognitionException e);
    void notifyErrorListeners(String message);

    TokenStream getInputStream();

    Token consume();

    boolean isExpectedToken(int la);

    IntervalSet getExpectedTokens();

    RuleContext getContext();

    Token getCurrentToken();

    int getPrecedence();

    List<String> getRuleInvocationStack();

    ATN getATNWithBypassAlts();

    int getRuleIndex(String tag);

    List<String> getRuleInvocationStack(RuleContext ruleContext);

    void removeParseListeners();

    void setBuildParseTree(boolean buildParseTrees);

    void addParseListener(ParseTreeListener listener);

    void setErrorHandler(ANTLRErrorStrategy errorStrategy);
}
