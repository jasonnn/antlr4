package org.antlr.v4.test.rt.gen;

import java.util.ArrayList;
import java.util.List;

public class AbstractParserTestMethod extends JUnitTestMethod {

	public String startRule;
	public final List<ConcreteParserTestMethod> derivedTests = new ArrayList<ConcreteParserTestMethod>();

	public AbstractParserTestMethod(String name, String grammarName, String startRule) {
		super(name, grammarName, null, null, null, null);
		this.startRule = startRule;
	}


	@Override
	<R, P> R accept(TestMethodVisitor<R,P> visitor, P param) {
		return visitor.visitAbstractParserTest(this,param);
	}
}
