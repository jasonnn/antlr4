package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.*;

import java.io.File;
import java.util.List;

/**
 * Created by jason on 4/16/15.
 */
public
class WriteInitialFilesPass<P> extends AOTPass<WriteInitialFilesPass.Output, P> {


  public static class ResultBuilder {
   boolean writeToFile;
    List<String> javaFiles;
  }


  public static class Output {

  }



  //TODO unescape when writing, escape when reading
  // (input/output are pre-escaped)
  //see: http://stackoverflow.com/a/14541345
  // https://gist.github.com/uklimaschewski/6741769

  @Override
  public
  Output beginVisit(JUnitTestMethod test, P p) {
    writeGrammar(test.grammar);
    if (test.input != null) {
      writeText(file("input.txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file("output.txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file("errors.txt"), test.expectedErrors);
    }

    return super.beginVisit(test, p);
  }

  @Override
  public
  Output visitCompositeParserTest(CompositeParserTestMethod test, P p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar);
    return super.visitCompositeParserTest(test, p);

  }

  @Override
  public
  Output visitCompositeLexerTest(CompositeLexerTestMethod test, P p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar);
    return super.visitCompositeLexerTest(test, p);
  }

  @Override
  protected
  void visitConcreteParser(ConcreteParserTestMethod test, P p) {
    String suffix = test.name.substring(test.baseName.length(), test.name.length());
    if (test.input != null) {
      writeText(file("input" + suffix + ".txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file("output" + suffix + ".txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file("errors" + suffix + ".txt"), test.expectedErrors);
    }
  }

  void writeGrammar(Grammar grammar) {
    writeText(new File(cwd(), grammar.grammarName + ".g4"), grammar.template.render());
  }
}
