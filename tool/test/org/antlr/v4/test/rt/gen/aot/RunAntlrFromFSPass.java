package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.Tool;
import org.antlr.v4.test.rt.gen.JUnitTestFile;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;

import java.io.File;

/**
 * Created by jason on 4/16/15.
 */
public
class RunAntlrFromFSPass extends AOTPass<Void, RunAntlrFromFSPass.Input> {
  public static final RunAntlrFromFSPass INSTANCE =new RunAntlrFromFSPass();
  public static void visit(JUnitTestMethod testMethod,Input input){
    INSTANCE.beginVisit(testMethod, input);
  }

  interface Input {
    JUnitTestFile getFile();
    File cwd();
  }


  static
  class MyTool extends Tool {
    void setHasOutput() {
      haveOutputDir = true;
    }

    public
    void addInput(String file) {
      grammarFiles.add(file);
    }
  }

  @Override
  public
  Void beginVisit(JUnitTestMethod test, Input input) {
    File cwd = input.cwd();
    JUnitTestFile suite = input.getFile();

    MyTool tool = new MyTool();
    tool.gen_visitor = true;
    tool.gen_listener = true;

    tool.inputDirectory = cwd.getAbsoluteFile();
    tool.libDirectory = cwd.getAbsolutePath();
    tool.genPackage = suite.name + ".Test" + test.name;

    if (!Character.isJavaIdentifierStart(test.name.codePointAt(0))) {
      System.out.println("bad name: " + test.name);
    }

    File pkgDir = new File(cwd, "src/" + suite.name + "/Test" + test.name);
    boolean b = pkgDir.mkdirs();
    assert b;

    tool.outputDirectory = pkgDir.getAbsolutePath();
    tool.setHasOutput();

    tool.addInput(new File(input.cwd(), test.grammar.grammarName + ".g4").getAbsolutePath());

    tool.processGrammarsOnCommandLine();

    return null;
  }

}
