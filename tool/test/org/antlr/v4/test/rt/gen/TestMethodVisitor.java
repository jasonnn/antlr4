package org.antlr.v4.test.rt.gen;

/**
 * Created by jason on 4/15/15.
 */
public interface TestMethodVisitor {
    void visitAbstractParserTest(AbstractParserTestMethod test);

    void visitCompositeLexerTest(CompositeLexerTestMethod test);

    void visitCompositeParserTest(CompositeParserTestMethod test);

    void visitConcreteParserTest(ConcreteParserTestMethod test);

    void visitParserTest(ParserTestMethod test);

    void visitLexerTest(LexerTestMethod test);


}
