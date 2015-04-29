package org.antlr.v4.test.rt.gen.dsl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;

/**
 * Created by jason on 4/11/15.
 */
public
interface BuilderApi {
  interface Name<N> {
    N name(String testName);
  }

  interface TestName<N> extends Name<N> {
    N test(String testName);
  }

  interface GrammarName<N> {
    N grammarName(String grammarName);
  }

  interface StartRule<N> {
    N startRule(String ruleName);

    N withStartRule(String name);
  }


  interface Test<N> {
    N test(String s, String s1);

    N test(int index, String s, String s1);

    N test(String s, String s1, String s2);
  }

  interface TestBuildable<N> extends Build<N>, Test<TestBuildable<N>> {}


  interface Input<N> {
    N input(String input);

    N withInput(String s);

    N text(String abc);

  }

  interface ExpectedOutput<N> {
    N expectOutput(String expectedOutput);
  }

  interface ExpectedErrors<N> {
    N expectErrors(String expectedErrors);
  }

  interface ExpectedErrorsBuildable<N> extends Build<N>, ExpectedErrors<Build<N>> {}

  interface SlaveGrammars<N> {
    N slaveGrammars(String... slaves);
  }

  interface Build<V> {
    V build();
  }

  interface LexerTestBuilder extends GrammarName<Input<ExpectedOutput<ExpectedErrorsBuildable<LexerTestMethod>>>> {}

  interface CompositeLexerTestBuilder
      extends GrammarName<SlaveGrammars<Input<ExpectedOutput<ExpectedErrorsBuildable<CompositeLexerTestMethod>>>>> {}

  interface ParserTestBuilder extends GrammarName<StartRule<TestBuildable<ParserTestMethod>>> {}

  interface CompositeParserTestBuilder2
      extends GrammarName<SlaveGrammars<StartRule<TestBuildable<CompositeParserTestMethod>>>> {}

  interface CompositeParserTestBuilder
      extends GrammarName<SlaveGrammars<StartRule<Input<ExpectedOutput<ExpectedErrorsBuildable<CompositeParserTestMethod>>>>>> {}

  interface Factory {

    LexerTestBuilder lexerTest(String name);

    //GrammarName<SlaveGrammars<Input<ExpectedOutput<ExpectedErrorsBuildable<CompositeLexerTestMethod>>>>>//
    CompositeLexerTestBuilder compositeLexerTest(String name);

    // GrammarName<StartRule<TestBuildable<ParserTestMethod>>> //
    ParserTestBuilder parserTest(String name);

    // GrammarName<SlaveGrammars<StartRule<TestBuildable<CompositeParserTestMethod>>>> //
    CompositeParserTestBuilder2 compositeParserTest2(String s);

    // GrammarName<SlaveGrammars<StartRule<Input<ExpectedOutput<ExpectedErrorsBuildable<CompositeParserTestMethod>>>>>> //
    CompositeParserTestBuilder compositeParserTest(String name);


  }
}
