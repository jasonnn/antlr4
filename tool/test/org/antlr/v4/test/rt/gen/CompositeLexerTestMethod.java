package org.antlr.v4.test.rt.gen;

import org.stringtemplate.v4.STGroup;

import java.io.File;

public class CompositeLexerTestMethod extends LexerTestMethod {

	public static CompositeLexerTestMethod create(File grammarDir, String testFileName,
												  String name, String grammarName,
												  String input, String expectedOutput,
												  String expectedErrors, String... slaves) throws Exception {

		CompositeLexerTestMethod cltm = new CompositeLexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, slaves);
		cltm.loadGrammars(grammarDir);
		return cltm;
	}

	public Grammar[] slaveGrammars;

	public CompositeLexerTestMethod(String name, String grammarName,
			String input, String expectedOutput,
			String expectedErrors, String ... slaves) {
		super(name, grammarName, input, expectedOutput, expectedErrors, null);
		this.slaveGrammars = new Grammar[slaves.length];
		for(int i=0;i<slaves.length;i++)
			this.slaveGrammars[i] = new Grammar(name + "_" + slaves[i], slaves[i]);

	}

	@Override
	public void loadGrammars(File grammarDir) throws Exception {
		for(Grammar slave : slaveGrammars)
			slave.load(grammarDir);
		super.loadGrammars(grammarDir);
	}

	@Override
	public void generateGrammars(STGroup group) {
		for(Grammar slave : slaveGrammars)
			slave.generate(group);
		super.generateGrammars(group);
	}

	@Override
	public
	<R, P> R accept(TestMethodVisitor<R, P> visitor, P param) {
		return visitor.visitCompositeLexerTest(this,param);
	}
}
