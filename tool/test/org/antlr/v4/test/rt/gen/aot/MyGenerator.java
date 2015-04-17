package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.*;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by jason on 4/14/15.
 */
public
class MyGenerator {
  static final
  Logger log = Logger.getLogger(MyGenerator.class.getName());

  Generator g;
  Collection<JUnitTestFile> testGroups;

  MyGenerator() throws Exception {
    g = Generator.javaTarget();
    File cwd= g.output = new File("/Users/jason/Desktop/tmp/tst");
    if (cwd.exists()) Files.deleteRecurisvely(cwd);
    boolean b = cwd.mkdirs();
    assert b;

    testGroups = g.buildTests();
    assert testGroups != null;
    assert g.group != null;

    CodegenContext context = new CodegenContext(g.output);

    for (JUnitTestFile file : testGroups) {
      context.update(file);
      for (JUnitTestMethod unitTest : file.unitTests) {
        if (ConcreteParserTestMethod.class.isAssignableFrom(unitTest.getClass())) continue;
        context.update(unitTest);
        WriteInitialFilesPass.visit(unitTest, context.cwd);
        RunAntlrFromFSPass.visit(unitTest, context);
        CompileFromFSPass.visit(unitTest,context.testMethodDir);
      }
    }
  }


  public static
  void main(String[] args) throws Exception {
    new MyGenerator();
  }
}
