package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.*;

/**
 * Created by jason on 4/11/15.
 */
public class BuilderImpls {

    public static class CompositeParserTestBuilderImpl
            extends
            AbstractParserTestBuilder<SlaveGrammars<CompositeParserTestMethod>>
            implements
            SlaveGrammars<CompositeParserTestMethod>,
            CompositeParseTestBuilder {

        public CompositeParserTestBuilderImpl(String name) {
            super(name);
        }

        @Override
        protected SlaveGrammars<CompositeParserTestMethod> nextStep() {
            return this;
        }


        @Override
        public CompositeParserTestMethod slaveGrammars(String... slaves) {
            return new CompositeParserTestMethod(name,
                                                 grammarName,
                                                 startRule,
                                                 input,
                                                 expectedOutput,
                                                 expectedErrors,
                                                 slaves);
        }
    }

    public static class ParserTestBuilderImpl
            extends AbstractParserTestBuilder<ParserTestMethod>
            implements ParseTestBuilder {

        public ParserTestBuilderImpl(String name) {
            super(name);
        }

        @Override
        protected ParserTestMethod nextStep() {
            return new ParserTestMethod(name, grammarName, startRule, input, expectedOutput, expectedErrors);
        }
    }


    public static abstract class AbstractParserTestBuilder<V>
            //@formatter:off
            implements
            BaseParseTestBuilder<V>,
            GrammarName<StartRule<Input<ExpectedOutput<ExpectedErrors<V>>>>>,
                        StartRule<Input<ExpectedOutput<ExpectedErrors<V>>>>,
                                  Input<ExpectedOutput<ExpectedErrors<V>>>,
                                        ExpectedOutput<ExpectedErrors<V>>,
                                                       ExpectedErrors<V> {
        //@formatter:on
        String name;
        String grammarName;
        String startRule;
        String input;
        String expectedOutput;
        String expectedErrors;

        public AbstractParserTestBuilder(String name) {
            this.name = name;
        }

        @Override
        public StartRule<Input<ExpectedOutput<ExpectedErrors<V>>>>
        grammarName(String name) {
            this.grammarName = name;
            return this;
        }

        @Override
        public Input<ExpectedOutput<ExpectedErrors<V>>>
        startRule(String ruleName) {
            this.startRule = ruleName;
            return this;
        }

        @Override
        public ExpectedOutput<ExpectedErrors<V>> input(String input) {
            this.input = input;
            return this;
        }

        @Override
        public ExpectedErrors<V> expectedOutput(String expectedOutput) {
            this.expectedOutput = expectedOutput;
            return this;
        }

        protected abstract V nextStep();

        @Override
        public V expectedErrors(String expectedErrors) {
            this.expectedErrors = expectedErrors;
            return nextStep();
        }
    }

    public static class LexerTestBuilderImpl
            extends
            AbstractLexerTestBuilder<RuleIndex>
            implements
            LexerTestBuilder,
            RuleIndex {
        int ruleIndex = -1;

        public LexerTestBuilderImpl(String name) {
            super(name);
        }

        @Override
        public LexerTestMethod ruleIndex(int index) {
            this.ruleIndex = index;
            return build();
        }

        @Override
        public LexerTestMethod build() {

            return new LexerTestMethod(name,
                                       grammarName,
                                       input,
                                       expectedOutput,
                                       expectedErrors,
                                       ruleIndex == -1 ? null : ruleIndex);
        }

        @Override
        protected RuleIndex self() {
            return this;
        }
    }


    public static class CompositeLexerTestBuilderImpl
            extends
            AbstractLexerTestBuilder<SlaveGrammars<CompositeLexerTestMethod>>
            implements
            CompositeLexerTestBuilder,
            SlaveGrammars<CompositeLexerTestMethod> {

        public CompositeLexerTestBuilderImpl(String name) {
            super(name);
        }

        @Override
        protected SlaveGrammars<CompositeLexerTestMethod> self() {
            return this;
        }


        @Override
        public CompositeLexerTestMethod slaveGrammars(String... slaves) {
            assert name != null : "name";
            assert grammarName != null : "grammar name";
            assert input != null : "input";
            assert expectedOutput != null : "expected output";
            assert slaves != null : "slaves";
            assert slaves.length != 0 : "slaves";
            return new CompositeLexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, slaves);
        }
    }

    public static abstract class AbstractLexerTestBuilder<N>
            //@formatter:off
            implements
            BaseLexerTestBuilder<N>,
            Input<ExpectedOutput<ExpectedErrors<N>>>,
                  ExpectedOutput<ExpectedErrors<N>>,
                                 ExpectedErrors<N>
            //@formatter:on
    {
        protected String name;
        protected String grammarName;
        protected String input;
        protected String expectedOutput;
        protected String expectedErrors;


        public AbstractLexerTestBuilder(String name) {
            this.name = name;
        }

        @Override
        public Input<ExpectedOutput<ExpectedErrors<N>>>
        grammarName(String name) {
            this.grammarName = name;
            return this;
        }

        @Override
        public ExpectedOutput<ExpectedErrors<N>> input(String input) {
            this.input = input;
            return this;
        }

        @Override
        public ExpectedErrors<N> expectedOutput(String expectedOutput) {
            this.expectedOutput = expectedOutput;
            return this;
        }

        @Override
        public N expectedErrors(String expectedErrors) {
            this.expectedErrors = expectedErrors;
            return self();
        }

        protected abstract N self();/*--{
        return this;
    }--*/
    }

}
