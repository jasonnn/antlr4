package org.antlr.v4.test.rt.gen;

/**
 * Created by jason on 4/15/15.
 */
public class TestMethodVisitor {
    public void visitAbstractParserTest(AbstractParserTestMethod test) {
        visitTest(test);
    }

    public void visitCompositeLexerTest(CompositeLexerTestMethod test) {
        visitLexerTest(test);
    }

    public void visitCompositeParserTest(CompositeParserTestMethod test) {
        visitParserTest(test);
    }

    public void visitConcreteParserTest(ConcreteParserTestMethod test) {
        visitTest(test);
    }

    public void visitParserTest(ParserTestMethod test) {
        visitTest(test);
    }

    public void visitLexerTest(LexerTestMethod test) {
        visitTest(test);
    }

    public void visitTest(JUnitTestMethod test){

    }
}
