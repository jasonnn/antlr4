package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;

import java.util.List;

/**
 * Created by jason on 3/15/15.
 */
public interface RuleContext extends RuleNode {
	void copyFrom(RuleContext ctx);

	void enterRule(ParseTreeListener listener);

	void exitRule(ParseTreeListener listener);

	TerminalNode addChild(TerminalNode t);

	RuleContext addChild(RuleContext ruleInvocation);

	void removeLastChild();

	TerminalNode addChild(Token matchedToken);

	ErrorNode addErrorNode(Token badToken);



	@Override
	ParseTree getChild(int i);

	<T extends ParseTree> T getChild(Class<? extends T> ctxType, int i);

	TerminalNode getToken(int ttype, int i);

	List<TerminalNode> getTokens(int ttype);

	<T extends RuleContext> T getRuleContext(Class<? extends T> ctxType, int i);

	<T extends RuleContext> List<T> getRuleContexts(Class<? extends T> ctxType);

//	@Override
//	ParserRuleContext getRuleContext();

	@Override
	RuleContext getParent();

	@Override
	RuleContext getPayload();

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

	int depth();

	boolean isEmpty();

	int getRuleIndex();

	int getInvokingState();

	void setInvokingState(int invokingState);

	void setParent(RuleContext parent);

	@Override
	<T> T accept(ParseTreeVisitor<? extends T> visitor);
}
