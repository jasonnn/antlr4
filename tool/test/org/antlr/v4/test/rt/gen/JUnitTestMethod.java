package org.antlr.v4.test.rt.gen;

import org.stringtemplate.v4.STGroup;

import java.io.File;
import java.lang.ref.WeakReference;

public abstract class JUnitTestMethod {

	public String name;
	public Grammar grammar;
	public String afterGrammar;
	public String input;
	public String expectedOutput;
	public String expectedErrors;
	public boolean debug = false;
	//just in case...
	private WeakReference<JUnitTestFile> declaringFile;

	protected JUnitTestMethod(String name, String grammarName, String input,
			String expectedOutput, String expectedErrors, Integer index) {
		this.name = name + (index==null ? "" : "_" + index);
		this.grammar = new Grammar(name, grammarName);
		this.input = Generator.escape(input);
		this.expectedOutput = Generator.escape(expectedOutput);
		this.expectedErrors = Generator.escape(expectedErrors);
	}

	/**
	 *
	 * @param grammarDir the dir which contains the resources needed by this test method.
	 *                   i.e.  org/antlr/v4/test/rt/gen/grammars/CompositeLexers
	 *
	 * @throws Exception
	 */
	public void loadGrammars(File grammarDir) throws Exception {

		grammar.load(grammarDir);
	}

	public
	JUnitTestFile getFile() {
		return declaringFile.get();
	}

	void setDeclaringFile(JUnitTestFile file) {
		this.declaringFile = new WeakReference<JUnitTestFile>(file);
	}

	public void generateGrammars(STGroup group) {
		grammar.generate(group);
	}

	 abstract <R,P> R accept(TestMethodVisitor<R,P> visitor,P param);

}
