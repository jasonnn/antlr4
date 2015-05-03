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
class WriteInitialFilesVisitor extends TestBuildingVisitor<Void, MyGenerator> {
  public static final WriteInitialFilesVisitor INSTANCE = new WriteInitialFilesVisitor();

  public static
  void visit(JUnitTestMethod testMethod, MyGenerator generator) {
    testMethod.accept(INSTANCE, generator);
  }

  @Override
  public
  Void visitCompositeParserTest(CompositeParserTestMethod test, MyGenerator p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar, p);
    return super.visitCompositeParserTest(test, p);

  }

  @Override
  public
  Void visitCompositeLexerTest(CompositeLexerTestMethod test, MyGenerator p) {
    for (Grammar grammar : test.slaveGrammars) writeGrammar(grammar, p);
    return super.visitCompositeLexerTest(test, p);
  }

  @Override
  public
  Void visitConcreteParserTest(ConcreteParserTestMethod test, MyGenerator ctx) {
    String suffix = test.name.substring(test.baseName.length(), test.name.length());
    File dir = ctx.cwd;
    if (test.input != null) {
      writeText(file(dir, "input" + suffix + ".txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file(dir, "output" + suffix + ".txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file(dir, "errors" + suffix + ".txt"), test.expectedErrors);
    }
    return super.visitConcreteParserTest(test, ctx);
  }

  //TODO unescape when writing, escape when reading
  // (input/output are pre-escaped)
  //see: http://stackoverflow.com/a/14541345
  // https://gist.github.com/uklimaschewski/6741769
  @Override
  protected
  Void visitTest(JUnitTestMethod test, MyGenerator generator) {
    File dir = generator.cwd;
    writeGrammar(test.grammar, generator);
    if (test.input != null) {
      writeText(file(dir, "input.txt"), test.input);
    }
    if (test.expectedOutput != null) {
      writeText(file(dir, "output.txt"), test.expectedOutput);
    }
    if (test.expectedErrors != null) {
      writeText(file(dir, "errors.txt"), test.expectedErrors);
    }
    return super.visitTest(test, generator);
  }

  void writeGrammar(Grammar grammar, MyGenerator ctx) {
    writeText(file(ctx.cwd, grammar.grammarName + ".g4"), grammar.template.render());
  }

  static
  URI file(File parent, String relativeName) {
    return parent.toURI().resolve(relativeName);
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
