package org.antlr.v4.test.rt.gen.dsl.impl;

import org.antlr.v4.test.rt.gen.dsl.BuilderApi;

/**
 * Created by jason on 4/11/15.
 */
public class FactoryImpl implements BuilderApi.Factory {


    @Override
    public BuilderApi.LexerTestBuilder lexerTest(String name) {
        return new BuilderImpls.LexerTestBuilderImpl(name);
    }

    @Override
    public BuilderApi.CompositeLexerTestBuilder compositeLexerTest(String name) {
        return new BuilderImpls.CompositeLexerTestBuilderImpl(name);
    }

    @Override
    public BuilderApi.ParseTestBuilder parserTest(String name) {
        return new BuilderImpls.ParserTestBuilderImpl(name);
    }

    @Override
    public BuilderApi.CompositeParseTestBuilder compositeParserTest(String name) {
        return new BuilderImpls.CompositeParserTestBuilderImpl(name);
    }
}
