package org.antlr.v4.test.rt.gen;

/**
 * Created by jason on 4/15/15.
 *
 * note somewhat abnormal dispatch:
 *
 * testMethod.accept -> visitor.visitXXX...-> visitor.visitTest
 */
public abstract
class TestMethodVisitor<R, P> {
  public
  R visitAbstractParserTest(AbstractParserTestMethod test, P p) {return visitTest(test, p);}

  public
  R visitCompositeLexerTest(CompositeLexerTestMethod test, P p) {return visitLexerTest(test, p);}

  public
  R visitCompositeParserTest(CompositeParserTestMethod test, P p) {return visitParserTest(test, p);}

  public
  R visitConcreteParserTest(ConcreteParserTestMethod test, P p) {return visitTest(test, p);}

  public
  R visitParserTest(ParserTestMethod test, P p) {return visitTest(test, p);}

  public
  R visitLexerTest(LexerTestMethod test, P p) {return visitTest(test, p);}

  protected
  R visitTest(JUnitTestMethod test, P p){
    return defaultValue(p);
  }

  protected R defaultValue(P p){
    return null;
  }

}
