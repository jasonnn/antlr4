package org.antlr.v4.test.rt.gen.aot;


import org.antlr.v4.Tool;
import org.antlr.v4.test.rt.gen.*;

import javax.tools.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
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
    g.output = cwd = new File("/Users/jason/Desktop/tmp/tst");
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
        if (ConcreteParserTestMethod.class.equals(unitTest.getClass())) continue;

        context.update(unitTest);


      }


    }

//    TestMethodVisitors.Multiplexer rootVisitor = new TestMethodVisitors.Multiplexer() {
//      @Override
//      public
//      void visitConcreteParserTest(ConcreteParserTestMethod test) {}
//    };
//    rootVisitor.visitors.add(new MkDirVisitor());
//    rootVisitor.visitors.add(new WriteInputFiles());
//    rootVisitor.visitors.add(new RunAntlr());
//    rootVisitor.visitors.add(new Compile());
//
//
//    for (JUnitTestFile testGroup : testGroups) {
//      currentGroup = testGroup;
//
//      baseDir = cwd = new File(g.output, testGroup.name);
//      b = baseDir.mkdir();
//      assert b;
//      testGroup.visitTests(rootVisitor);
//    }
  }


//  class MkDirVisitor extends TestMethodVisitors.Generalizer {
//    @Override
//    protected
//    void visitTest(JUnitTestMethod test) {
//      cwd = new File(baseDir, test.name);
//      boolean b = cwd.mkdir();
//      assert b;
//    }
//  }

//  class WriteInputFiles extends TestMethodVisitors.Generalizer {
//    @Override
//    public
//    void visitCompositeParserTest(CompositeParserTestMethod test) {
//      super.visitCompositeParserTest(test);
//      for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar);
//    }
//
//    @Override
//    public
//    void visitCompositeLexerTest(CompositeLexerTestMethod test) {
//      super.visitCompositeLexerTest(test);
//      for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar);
//    }
//
//    @Override
//    public
//    void visitAbstractParserTest(AbstractParserTestMethod test) {
//      super.visitAbstractParserTest(test);
//      for (ConcreteParserTestMethod derivedTest : test.derivedTests) {
//        realVisitConcreteParser(derivedTest);
//      }
//    }
//
//    void realVisitConcreteParser(ConcreteParserTestMethod test) {
//      String suffix = test.name.substring(test.baseName.length(), test.name.length());
//      if (test.input != null) {
//        writeText(file("input" + suffix + ".txt"), test.input);
//      }
//      if (test.expectedOutput != null) {
//        writeText(file("output" + suffix + ".txt"), test.expectedOutput);
//      }
//      if (test.expectedErrors != null) {
//        writeText(file("errors" + suffix + ".txt"), test.expectedErrors);
//      }
//    }
//
//    //TODO unescape when writing, escape when reading
//    // (input/output are pre-escaped)
//    //see: http://stackoverflow.com/a/14541345
//    // https://gist.github.com/uklimaschewski/6741769
//    @Override
//    protected
//    void visitTest(JUnitTestMethod test) {
//      writeGrammar(test.grammar);
//      if (test.input != null) {
//        writeText(file("input.txt"), test.input);
//      }
//      if (test.expectedOutput != null) {
//        writeText(file("output.txt"), test.expectedOutput);
//      }
//      if (test.expectedErrors != null) {
//        writeText(file("errors.txt"), test.expectedErrors);
//      }
//    }
//  }

  class RunAntlr extends TestMethodVisitors.Generalizer {
    String packageName(JUnitTestMethod testMethod) {
      return currentGroup.name + ".Test" + testMethod.name;
    }

    File srcDir(JUnitTestMethod testMethod) {
      return new File(cwd, "src/" + currentGroup.name + "/Test" + testMethod.name);
    }

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
    protected
    void visitTest(JUnitTestMethod test) {

      MyTool tool = new MyTool();
      tool.gen_visitor = true;
      tool.gen_listener = true;

      tool.inputDirectory = cwd.getAbsoluteFile();
      tool.libDirectory = cwd.getAbsolutePath();
      tool.genPackage = packageName(test);

      if (!Character.isJavaIdentifierStart(test.name.codePointAt(0))) {
        System.out.println("bad name: " + test.name);
      }

      File pkgDir = srcDir(test);
      boolean b = pkgDir.mkdirs();
      assert b;

      tool.outputDirectory = pkgDir.getAbsolutePath();
      tool.setHasOutput();

      tool.addInput(new File(cwd, test.grammar.grammarName + ".g4").getAbsolutePath());

      tool.processGrammarsOnCommandLine();
    }
  }

  class Compile extends TestMethodVisitors.Generalizer {
    @Override
    protected
    void visitTest(JUnitTestMethod test) {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

      StandardJavaFileManager manager = compiler.getStandardFileManager(ERROR_LISTENER,
                                                                        Locale.getDefault(),
                                                                        Charset.forName("UTF-8"));

      File srcDir = new File(cwd, "src");

      assert srcDir.exists();
      assert srcDir.isDirectory();


      Iterable<? extends JavaFileObject> compilationUnits =
          manager.getJavaFileObjectsFromFiles(Files.findJavaFiles(srcDir));

      assert compilationUnits.iterator().hasNext();


      try {
        manager.setLocation(StandardLocation.SOURCE_PATH, Collections.singleton(srcDir));
        File bin = new File(cwd, "bin");
        boolean b = bin.mkdir();
        assert b;
        manager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(bin));
      } catch (IOException e) {
        e.printStackTrace();
      }


      JavaCompiler.CompilationTask task = compiler.getTask(new OutputStreamWriter(System.err),
                                                           manager,
                                                           ERROR_LISTENER,
                                                           Collections.<String>emptySet(),
                                                           Collections.<String>emptySet(),
                                                           compilationUnits);
      task.call();

      try {
        manager.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
  }

  File baseDir;

  JUnitTestFile currentGroup;

  File cwd;


  File file(String path) {
    return new File(cwd, path);
  }


  void writeGrammar(Grammar grammar) {
    writeText(new File(cwd, grammar.grammarName + ".g4"), grammar.template.render());
  }

  static
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

  static final DiagnosticListener<JavaFileObject> ERROR_LISTENER = new DiagnosticListener<JavaFileObject>() {
    @Override
    public
    void report(Diagnostic<? extends JavaFileObject> diagnostic) {
      if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
        System.err.println(diagnostic);
      }
    }
  };

  public static
  void main(String[] args) throws Exception {
    new MyGenerator();
  }
}
