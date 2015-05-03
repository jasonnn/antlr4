package org.antlr.v4.test.rt.gen;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JUnitTestFile implements JUnitTestFileBuilder{

	public static JUnitTestFile create(File grammarsDir,String suiteName){
		JUnitTestFile file = new JUnitTestFile(suiteName);
		file.grammarsDir=new File(grammarsDir,suiteName);
		return file;
	}
	File grammarsDir;

  public final List<JUnitTestMethod> unitTests = new ArrayList<JUnitTestMethod>();
	public String name;
	//used by stg
	public List<String> tests = new ArrayList<String>();
	public boolean importErrorQueue = false;
	public boolean importGrammar = false;

	public JUnitTestFile(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ParserTestMethod addParserTest(File grammarDir, String name, String grammarName, String methodName,
			String input, String expectedOutput, String expectedErrors) throws Exception {
		ParserTestMethod tm = new ParserTestMethod(name, grammarName, methodName, input, expectedOutput, expectedErrors);
		tm.loadGrammars(grammarsDir);
		return addTest(tm);
	}

	public AbstractParserTestMethod addParserTests(File grammarDir, String name, String grammarName, String methodName,
			String ... inputsAndOuputs) throws Exception {
		AbstractParserTestMethod tm = new AbstractParserTestMethod(name, grammarName, methodName);
		tm.loadGrammars(grammarsDir);
		addTest(tm);
		for (int i = 0; i < inputsAndOuputs.length; i += 2) {
			ConcreteParserTestMethod cm = new ConcreteParserTestMethod(name,
					inputsAndOuputs[i], inputsAndOuputs[i+1], null,
					1 + (i/2));
			addTest(cm);
			tm.derivedTests.add(cm);
		}
		return tm;
	}

	public AbstractParserTestMethod addParserTestsWithErrors(File grammarDir, String name, String grammarName, String methodName,
			String ... inputsOuputsAndErrors) throws Exception {
		AbstractParserTestMethod tm = new AbstractParserTestMethod(name, grammarName, methodName);
		tm.loadGrammars(grammarsDir);
		addTest(tm);
		for(int i=0; i<inputsOuputsAndErrors.length; i+=3) {
			ConcreteParserTestMethod cm = new ConcreteParserTestMethod(name,
					inputsOuputsAndErrors[i], inputsOuputsAndErrors[i+1], inputsOuputsAndErrors[i+2],
					1 + (i/3));
			tm.derivedTests.add(cm);
			addTest(cm);
		}
		return tm;
	}

	public CompositeParserTestMethod addCompositeParserTest(File grammarDir, String name, String grammarName, String methodName,
			String input, String expectedOutput, String expectedErrors, String ... slaves) throws Exception {
		CompositeParserTestMethod tm = new CompositeParserTestMethod(name, grammarName, methodName, input, expectedOutput, expectedErrors, slaves);
		tm.loadGrammars(grammarsDir);
		return addTest(tm);
	}

	public LexerTestMethod addLexerTest(File grammarDir, String name, String grammarName,
			String input, String expectedOutput, String expectedErrors) throws Exception {
		return addLexerTest(grammarDir, name, grammarName, input, expectedOutput, expectedErrors, null);
	}

	public LexerTestMethod addLexerTest(File grammarDir, String name, String grammarName,
			String input, String expectedOutput, String expectedErrors, Integer index) throws Exception {
		LexerTestMethod tm = new LexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, index);
		tm.loadGrammars(grammarsDir);
		return addTest(tm);
	}

	public
	CompositeLexerTestMethod addCompositeLexerTest(String testName,
																								 String grammarName,
																								 String input,
																								 String expectedOutput,
																								 String expectedErrors,
																								 String... slaves) throws Exception {

		return addTest(CompositeLexerTestMethod.create(this.grammarsDir,
																									 this.name,
																									 testName,
																									 grammarName,
																									 input,
																									 expectedOutput,
																									 expectedErrors,
																									 slaves));
	}

	<T extends JUnitTestMethod> T addTest(T test) throws Exception {
		//	test.loadGrammars(grammarsDir);
		test.setDeclaringFile(this);
		unitTests.add(test);
		return test;
	}


	private void generateUnitTest(STGroup stGroup, JUnitTestMethod test){
		test.generateGrammars(stGroup);
		String name = test.getClass().getSimpleName();
		ST template = stGroup.getInstanceOf(name);
		template.add("test", test);
		tests.add(template.render());
	}

	public JUnitTestFile build(STGroup group){
		for(JUnitTestMethod tm : unitTests) {
			generateUnitTest(group,tm);
		}
		return this;
	}

	@Override
	public JUnitTestFileBuilder importErrorQueue(boolean bool) {
		importErrorQueue=bool;
		return this;
	}

	@Override
	public JUnitTestFileBuilder importGrammar(boolean bool) {
		importGrammar=bool;
		return this;
	}

	public <R,P> void visitTests(TestMethodVisitor<R,P> visitor){
		visitTests(visitor, null);
	}

	public
	<R, P> void visitTests(TestMethodVisitor<R, P> visitor, P param) {
		for (JUnitTestMethod unitTest : unitTests) {
			unitTest.accept(visitor, param);
		}
	}

}
