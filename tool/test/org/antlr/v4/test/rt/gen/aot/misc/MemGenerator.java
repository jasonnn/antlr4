package org.antlr.v4.test.rt.gen.aot.misc;

import org.antlr.v4.test.impl.wip.NewTestCodeGenerator;
import org.antlr.v4.test.rt.gen.*;
import org.antlr.v4.test.rt.gen.aot.AOTPass;

import javax.tools.JavaFileObject;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by jason on 4/20/15.
 */
public
class MemGenerator {

  private final List<JUnitTestFile> testFiles;


  public
  MemGenerator() throws Exception {
    Generator g = Generator.javaTarget();
    //    Collections.sort(testFiles, new Comparator<JUnitTestFile>() {
//      @Override
//      public
//      int compare(JUnitTestFile t0, JUnitTestFile t1) {
//        return t1.unitTests.size() - t0.unitTests.size();
//        // return t0.name.compareTo(t1.name);
//      }
//    });
    this.testFiles = new ArrayList<JUnitTestFile>(g.buildTests());
  }

  static
  Callable<List<BytecodeFileObject>> tests(final JUnitTestFile testFile,
                                           final BlockingQueue<BytecodeFileObject> queue,
                                           final CountDownLatch latch) {
    return new Callable<List<BytecodeFileObject>>() {
      @Override
      public
      List<BytecodeFileObject> call() throws Exception {
        List<BytecodeFileObject> accum = new ArrayList<BytecodeFileObject>();
        for (JUnitTestMethod test : testFile.unitTests) {
          if (ConcreteParserTestMethod.class.isAssignableFrom(test.getClass())) continue;
          String pkgName = testFile.name + ".Test" + test.name;

          List<JavaFileObject> javaSources = runAntlr(testFile, test);
          javaSources.add(GenerateTestFile.visit(test, pkgName));

          assert !javaSources.isEmpty();

          TestCompiler compiler = new TestCompiler(javaSources, accum);
          JavacOutput compile = compiler.compile();
          if (!compile.success) compile.log();
          queue.addAll(compile.compiledClasses);
        }
        latch.countDown();
        return accum;
      }
    };
  }

//  void runParallel() throws IOException {
//    File jarFile = new File("/Users/jason/Desktop/tmp/tests.jar");
//    Manifest manifest = new Manifest();
//    JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)), manifest);
//
//    testFiles.stream()
//             .flatMap(tf -> tf.unitTests.stream())
//             .filter(tm -> !(tm instanceof ConcreteParserTestMethod))
//             .flatMap((tm) -> {
//               List<JavaFileObject> javaSources = runAntlr(tm.getFile(), tm);
//               assert !javaSources.isEmpty();
//               String pkgName = tm.getFile().name + ".Test" + tm.name;
//               javaSources.add(GenerateTestFile.visit(tm, pkgName));
//               return new TestCompiler(javaSources).compile().compiledClasses.stream();
//             })
//        .parallel()// sequential()
//        .collect(Collectors.toList())
//        .forEach(fileObject -> {
//          JarEntry entry = new JarEntry(fileObject.getName() + ".class");
//          try {
//            out.putNextEntry(entry);
//            out.write(fileObject.getBytes());
//            out.closeEntry();
//          } catch (IOException e) {
//            e.printStackTrace();
//          }
//
//        });
//
//  }


  void batchRun() throws Exception {
    List<JavaFileObject> sources = new ArrayList<JavaFileObject>(2000);
    long t0 = System.nanoTime();
    for (JUnitTestFile testFile : testFiles) {
      for (JUnitTestMethod test : testFile.unitTests) {
        if (!ConcreteParserTestMethod.class.isAssignableFrom(test.getClass())) {
          String pkgName = testFile.name + ".Test" + test.name;
          TestingTool tool = new TestingTool(new HashMap<String, ToolInput>(), sources);
          tool.gen_visitor = true;
          tool.gen_listener = true;
          tool.genPackage = pkgName;
          tool.outputDirectory = testFile.name + "/Test" + test.name;
          AntlrPass.INSTANCE.beginVisit(test, tool);
          sources.add(GenerateTestFile.visit(test, pkgName));
        }
      }

    }
    long t1 = System.nanoTime();
    double time = (t1 - t0) / 1.0E09;
    System.out.printf("antlr time : %.3f %n", time);

    TestCompiler compiler = new TestCompiler(sources);
    JavacOutput compile = compiler.compile();
    if (!compile.success) compile.log();
    t0 = System.nanoTime();
    time = (t0 - t1) / 1.0E09;
    System.out.printf("javac time : %.3f %n", time);


    File jarFile = new File("/Users/jason/Desktop/tmp/tests.jar");
    Manifest manifest = new Manifest();
    JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)), manifest);

    for (BytecodeFileObject compiledFile : compile.compiledClasses) {
      JarEntry entry = new JarEntry(compiledFile.getName() + ".class");
      out.putNextEntry(entry);
      out.write(compiledFile.getBytes());
      out.closeEntry();
    }
    out.close();
    t1 = System.nanoTime();
    time = (t1 - t0) / 1.0E09;
    System.out.printf("jar time : %.3f %n", time);

  }


  void run() throws IOException {
    JarGen gen = new JarGen();

    for (JUnitTestFile testFile : testFiles) {
      long t0 = System.nanoTime();
      for (JUnitTestMethod test : testFile.unitTests) {
        if (!ConcreteParserTestMethod.class.isAssignableFrom(test.getClass())) {
          handleTest(testFile, test, gen);
        }
      }
      long t1 = System.nanoTime();
      double time = (t1 - t0) / 1.0E09;
      System.out.printf("%s(%d) time : %.3f %n", testFile.name, testFile.unitTests.size(), time);
    }

    gen.close();
  }

  private
  void handleTest(JUnitTestFile testFile, JUnitTestMethod test, JarGen gen) throws IOException {

    String pkgName = testFile.name + ".Test" + test.name;

    List<JavaFileObject> javaSources = runAntlr(testFile, test);
    javaSources.add(GenerateTestFile.visit(test, pkgName));

    assert !javaSources.isEmpty();

    List<BytecodeFileObject> compiledFiles = compile(javaSources);

    assert !compiledFiles.isEmpty();
    assert compiledFiles.size() >= javaSources.size();


    gen.test(testFile, test, compiledFiles);


  }

  static
  class JarGen {
    JarOutputStream out;

    JarGen() throws IOException {
      File jarFile = new File("/Users/jason/Desktop/tmp/tests.jar");
      Manifest manifest = new Manifest();
      out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)), manifest);
    }

    public
    void test(JUnitTestFile testFile, JUnitTestMethod test, List<BytecodeFileObject> compiledFiles) throws IOException {
      for (BytecodeFileObject compiledFile : compiledFiles) {
        JarEntry entry = new JarEntry(compiledFile.getName() + ".class");
        out.putNextEntry(entry);
        out.write(compiledFile.getBytes());
        out.closeEntry();
      }
    }

    public
    void close() throws IOException {
      out.close();
    }
  }

  public static
  List<BytecodeFileObject> compile(List<JavaFileObject> sources) {
    TestCompiler compiler = new TestCompiler(sources);
    JavacOutput compile = compiler.compile();
    if (!compile.success) compile.log();
    return compile.compiledClasses;
  }


  static
  List<JavaFileObject> runAntlr(JUnitTestFile testFile, JUnitTestMethod test) {
    TestingTool tool = new TestingTool();
    tool.gen_visitor = true;
    tool.gen_listener = true;
    tool.genPackage = testFile.name + ".Test" + test.name;


    tool.outputDirectory = testFile.name + "/Test" + test.name;

    List<JavaFileObject> javaSources = AntlrPass.INSTANCE.beginVisit(test, tool);
    assert !javaSources.isEmpty();
    return javaSources;
  }

  static
  class GenerateTestFile extends AOTPass<JavaFileObject, String> {
    public static final GenerateTestFile INSTANCE = new GenerateTestFile();

    public static
    JavaFileObject visit(JUnitTestMethod testMethod, String pkgName) {
      return INSTANCE.beginVisit(testMethod, pkgName);
    }

    @Override
    public
    JavaFileObject visitAbstractParserTest(AbstractParserTestMethod test, String pkgname) {
      return parserCode(test.grammar.grammarName, test.startRule, pkgname);
    }

    @Override
    public
    JavaFileObject visitParserTest(ParserTestMethod test, String s) {
      return parserCode(test.grammar.grammarName, test.startRule, s);
    }

    static
    JavaFileObject parserCode(String grammarName, String startRule, String pkgName) {
      String lexName = grammarName + "Lexer";
      String parseName = grammarName + "Parser";
      String src = NewTestCodeGenerator.generateParserTest(pkgName, lexName, parseName, startRule);
      return new StringJavaSource(pkgName + ".ParserTest", src);
    }

    @Override
    public
    JavaFileObject visitLexerTest(LexerTestMethod test, String pkgName) {
      String lexName = test.grammar.grammarName;
      if (!test.grammar.lines[0].startsWith("lexer")) {
        lexName = lexName + "Lexer";
      }
      String code = NewTestCodeGenerator.generateLexerTest(pkgName, lexName);
      return new StringJavaSource(pkgName + ".LexerTest", code);

    }
  }

  static
  class AntlrPass extends AOTPass<List<JavaFileObject>, TestingTool> {

    static final AntlrPass INSTANCE = new AntlrPass();

    public static
    List<JavaFileObject> visit(JUnitTestMethod test) {
      return INSTANCE.beginVisit(test, new TestingTool());
    }

    @Override
    public
    List<JavaFileObject> visitCompositeLexerTest(CompositeLexerTestMethod test, TestingTool tool) {
      for (Grammar slaveGrammar : test.slaveGrammars) {
        tool.addGrammarSource(slaveGrammar.grammarName, slaveGrammar.template.render());
      }
      return super.visitCompositeLexerTest(test, tool);
    }

    @Override
    public
    List<JavaFileObject> visitCompositeParserTest(CompositeParserTestMethod test, TestingTool tool) {
      for (Grammar slaveGrammar : test.slaveGrammars) {
        tool.addGrammarSource(slaveGrammar.grammarName, slaveGrammar.template.render());
      }
      return super.visitCompositeParserTest(test, tool);
    }

    @Override
    protected
    List<JavaFileObject> visitTest(JUnitTestMethod test, TestingTool tool) {
      tool.addGrammarSource(test.grammar.grammarName, test.grammar.template.render());
      tool.run(test.grammar.grammarName);
      return tool.generatedJavaSources;
    }
  }


  public static
  void main(String[] args) throws Exception {
    long t0 = System.nanoTime();
    new MemGenerator().run();//runParallel();
    long t1 = System.nanoTime();

    double time = (t1 - t0) / 1.0E09;
    System.out.printf("total time : %.2f", time);
  }


}
