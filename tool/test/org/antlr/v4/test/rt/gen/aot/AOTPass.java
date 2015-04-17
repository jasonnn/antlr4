package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jason on 4/16/15.
 */
public abstract
class AOTPass<R, P> extends TestMethodVisitor<R, P> {
  @Override
  public
  R beginVisit(JUnitTestMethod test, P p) {
    assert !ConcreteParserTestMethod.class.equals(test.getClass());
    return super.beginVisit(test, p);
  }

  @Override
  public final
  R visitConcreteParserTest(ConcreteParserTestMethod test, P p) {
    throw new IllegalArgumentException("SHOULD NOT BE CALLED");
  }

  protected void visitConcreteParser(ConcreteParserTestMethod test, P p){}

  @Override
  public
  R visitAbstractParserTest(AbstractParserTestMethod test, P p) {
    for (ConcreteParserTestMethod derivedTest : test.derivedTests) {
      visitConcreteParserTest(derivedTest,p);
    }
    return super.visitAbstractParserTest(test, p);
  }

  private CodegenContext context;

  protected
  File suiteDir() {
    return context.suiteDir;
  }

  protected
  File cwd() {
    return context.testMethodDir;
  }

  protected
  JUnitTestFile declaringSuite() {
    return context.enclosingFile;
  }

  protected R defaultValue(P p){
    return null;
  }

  protected
  File file(String path) {
    return new File(cwd(), path);
  }

  protected
  void writeText(File file, String text) {
    assert file != null;
    assert text != null;
    try {

      FileWriter fw = new FileWriter(file);
      fw.write(text);
      fw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
