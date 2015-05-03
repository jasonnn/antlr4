package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;
import org.antlr.v4.test.rt.gen.dsl.BuilderApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 4/13/15.
 */
public abstract
class SpecBuilder implements BuilderApi.Factory {
  List<LexerTestMethod> lexerTestMethods = new ArrayList<LexerTestMethod>();
  List<CompositeLexerTestMethod> compositeLexerTestMethods = new ArrayList<CompositeLexerTestMethod>();
  List<ParserTestMethod> parserTestMethods = new ArrayList<ParserTestMethod>();
  List<CompositeParserTestMethod> compositeParserTestMethods = new ArrayList<CompositeParserTestMethod>();

  final BuildFactory factory = new BuildFactory() {
    @Override
    public
    LexerTestMethod lexerTestMethod(String name,
                                    String grammarName,
                                    String input,
                                    String expectedOutput,
                                    String expectedErrors,
                                    Integer ruleIndex) {
      LexerTestMethod m = super.lexerTestMethod(name, grammarName, input, expectedOutput, expectedErrors, ruleIndex);
      lexerTestMethods.add(m);
      return m;
    }

    @Override
    public
    CompositeLexerTestMethod compositeLexerTestMethod(String name,
                                                      String grammarName,
                                                      String input,
                                                      String expectedOutput,
                                                      String expectedErrors,
                                                      String[] slaveGrammars) {
      CompositeLexerTestMethod m = super.compositeLexerTestMethod(name,
                                                                  grammarName,
                                                                  input, expectedOutput, expectedErrors, slaveGrammars);
      compositeLexerTestMethods.add(m);
      return m;
    }

    @Override
    public
    ParserTestMethod parserTestMethod(String name,
                                      String grammarName,
                                      String startRule,
                                      String input,
                                      String expectedOutput,
                                      String expectedErrors) {
      ParserTestMethod m = super.parserTestMethod(name, grammarName, startRule, input, expectedOutput, expectedErrors);
      parserTestMethods.add(m);
      return m;
    }

    @Override
    public
    CompositeParserTestMethod compositeParserTestMethod(String name,
                                                        String grammarName,
                                                        String startRule,
                                                        String input,
                                                        String expectedOutput,
                                                        String expectedErrors,
                                                        String[] slaveGrammars) {
      CompositeParserTestMethod m = super.compositeParserTestMethod(name,
                                                                    grammarName,
                                                                    startRule,
                                                                    input,
                                                                    expectedOutput,
                                                                    expectedErrors,
                                                                    slaveGrammars);
      compositeParserTestMethods.add(m);
      return m;
    }
  };

  @Override
  public
  BuilderApi.LexerTestBuilder lexerTest(String name) {
    return new LazyBuilder(factory).name(name).asLexerTestBuilder();
  }

  @Override
  public
  BuilderApi.CompositeLexerTestBuilder compositeLexerTest(String name) {
    return new LazyBuilder(factory).name(name).asCompositeLexerTestBuilder();
  }
//
//  @Override
//  public
//  BuilderApi.ParseTestBuilder parserTest(String name) {
//    return new LazyBuilder(factory).name(name).asParseTestBuilder();
//  }
//
//  @Override
//  public
//  BuilderApi.CompositeParseTestBuilder compositeParserTest(String name) {
//    return new LazyBuilder(factory).name(name).asCompositeParseTestBuilder();
//  }


}
