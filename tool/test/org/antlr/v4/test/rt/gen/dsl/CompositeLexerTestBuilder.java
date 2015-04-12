package org.antlr.v4.test.rt.gen.dsl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.*;

/**
 * Created by jason on 4/11/15.
 */
public class CompositeLexerTestBuilder
        implements
        TestName<GrammarName<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>>>,
        GrammarName<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>>,
        Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>,
        ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>,
        ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>,
        SlaveGrammars<CompositeLexerTestMethod> {
    String name;
    String grammarName;
    String input;
    String expectedOutput;
    String expectedErrors;
    String[] slaves;

    @Override
    public CompositeLexerTestMethod
    slaveGrammars(String... slaves) {
        this.slaves = slaves;
        return new CompositeLexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, slaves);
    }

    @Override
    public SlaveGrammars<CompositeLexerTestMethod>
    expectedErrors(String expectedErrors) {
        this.expectedErrors = expectedErrors;
        return this;
    }

    @Override
    public ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>
    expectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
        return this;
    }

    @Override
    public ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>
    input(String input) {
        this.input = input;
        return this;
    }

    @Override
    public Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>
    grammarName(String name) {
        this.grammarName = name;
        return this;
    }

    @Override
    public GrammarName<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>>
    test(String name) {
        this.name = name;

        return this;
    }

    @Override
    public GrammarName<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<CompositeLexerTestMethod>>>>>
    name(String name) {
        this.name = name;
        return this;
    }
}
