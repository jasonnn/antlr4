/*
 * [The "BSD license"]
 *  Copyright (c) 2012 Terence Parr
 *  Copyright (c) 2012 Sam Harwell
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.antlr.v4.runtime;

import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** A rule invocation record for parsing.
 *
 *  Contains all of the information about the current rule not stored in the
 *  RuleContext. It handles parse tree children list, Any ATN state
 *  tracing, and the default values available for rule invocations:
 *  start, stop, rule index, current alt number.
 *
 *  Subclasses made for each rule and grammar track the parameters,
 *  return values, locals, and labels specific to that rule. These
 *  are the objects that are returned from rules.
 *
 *  Note text is not an actual field of a rule return value; it is computed
 *  from start and stop using the input stream's toString() method.  I
 *  could add a ctor to this so that we can pass in and store the input
 *  stream, but I'm not sure we want to do that.  It would seem to be undefined
 *  to get the .text property anyway if the rule matches tokens from multiple
 *  input streams.
 *
 *  I do not use getters for fields of objects that are used simply to
 *  group values such as this aggregate.  The getters/setters are there to
 *  satisfy the superclass interface.
 */
public class ParserRuleContextImpl extends AbstractRuleContext implements RuleContext {
	private List<ParseTree> children;

	//	public List<Integer> states;

	private Token start;
	private Token stop;

	private RecognitionException exception;

	public ParserRuleContextImpl() { }

	/** COPY a ctx (I'm deliberately not using copy constructor) to avoid
	 *  confusion with creating node with parent. Does not copy children.
	 */
	@Override
	public void copyFrom(RuleContext ctx) {
		this.setParent(ctx.getParent());
		this.setInvokingState(ctx.getInvokingState());

		this.start = ctx.getStart();
		this.stop = ctx.getStop();
	}

	public ParserRuleContextImpl(RuleContext parent, int invokingStateNumber) {
		super(parent, invokingStateNumber);
	}

	// Double dispatch methods for listeners

	@Override
	public void enterRule(ParseTreeListener listener) { }
	@Override
	public void exitRule(ParseTreeListener listener) { }

	/** Does not set parent link; other add methods do that */
	@Override
	public TerminalNode addChild(TerminalNode t) {
		if ( children==null ) children = new ArrayList<ParseTree>();
		children.add(t);
		return t;
	}

	@Override
	public RuleContext addChild(RuleContext ruleInvocation) {
		if ( children==null ) children = new ArrayList<ParseTree>();
		children.add(ruleInvocation);
		return ruleInvocation;
	}

	/** Used by enterOuterAlt to toss out a RuleContext previously added as
	 *  we entered a rule. If we have # label, we will need to remove
	 *  generic ruleContext object.
 	 */
	@Override
	public void removeLastChild() {
		if ( children!=null ) {
			children.remove(children.size()-1);
		}
	}

//	public void trace(int s) {
//		if ( states==null ) states = new ArrayList<Integer>();
//		states.add(s);
//	}

	@Override
	public TerminalNode addChild(Token matchedToken) {
		TerminalNodeImpl t = new TerminalNodeImpl(matchedToken);
		addChild(t);
		t.parent = this;
		return t;
	}

	@Override
	public ErrorNode addErrorNode(Token badToken) {
		ErrorNodeImpl t = new ErrorNodeImpl(badToken);
		addChild(t);
		t.parent = this;
		return t;
	}

	@Override
	/** Override to make type more specific */
	public ParserRuleContextImpl getParent() {
		return (ParserRuleContextImpl)super.getParent();
	}

	@Override
	public RuleContext getPayload() {
		return this;
	}

	@Override
	public ParseTree getChild(int i) {
		return children!=null && i>=0 && i<children.size() ? children.get(i) : null;
	}

	@Override
	public <T extends ParseTree> T getChild(Class<? extends T> ctxType, int i) {
		if ( children==null || i < 0 || i >= children.size() ) {
			return null;
		}

		int j = -1; // what element have we found with ctxType?
		for (ParseTree o : children) {
			if ( ctxType.isInstance(o) ) {
				j++;
				if ( j == i ) {
					return ctxType.cast(o);
				}
			}
		}
		return null;
	}

	@Override
	public TerminalNode getToken(int ttype, int i) {
		if ( children==null || i < 0 || i >= children.size() ) {
			return null;
		}

		int j = -1; // what token with ttype have we found?
		for (ParseTree o : children) {
			if ( o instanceof TerminalNode ) {
				TerminalNode tnode = (TerminalNode)o;
				Token symbol = tnode.getSymbol();
				if ( symbol.getType()==ttype ) {
					j++;
					if ( j == i ) {
						return tnode;
					}
				}
			}
		}

		return null;
	}

	@Override
	public List<TerminalNode> getTokens(int ttype) {
		if ( children==null ) {
			return Collections.emptyList();
		}

		List<TerminalNode> tokens = null;
		for (ParseTree o : children) {
			if ( o instanceof TerminalNode ) {
				TerminalNode tnode = (TerminalNode)o;
				Token symbol = tnode.getSymbol();
				if ( symbol.getType()==ttype ) {
					if ( tokens==null ) {
						tokens = new ArrayList<TerminalNode>();
					}
					tokens.add(tnode);
				}
			}
		}

		if ( tokens==null ) {
			return Collections.emptyList();
		}

		return tokens;
	}

	@Override
	public <T extends RuleContext> T getRuleContext(Class<? extends T> ctxType, int i) {
		return getChild(ctxType, i);
	}

	@Override
	public <T extends RuleContext> List<T> getRuleContexts(Class<? extends T> ctxType) {
		if ( children==null ) {
			return Collections.emptyList();
		}

		List<T> contexts = null;
		for (ParseTree o : children) {
			if ( ctxType.isInstance(o) ) {
				if ( contexts==null ) {
					contexts = new ArrayList<T>();
				}

				contexts.add(ctxType.cast(o));
			}
		}

		if ( contexts==null ) {
			return Collections.emptyList();
		}

		return contexts;
	}

	@Override
	public int getChildCount() { return children!=null ? children.size() : 0; }

	@Override
	public Interval getSourceInterval() {
		if ( start==null || stop==null ) return Interval.INVALID;
		return Interval.of(start.getTokenIndex(), stop.getTokenIndex());
	}

	/** For debugging/tracing purposes, we want to track all of the nodes in
	 *  the ATN traversed by the parser for a particular rule.
	 *  This list indicates the sequence of ATN nodes used to match
	 *  the elements of the children list. This list does not include
	 *  ATN nodes and other rules used to match rule invocations. It
	 *  traces the rule invocation node itself but nothing inside that
	 *  other rule's ATN submachine.
	 *
	 *  There is NOT a one-to-one correspondence between the children and
	 *  states list. There are typically many nodes in the ATN traversed
	 *  for each element in the children list. For example, for a rule
	 *  invocation there is the invoking state and the following state.
	 *
	 *  The parser setState() method updates field s and adds it to this list
	 *  if we are debugging/tracing.
     *
     *  This does not trace states visited during prediction.
	 */ /**
	 * Get the initial token in this context.
	 * Note that the range from start to stop is inclusive, so for rules that do not consume anything
	 * (for example, zero length or error productions) this token may exceed stop.
	 */
	@Override
	public Token getStart() { return start; }
	/**
	 * Get the final token in this context.
	 * Note that the range from start to stop is inclusive, so for rules that do not consume anything
	 * (for example, zero length or error productions) this token may precede start.
	 */
	@Override
	public Token getStop() { return stop; }

    /** Used for rule context info debugging during parse-time, not so much for ATN debugging */
    public String toInfoString(IParser recognizer) {
        List<String> rules = recognizer.getRuleInvocationStack(this);
        Collections.reverse(rules);
        return "ParserRuleContext"+rules+"{" +
                "start=" + start +
                ", stop=" + stop +
                '}';
    }

	/**
	 * The exception that forced this rule to return. If the rule successfully
	 * completed, this is {@code null}.
	 */
	@Override
	public RecognitionException getException() {
		return exception;
	}

	@Override
	public void setException(RecognitionException exception) {
		this.exception = exception;
	}

	@Override
	public void setStart(Token start) {
		this.start = start;
	}

	@Override
	public void setStop(Token stop) {
		this.stop = stop;
	}

	/** If we are debugging or building a parse tree for a visitor,
	 *  we need to track all of the tokens and rule invocations associated
	 *  with this rule's context. This is empty for parsing w/o tree constr.
	 *  operation because we don't the need to track the details about
	 *  how we parse this rule.
	 */
	@Override
	public List<ParseTree> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<ParseTree> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return toString((List<String>)null, null);
	}

	public final String toString(IRecognizer<?,?> recog) {
		return toString(recog, ParserRuleContextImpl.EMPTY);
	}

	public final String toString(List<String> ruleNames) {
		return toString(ruleNames, null);
	}

	// recog null unless ParserRuleContext, in which case we use subclass toString(...)
	public String toString(IRecognizer<?,?> recog, RuleContext stop) {
		String[] ruleNames = recog != null ? recog.getRuleNames() : null;
		List<String> ruleNamesList = ruleNames != null ? Arrays.asList(ruleNames) : null;
		return toString(ruleNamesList, stop);
	}

	public String toString(List<String> ruleNames, RuleContext stop) {
		StringBuilder buf = new StringBuilder();
		RuleContext p = this;
		buf.append("[");
		while (p != null && p != stop) {
			if (ruleNames == null) {
				if (!p.isEmpty()) {
					buf.append(p.getInvokingState());
				}
			}
			else {
				int ruleIndex = p.getRuleIndex();
				String ruleName = ruleIndex >= 0 && ruleIndex < ruleNames.size() ? ruleNames.get(ruleIndex) : Integer.toString(ruleIndex);
				buf.append(ruleName);
			}

			if (p.getParent() != null && (ruleNames != null || !p.getParent().isEmpty())) {
				buf.append(" ");
			}

			p = p.getParent();
		}

		buf.append("]");
		return buf.toString();
	}

	public int depth() {
		int n = 0;
		RuleContext p = this;
		while ( p!=null ) {
			p = p.getParent();
			n++;
		}
		return n;
	}
}
