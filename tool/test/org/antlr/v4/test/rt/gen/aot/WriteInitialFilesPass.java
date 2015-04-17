package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

/**
 * Created by jason on 4/16/15.
 */
public
class WriteInitialFilesPass extends AOTPass<Void, URI> {
  public static final WriteInitialFilesPass INSTANCE = new WriteInitialFilesPass();
  public static void visit(JUnitTestMethod testMethod,URI uri){
    INSTANCE.beginVisit(testMethod, uri);
  }

  //TODO unescape when writing, escape when reading
  // (input/output are pre-escaped)
  //see: http://stackoverflow.com/a/14541345
  // https://gist.github.com/uklimaschewski/6741769

  @Override
  public
  Void beginVisit(JUnitTestMethod test, URI uri) {
    writeGrammar(test.grammar, uri);
    if (test.input != null) {
      writeText(file(uri, "input.txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file(uri, "output.txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file(uri, "errors.txt"), test.expectedErrors);
    }

    return super.beginVisit(test, uri);
  }

  @Override
  public
  Void visitCompositeParserTest(CompositeParserTestMethod test, URI p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar, p);
    return super.visitCompositeParserTest(test, p);

  }

  @Override
  public
  Void visitCompositeLexerTest(CompositeLexerTestMethod test, URI p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar, p);
    return super.visitCompositeLexerTest(test, p);
  }

  @Override
  public
  Void visitConcreteParserTest(ConcreteParserTestMethod test, URI uri) {
    String suffix = test.name.substring(test.baseName.length(), test.name.length());
    if (test.input != null) {
      writeText(file(uri, "input" + suffix + ".txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file(uri, "output" + suffix + ".txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file(uri, "errors" + suffix + ".txt"), test.expectedErrors);
    }
    return super.visitConcreteParserTest(test, uri);
  }

  void writeGrammar(Grammar grammar, URI uri) {
    writeText(file(uri, grammar.grammarName + ".g4"), grammar.template.render());
  }

  static
  URI file(URI parent, String relativeName) {
    return parent.resolve(relativeName);
  }

  static
  void writeText(URI uri, String text) {
    File file = new File(uri);
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
