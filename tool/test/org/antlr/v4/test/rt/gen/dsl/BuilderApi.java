package org.antlr.v4.test.rt.gen.dsl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;

/**
 * Created by jason on 4/11/15.
 */
public interface BuilderApi {
    interface Name<N> {
        N name(String name);
    }

    interface TestName<N> extends Name<N> {
        N test(String name);
    }

    interface GrammarName<N> {
        N grammarName(String name);
    }

    interface StartRule<N> {
        N startRule(String ruleName);
    }

    interface Input<N> {
        N input(String input);
    }

    interface ExpectedOutput<N> {
        N expectedOutput(String expectedOutput);
    }

    interface ExpectedErrors<N> {
        N expectedErrors(String expectedErrors);
    }

    interface SlaveGrammars<N> {
        N slaveGrammars(String... slaves);
    }

    interface Build<V> {
        V build();
    }

    interface BuildWithParam<P, V> {
        V build(P param);
    }

    interface OptionalStep<V, N extends OptionalStep<V, N>> extends Build<V> {
    }

    interface RuleIndex extends OptionalStep<LexerTestMethod, RuleIndex> {
        LexerTestMethod ruleIndex(int index);
    }


    interface BaseLexerTestBuilder<N> extends GrammarName<Input<ExpectedOutput<ExpectedErrors<N>>>> {
    }

    interface BaseParseTestBuilder<V> extends GrammarName<StartRule<Input<ExpectedOutput<ExpectedErrors<V>>>>> {
    }

    interface ParseTestBuilder extends BaseParseTestBuilder<ParserTestMethod> {
    }

    interface CompositeParseTestBuilder extends BaseParseTestBuilder<SlaveGrammars<CompositeParserTestMethod>> {
    }

    interface CompositeLexerTestBuilder extends BaseLexerTestBuilder<SlaveGrammars<CompositeLexerTestMethod>> {
    }

    interface LexerTestBuilder extends BaseLexerTestBuilder<RuleIndex> {
    }

    interface Factory {
        LexerTestBuilder lexerTest(String name);

        CompositeLexerTestBuilder compositeLexerTest(String name);

        ParseTestBuilder parserTest(String name);

        CompositeParseTestBuilder compositeParserTest(String name);

    }
}
