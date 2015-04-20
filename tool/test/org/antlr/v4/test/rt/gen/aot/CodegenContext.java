package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.JUnitTestFile;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;

import java.io.File;
import java.net.URI;

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
  URI cwd;

  public
  JUnitTestFile update(JUnitTestFile file) {
    File suiteDir = new File(allTestsDir, file.name);
    boolean b = suiteDir.mkdirs();
    assert b;
    this.suiteDir = suiteDir;
    this.enclosingFile = file;
    return file;
  }

  public
  CodegenContext update(JUnitTestMethod testMethod) {
    File cwd = new File(suiteDir, testMethod.name);
    boolean b = cwd.mkdirs();
    assert b : cwd.getPath();
    this.testMethodDir = cwd;
    this.cwd=cwd.toURI();
    return this;
  }


  public
  JUnitTestFile getFile() {
    return enclosingFile;
  }

  public
  File cwd() {
    return testMethodDir;
  }
}
