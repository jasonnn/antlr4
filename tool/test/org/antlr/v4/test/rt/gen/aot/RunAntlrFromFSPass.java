package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.Tool;
import org.antlr.v4.test.rt.gen.JUnitTestFile;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;
import org.antlr.v4.tool.ANTLRMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 4/16/15.
 */
public
class RunAntlrFromFSPass extends AOTPass<Void, MyGenerator> {
  public static final RunAntlrFromFSPass INSTANCE = new RunAntlrFromFSPass();

  public static
  void visit(JUnitTestMethod testMethod, MyGenerator input) {
    INSTANCE.beginVisit(testMethod, input);
  }

  static
  class MyTool extends Tool {
    List<String> messages = new ArrayList<String>();

    public
    MyTool(JUnitTestMethod test) {
      from = test.getFile().name + "[" + test.name + "]";
    }

    final String from;

    void setHasOutput() {
      haveOutputDir = true;
    }

    public
    void addInput(String file) {
      grammarFiles.add(file);
    }

    @Override
    public
    void warning(ANTLRMessage msg) {
      // System.out.printf("%s : %s%n", from, errMgr.getMessageTemplate(msg).render());

    }

    @Override
    public
    void error(ANTLRMessage msg) {
      messages.add(errMgr.getMessageTemplate(msg).render());
    }

    @Override
    public
    void info(String msg) {
      // System.out.printf("info from %s : %n\t%s", from, msg);
    }
  }

  @Override
  public
  Void beginVisit(JUnitTestMethod test, MyGenerator ctx) {
    File cwd = ctx.cwd;
    JUnitTestFile suite = ctx.currentFile;

    MyTool tool = new MyTool(test);
    tool.gen_visitor = true;
    tool.gen_listener = true;

    tool.inputDirectory = cwd.getAbsoluteFile();
    tool.libDirectory = cwd.getAbsolutePath();
    tool.genPackage = ctx.pkgName;

//    if (!Character.isJavaIdentifierStart(test.name.codePointAt(0))) {
//      System.out.println("bad name: " + test.name);
//    }

//    File pkgDir = new File(cwd, "src/" + suite.name + "/Test" + test.name);
//    boolean b = pkgDir.mkdirs();
//    assert b;

    tool.outputDirectory = ctx.srcPkgDir.getAbsolutePath();// pkgDir.getAbsolutePath();
    tool.setHasOutput();

    tool.addInput(new File(cwd, test.grammar.grammarName + ".g4").getAbsolutePath());

    tool.processGrammarsOnCommandLine();

    if (!tool.messages.isEmpty()) {
      String header = test.getFile().name + "[" + test.name + "]";
      System.err.println(header);
      for (String s : tool.messages) {
        System.err.println("\t" + s);

      }

    }

    return null;
  }

}
