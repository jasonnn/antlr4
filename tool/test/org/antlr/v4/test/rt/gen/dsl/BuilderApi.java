package org.antlr.v4.test.rt.gen.dsl;

import com.sun.tools.corba.se.idl.Comment;
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

    SpecialInput<N> withStartRule(String name);
  }


  interface SpecialInput<N> implements Build<N> {
    Input<N> test(int i);

    SpecialInput<N> test(Out<Void> abc, Out<Void> output);

    Build<N> test(Out<Void> voidOut);

    //    SpecialInput<N> test(String s);
    SpecialInput<N> test(String s, String s1);

    SpecialInput<N> test(int index, String s, String s1);

    SpecialInput<N> test(String s, String s1, String s2);
  }

  interface Input<N> {
    Something<N> input(String input);

    Something<N> withInput(String s);

    Something<N> text(String abc);

  }

  interface Something<N> {

    SomethingElse<N> expectOutput(String s);

  }


  interface SomethingElse<N> {
    SpecialInput<N> expectError(String asdasd);

    N build();
  }


  interface ExpectedOutput<N> {
    N expectOutput(String expectedOutput);
  }

  interface ExpectedErrors<N> {
    N expectErrors(String expectedErrors);
  }

  interface SlaveGrammars<N> {
    N slaveGrammars(String... slaves);
  }

  interface Build<V> {
    V build();
  }

//    interface BuildWithParam<P, V> {
//        V build(P param);
//    }

//    interface OptionalStep<V, N extends OptionalStep<V, N>> extends Build<V> {
//    }
//
//    interface RuleIndex extends OptionalStep<LexerTestMethod, RuleIndex> {
//        Build<LexerTestMethod> ruleIndex(int index);
//    }

  interface LexerOptionals<V> extends Build<V> {
    LexerOptionals<V> ruleIndex(int index);
  }

  interface InputChoice<A, B> extends Input<A>, Mimo<B> {}

  interface Mimo<N> {
    <V> N insAndOuts(Out<?>... inputsAndOutputs);

  }

//  interface RuleIndex<N> {
//    N ruleIndex(int index);
//  }


  interface Out<V> extends ExpectedOutput<Out<V>>, ExpectedErrors<Out<V>> {}

  interface BaseLexerTestBuilder<N> extends GrammarName<Input<ExpectedOutput<ExpectedErrors<N>>>> {}

  interface BaseParseTestBuilder<V> extends GrammarName<StartRule<InputChoice<ExpectedOutput<ExpectedErrors<V>>, V>>> {}

  interface ParseTestBuilder extends BaseParseTestBuilder<Build<ParserTestMethod>> {}

  interface CompositeParseTestBuilder extends BaseParseTestBuilder<SlaveGrammars<Build<CompositeParserTestMethod>>> {}

  interface CompositeLexerTestBuilder extends BaseLexerTestBuilder<SlaveGrammars<Build<CompositeLexerTestMethod>>> {}

  interface LexerTestBuilder extends BaseLexerTestBuilder<LexerOptionals<LexerTestMethod>> {}

  interface Factory {
    LexerTestBuilder lexerTest(String name);

    CompositeLexerTestBuilder compositeLexerTest(String name);

    ParseTestBuilder parserTest(String name);

    CompositeParseTestBuilder compositeParserTest(String name);


    Out<Void> input(String input);

    Out<Void> output(String s);

  }
}
