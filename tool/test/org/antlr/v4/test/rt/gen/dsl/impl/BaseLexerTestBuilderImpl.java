package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.dsl.BuilderApi.BaseLexerTestBuilder;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.ExpectedErrors;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.ExpectedOutput;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.Input;

/**
 * Created by jason on 4/11/15.
 */
public abstract class BaseLexerTestBuilderImpl<N>
        implements
        BaseLexerTestBuilder<N>,
        Input<ExpectedOutput<ExpectedErrors<N>>>,
        ExpectedOutput<ExpectedErrors<N>>,
        ExpectedErrors<N> {
    @Override
    public Input<ExpectedOutput<ExpectedErrors<N>>> grammarName(String name) {
        return this;
    }

    @Override
    public ExpectedOutput<ExpectedErrors<N>> input(String input) {
        return this;
    }

    @Override
    public ExpectedErrors<N> expectedOutput(String expectedOutput) {
        return this;
    }

    @Override
    public N expectedErrors(String expectedErrors) {
        return nextStep();
    }

    protected abstract N nextStep();
}
