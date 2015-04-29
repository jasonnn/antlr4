package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;

/**
 * Created by jason on 4/13/15.
 */
public class BuildFactory {
    public static final BuildFactory INSTANCE = new BuildFactory();

    public LexerTestMethod lexerTestMethod(String name,
                                           String grammarName,
                                           String input,
                                           String expectedOutput,
                                           String expectedErrors,
                                           Integer ruleIndex) {
        return new LexerTestMethod(name,
                                   grammarName,
                                   input,
                                   expectedOutput,
                                   expectedErrors,
                                   ruleIndex);
    }

    public CompositeLexerTestMethod compositeLexerTestMethod(String name,
                                                             String grammarName,
                                                             String input,
                                                             String expectedOutput,
                                                             String expectedErrors,
                                                             String[] slaveGrammars) {
        return new CompositeLexerTestMethod(name,
                                            grammarName,
                                            input,
                                            expectedOutput,
                                            expectedErrors,
                                            slaveGrammars);
    }

    public ParserTestMethod parserTestMethod(String name,
                                             String grammarName,
                                             String startRule,
                                             String input,
                                             String expectedOutput,
                                             String expectedErrors) {
        return new ParserTestMethod(name, grammarName, startRule, input, expectedOutput, expectedErrors);

    }

    public CompositeParserTestMethod compositeParserTestMethod(String name,
                                                               String grammarName,
                                                               String startRule,
                                                               String input,
                                                               String expectedOutput,
                                                               String expectedErrors,
                                                               String[] slaveGrammars) {
        return new CompositeParserTestMethod(name,
                                             grammarName,
                                             startRule,
                                             input,
                                             expectedOutput,
                                             expectedErrors,
                                             slaveGrammars);
    }
}
