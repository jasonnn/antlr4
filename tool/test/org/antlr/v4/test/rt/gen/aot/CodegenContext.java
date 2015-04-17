package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.JUnitTestFile;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;

import java.io.File;

/**
 * Created by jason on 4/16/15.
 */
public
class CodegenContext {

  public
  CodegenContext(File allTestsDir) {
    this.allTestsDir = allTestsDir;
  }


  final File allTestsDir;

  public File suiteDir;

  public JUnitTestFile enclosingFile;

  public File testMethodDir;

  public
  CodegenContext update(JUnitTestFile file) {
    File suiteDir = new File(allTestsDir, file.name);
    boolean b = suiteDir.mkdirs();
    assert b;
    this.suiteDir = suiteDir;
    this.enclosingFile = file;
    return this;
  }

  public
  CodegenContext update(JUnitTestMethod testMethod) {
    File cwd = new File(suiteDir, testMethod.name);
    boolean b = cwd.mkdir();
    assert b;
    this.testMethodDir = cwd;
    return this;
  }


}
