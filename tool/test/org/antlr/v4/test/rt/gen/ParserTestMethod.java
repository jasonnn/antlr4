package org.antlr.v4.test.rt.gen;

public class ParserTestMethod extends JUnitTestMethod {

	public String startRule;

	public ParserTestMethod(String name, String grammarName, String startRule,
			String input, String expectedOutput, String expectedErrors) {
		super(name, grammarName, input, expectedOutput, expectedErrors, null);
		this.startRule = startRule;
	}


	@Override
	<R, P> R accept(TestMethodVisitor<R, P> visitor, P param) {
		return visitor.visitParserTest(this,param);
	}
}
