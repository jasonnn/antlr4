package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.AbstractParserTestMethod;
import org.antlr.v4.test.rt.gen.ConcreteParserTestMethod;
import org.antlr.v4.test.rt.gen.TestMethodVisitor;

/**
 * Created by jason on 4/16/15.
 */
public abstract
class TestBuildingVisitor<R, P> extends TestMethodVisitor<R, P> {

  @Override
  public
  R visitConcreteParserTest(ConcreteParserTestMethod test, P p) {
    return defaultValue(p);
  }


  @Override
  public
  R visitAbstractParserTest(AbstractParserTestMethod test, P p) {
    for (ConcreteParserTestMethod derivedTest : test.derivedTests) {
      visitConcreteParserTest(derivedTest, p);
    }
    return super.visitAbstractParserTest(test, p);
  }

  protected
  R defaultValue(P p) {
    return null;
  }

}
