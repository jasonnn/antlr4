package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * Created by jason on 3/15/15.
 */
public interface ParserRuleContext extends RuleContext {
	void copyFrom(ParserRuleContext ctx);

	void enterRule(ParseTreeListener listener);

	void exitRule(ParseTreeListener listener);

	TerminalNode addChild(TerminalNode t);

	RuleContext addChild(RuleContext ruleInvocation);

	void removeLastChild();

	TerminalNode addChild(Token matchedToken);

	ErrorNode addErrorNode(Token badToken);

	@Override
	/** Override to make type more specific */
	ParserRuleContext getParent();

	@Override
	ParseTree getChild(int i);

	<T extends ParseTree> T getChild(Class<? extends T> ctxType, int i);

	TerminalNode getToken(int ttype, int i);

	List<TerminalNode> getTokens(int ttype);

	<T extends ParserRuleContext> T getRuleContext(Class<? extends T> ctxType, int i);

	<T extends ParserRuleContext> List<T> getRuleContexts(Class<? extends T> ctxType);

	@Override
	int getChildCount();

	@Override
	Interval getSourceInterval();

	Token getStart();

	Token getStop();

	RecognitionException getException();

	void setException(RecognitionException exception);

	void setStart(Token start);

	void setStop(Token stop);

	List<ParseTree> getChildren();

	void setChildren(List<ParseTree> children);
}
