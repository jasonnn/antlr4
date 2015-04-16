package org.antlr.v4.test.rt.gen;

public class ParserTestMethod extends JUnitTestMethod {

	public String startRule;

	public ParserTestMethod(String name, String grammarName, String startRule,
			String input, String expectedOutput, String expectedErrors) {
		super(name, grammarName, input, expectedOutput, expectedErrors, null);
		this.startRule = startRule;
	}

	@Override
	public void accept(TestMethodVisitor visitor) {
		visitor.visitParserTest(this);
	}
}
