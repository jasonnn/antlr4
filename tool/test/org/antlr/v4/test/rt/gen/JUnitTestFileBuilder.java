package org.antlr.v4.test.rt.gen;

import org.stringtemplate.v4.STGroup;

import java.io.File;

/**
 * Created by jason on 4/15/15.
 */
public interface JUnitTestFileBuilder {
    ParserTestMethod addParserTest(File grammarDir, String name, String grammarName, String methodName,
                                   String input, String expectedOutput, String expectedErrors) throws Exception;

    AbstractParserTestMethod addParserTests(File grammarDir, String name, String grammarName, String methodName,
                                            String ... inputsAndOuputs) throws Exception;

    AbstractParserTestMethod addParserTestsWithErrors(File grammarDir, String name, String grammarName, String methodName,
                                                      String ... inputsOuputsAndErrors) throws Exception;

    CompositeParserTestMethod addCompositeParserTest(File grammarDir, String name, String grammarName, String methodName,
                                                     String input, String expectedOutput, String expectedErrors, String ... slaves) throws Exception;

    LexerTestMethod addLexerTest(File grammarDir, String name, String grammarName,
                                 String input, String expectedOutput, String expectedErrors) throws Exception;

    LexerTestMethod addLexerTest(File grammarDir, String name, String grammarName,
                                 String input, String expectedOutput, String expectedErrors, Integer index) throws Exception;

    CompositeLexerTestMethod addCompositeLexerTest(String testName, String grammarName,
                                                   String input, String expectedOutput, String expectedErrors, String... slaves) throws Exception;

    JUnitTestFile build(STGroup group);

    JUnitTestFileBuilder importErrorQueue(boolean bool);
    JUnitTestFileBuilder importGrammar(boolean bool);
}
