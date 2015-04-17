package org.antlr.v4.test.rt.gen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 4/15/15.
 */
public
class TestMethodVisitors {
  public static
  class Delegator implements TestMethodVisitor {
    public TestMethodVisitor delegate;

    public
    Delegator() {
    }

    public
    Delegator(TestMethodVisitor delegate) {this.delegate = delegate;}

    @Override
    public
    void visitAbstractParserTest(AbstractParserTestMethod test) {
      if (delegate != null) delegate.visitAbstractParserTest(test);
    }

    @Override
    public
    void visitCompositeLexerTest(CompositeLexerTestMethod test) {
      if (delegate != null) delegate.visitCompositeLexerTest(test);
    }

    @Override
    public
    void visitCompositeParserTest(CompositeParserTestMethod test) {
      if (delegate != null) delegate.visitCompositeParserTest(test);
    }

    @Override
    public
    void visitConcreteParserTest(ConcreteParserTestMethod test) {
      if (delegate != null) delegate.visitConcreteParserTest(test);
    }

    @Override
    public
    void visitParserTest(ParserTestMethod test) {
      if (delegate != null) delegate.visitParserTest(test);
    }

    @Override
    public
    void visitLexerTest(LexerTestMethod test) {
      if (delegate != null) delegate.visitLexerTest(test);
    }
  }

  public static abstract
  class Generalizer implements TestMethodVisitor {

    @Override
    public
    void visitAbstractParserTest(AbstractParserTestMethod test) {
      visitTest(test);
    }

    @Override
    public
    void visitCompositeLexerTest(CompositeLexerTestMethod test) {
      visitLexerTest(test);
    }

    @Override
    public
    void visitCompositeParserTest(CompositeParserTestMethod test) {
      visitParserTest(test);
    }

    @Override
    public
    void visitConcreteParserTest(ConcreteParserTestMethod test) {
      visitTest(test);
    }

    @Override
    public
    void visitParserTest(ParserTestMethod test) {
      visitTest(test);
    }

    @Override
    public
    void visitLexerTest(LexerTestMethod test) {
      visitTest(test);
    }

    protected abstract
    void visitTest(JUnitTestMethod test);
  }

  public static
  class Multiplexer implements TestMethodVisitor {
    public final List<TestMethodVisitor> visitors = new ArrayList<TestMethodVisitor>();

    @Override
    public
    void visitAbstractParserTest(AbstractParserTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitAbstractParserTest(test);
      }
    }

    @Override
    public
    void visitCompositeLexerTest(CompositeLexerTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitCompositeLexerTest(test);
      }
    }

    @Override
    public
    void visitCompositeParserTest(CompositeParserTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitCompositeParserTest(test);
      }
    }

    @Override
    public
    void visitConcreteParserTest(ConcreteParserTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitConcreteParserTest(test);
      }
    }

    @Override
    public
    void visitParserTest(ParserTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitParserTest(test);
      }
    }

    @Override
    public
    void visitLexerTest(LexerTestMethod test) {
      for (TestMethodVisitor delegate : visitors) {
        delegate.visitLexerTest(test);
      }
    }
  }

}
