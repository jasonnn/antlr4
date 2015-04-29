package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.*;

/**
 * Created by jason on 4/11/15.
 */
public
class BuilderImpls2 {

  private String name;
  private String grammarName;
  private String startRule;
  private String input;
  private String expectedOutput;
  private String expectedErrors;
  private String[] slaveGrammars;
  private int ruleIndex = -1;

//    public static TestName<LexerTestBuilder> lexerTestBuilder3() {
//        BuilderImpls2 builderImpls2 = new BuilderImpls2();
//        return builderImpls2.testName(builderImpls2.lexerTestBuilder2());
//    }

//    public LexerTestBuilder lexerTestBuilder2() {
//        return new LexerTestBuilder() {
//            @Override
//            public Input<ExpectedOutput<ExpectedErrors<Build<RuleIndex2>>>> grammarName(String name) {
//                grammarName = name;
//                return input(expectOutput(expectErrors(ruleIndex())));
//            }
//        };
//    }


  public
  TestName<GrammarName<StartRule<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<Build<CompositeParserTestMethod>>>>>>>> compositeParseTestBuilder() {
    return testName(grammarName(startRule(input(expectedOutput(expectedErrors(slaveGrammars(compositeParserTestMethod())))))));
  }

  public
  TestName<GrammarName<StartRule<Input<ExpectedOutput<ExpectedErrors<Build<ParserTestMethod>>>>>>> parserTestBuilder() {
    return testName(grammarName(startRule(input(expectedOutput(expectedErrors(parserTestMethod()))))));
  }

  public
  TestName<GrammarName<Input<ExpectedOutput<ExpectedErrors<LexerOptionals<LexerTestMethod>>>>>>

  lexerTestBuilder() {
    return testName(grammarName(input(expectedOutput(expectedErrors(lexerOptionals(lexerTestMethod()))))));
  }

  public
  TestName<GrammarName<Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<Build<CompositeLexerTestMethod>>>>>>> compositeLexerTestBuilder() {
    return testName(grammarName(input(expectedOutput(expectedErrors(slaveGrammars(compositeLexerTestMethod()))))));
  }


  Build<ParserTestMethod> parserTestMethod() {
    return new Build<ParserTestMethod>() {
      @Override
      public
      ParserTestMethod build() {
        return new ParserTestMethod(name, grammarName, startRule, input, expectedOutput, expectedErrors);
      }
    };
  }

  Build<CompositeParserTestMethod> compositeParserTestMethod() {
    return new Build<CompositeParserTestMethod>() {
      @Override
      public
      CompositeParserTestMethod build() {
        return new CompositeParserTestMethod(name,
                                             grammarName,
                                             startRule,
                                             input,
                                             expectedOutput,
                                             expectedErrors,
                                             slaveGrammars);
      }
    };
  }

  Build<CompositeLexerTestMethod> compositeLexerTestMethod() {
    return new Build<CompositeLexerTestMethod>() {
      @Override
      public
      CompositeLexerTestMethod build() {
        return new CompositeLexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, slaveGrammars);
      }
    };
  }

  Build<LexerTestMethod> lexerTestMethod() {
    return new Build<LexerTestMethod>() {
      @Override
      public
      LexerTestMethod build() {
        return new LexerTestMethod(name,
                                   grammarName,
                                   input,
                                   expectedOutput,
                                   expectedErrors,
                                   ruleIndex == -1 ? null : ruleIndex);
      }
    };
  }

  <B> LexerOptionals<B> lexerOptionals(final Build<B> build) {
    return new LexerOptionals<B>() {
      @Override
      public
      LexerOptionals<B> ruleIndex(int index) {
        BuilderImpls2.this.ruleIndex = index; return this;
      }

      @Override
      public
      B build() {
        return build.build();
      }
    };
  }

//    <B> RuleIndex<B> ruleIndex(final B next) {
//        return new RuleIndex<B>() {
//            @Override
//            public B ruleIndex(int index) {
//                BuilderImpls2.this.ruleIndex = index;
//
//                return next;
//            }
//        };
//
//    }

  <B> TestName<B> testName(final B next) {
    return new TestName<B>() {
      @Override
      public
      B test(String name) {
        return name(name);
      }

      @Override
      public
      B name(String name) {
        BuilderImpls2.this.name = name; return next;
      }
    };
  }

  <B> GrammarName<B> grammarName(final B next) {
    return new GrammarName<B>() {
      @Override
      public
      B grammarName(String name) {
        BuilderImpls2.this.grammarName = name; return next;
      }
    };
  }

  <B> StartRule<B> startRule(final B next) {
    return new StartRule<B>() {
      @Override
      public
      B startRule(String ruleName) {
        BuilderImpls2.this.startRule = ruleName; return next;
      }
    };
  }

  <B> Input<B> input(final B next) {
    return new Input<B>() {
      @Override
      public
      B input(String input) {
        BuilderImpls2.this.input = input; return next;
      }
    };
  }

  <B> ExpectedOutput<B> expectedOutput(final B next) {
    return new ExpectedOutput<B>() {
      @Override
      public
      B expectOutput(String input) {
        BuilderImpls2.this.expectedOutput = input; return next;
      }
    };
  }

  <B> ExpectedErrors<B> expectedErrors(final B next) {
    return new ExpectedErrors<B>() {
      @Override
      public
      B expectErrors(String expectedErrors) {
        BuilderImpls2.this.expectedErrors = expectedErrors; return next;
      }
    };
  }

  <B> SlaveGrammars<B> slaveGrammars(final B next) {
    return new SlaveGrammars<B>() {
      @Override
      public
      B slaveGrammars(String... slaveGrammars) {
        BuilderImpls2.this.slaveGrammars = slaveGrammars; return next;
      }
    };
  }

}
