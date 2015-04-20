package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.impl.wip.NewTestCodeGenerator;
import org.antlr.v4.test.rt.gen.AbstractParserTestMethod;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;
import org.antlr.v4.test.rt.gen.LexerTestMethod;
import org.antlr.v4.test.rt.gen.ParserTestMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jason on 4/20/15.
 */
public
class WriteTestPass extends AOTPass<Void, MyGenerator> {
  public static final WriteTestPass INSTANCE = new WriteTestPass();

  public static
  void visit(JUnitTestMethod test, MyGenerator gen) {
    INSTANCE.beginVisit(test, gen);
  }

  @Override
  public
  Void visitLexerTest(LexerTestMethod test, MyGenerator myGenerator) {

    String code = NewTestCodeGenerator.generateLexerTest(myGenerator.pkgName, test.grammar.grammarName);
    writeText(new File(myGenerator.srcPkgDir, "LexerTest.java"), code);
    return super.visitLexerTest(test, myGenerator);
  }

  static
  void writeParseTest(String grammarName, String startRule, MyGenerator generator) {
    String lexName = grammarName + "Lexer";
    String parseName = grammarName + "Parser";
    String code = NewTestCodeGenerator.generateParserTest(generator.pkgName, lexName, parseName, startRule);
    writeText(new File(generator.srcPkgDir, "ParserTest.java"), code);
  }

  @Override
  public
  Void visitParserTest(ParserTestMethod test, MyGenerator myGenerator) {
    writeParseTest(test.grammar.grammarName, test.startRule, myGenerator);
    return super.visitParserTest(test, myGenerator);
  }

  @Override
  public
  Void visitAbstractParserTest(AbstractParserTestMethod test, MyGenerator myGenerator) {
    writeParseTest(test.grammar.grammarName, test.startRule, myGenerator);
    return super.visitAbstractParserTest(test, myGenerator);
  }

  static
  void writeText(File file, String text) {
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
