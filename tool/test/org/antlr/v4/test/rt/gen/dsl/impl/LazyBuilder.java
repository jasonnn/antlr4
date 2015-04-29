package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi.*;

/**
 * Created by jason on 4/12/15.
 */
public class LazyBuilder
        implements
        TestName,
        GrammarName,
        StartRule,
        Input,
        ExpectedOutput,
        ExpectedErrors,
        SlaveGrammars,
        Build,
//        RuleIndex,
        LexerOptionals,
        Mimo,
        InputChoice{

    String name;
    String grammarName;
    String startRule;
    String input;
    String expectedOutput;
    String expectedErrors;
    String[] slaveGrammars;
    int ruleIndex = -1;
    BUILD_A whatDoesItBuild;
    BuildFactory build = BuildFactory.INSTANCE;

    @SuppressWarnings("unchecked")
    public LexerTestBuilder asLexerTestBuilder() {
        whatDoesItBuild = BUILD_A.LEXER;
        return new LexerTestBuilder() {
            @Override
            public Input<ExpectedOutput<ExpectedErrors<LexerOptionals<LexerTestMethod>>>>
            grammarName(String name) {
                return LazyBuilder.this.grammarName(name);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public CompositeLexerTestBuilder asCompositeLexerTestBuilder() {
        whatDoesItBuild = BUILD_A.COMPOSITE_LEXER;
        return new CompositeLexerTestBuilder() {
            @Override
            public Input<ExpectedOutput<ExpectedErrors<SlaveGrammars<Build<CompositeLexerTestMethod>>>>>
            grammarName(String name) {
                return LazyBuilder.this.grammarName(name);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public ParseTestBuilder asParseTestBuilder() {
        whatDoesItBuild = BUILD_A.PARSER;
      return new ParseTestBuilder() {
        @Override
        public
        StartRule<InputChoice<ExpectedOutput<ExpectedErrors<Build<ParserTestMethod>>>,
                              Build<ParserTestMethod>>>
        grammarName(String grammarName) {
          return LazyBuilder.this.grammarName(name);
        }
      };

    }

    @SuppressWarnings("unchecked")
    public CompositeParseTestBuilder asCompositeParseTestBuilder() {
        whatDoesItBuild = BUILD_A.COMPOSITE_PARSER;
      return new CompositeParseTestBuilder() {
        @Override
        public
        StartRule<InputChoice<ExpectedOutput<ExpectedErrors<SlaveGrammars<Build<CompositeParserTestMethod>>>>,
            SlaveGrammars<Build<CompositeParserTestMethod>>>>
        grammarName(String grammarName) {
          return LazyBuilder.this.grammarName(grammarName);
        }
      };

    }

  @Override
  public
  Object insAndOuts(Out... inputsAndOutputs) {
    return null;
  }


  enum BUILD_A {
        LEXER,
        COMPOSITE_LEXER,
        PARSER,
        COMPOSITE_PARSER
    }

    LazyBuilder(BuildFactory build) {
        this.build = build;
    }

    LazyBuilder() {
    }

    LazyBuilder(BUILD_A whatDoesItBuild) {
        this.whatDoesItBuild = whatDoesItBuild;
    }

    @Override
    public LazyBuilder test(String name) {
        this.name = name;
        return this;
    }

    @Override
    public LazyBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public LazyBuilder grammarName(String name) {
        this.grammarName = name;
        return this;
    }

    @Override
    public LazyBuilder startRule(String ruleName) {
        this.startRule = ruleName;
        return this;
    }

    @Override
    public LazyBuilder input(String input) {
        this.input = input;
        return this;
    }


    @Override
    public LazyBuilder expectErrors(String expectedErrors) {
        this.expectedErrors = expectedErrors;
        return this;
    }

    @Override
    public LazyBuilder expectOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
        return this;
    }

    @Override
    public LazyBuilder slaveGrammars(String... slaves) {
        this.slaveGrammars = slaves;
        return this;
    }

    @Override
    public LazyBuilder ruleIndex(int index) {
        this.ruleIndex = index;
        return this;
    }

    @Override
    public Object build() {
        switch (whatDoesItBuild) {

            case LEXER:
                return build.lexerTestMethod(name,
                                             grammarName,
                                             input,
                                             expectedOutput,
                                             expectedErrors,
                                             ruleIndex == -1 ? null : ruleIndex);

            case COMPOSITE_LEXER:
                return build.compositeLexerTestMethod(name,
                                                      grammarName,
                                                      input,
                                                      expectedOutput,
                                                      expectedErrors,
                                                      slaveGrammars);

            case PARSER:
                return build.parserTestMethod(name, grammarName, startRule, input, expectedOutput, expectedErrors);

            case COMPOSITE_PARSER:
                return build.compositeParserTestMethod(name,
                                                       grammarName,
                                                       startRule,
                                                       input,
                                                       expectedOutput,
                                                       expectedErrors,
                                                       slaveGrammars);

        }

        return null;
    }


}
