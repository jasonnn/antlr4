package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * Created by jason on 3/15/15.
 */
public interface RuleContext extends RuleNode {
	int depth();

	boolean isEmpty();

	@Override
	Interval getSourceInterval();

	@Override
	RuleContext getRuleContext();

	@Override
	RuleContext getParent();

	@Override
	RuleContext getPayload();

	@Override
	String getText();

	int getRuleIndex();

	@Override
	ParseTree getChild(int i);

	@Override
	int getChildCount();

	@Override
	<T> T accept(ParseTreeVisitor<? extends T> visitor);

	int getInvokingState();

	void setInvokingState(int invokingState);

	void setParent(RuleContext parent);
}
