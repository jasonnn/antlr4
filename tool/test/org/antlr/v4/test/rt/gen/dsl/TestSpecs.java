package org.antlr.v4.test.rt.gen.dsl;

import org.antlr.v4.test.rt.gen.CompositeParserTestMethod;
import org.antlr.v4.test.rt.gen.dsl.impl.SpecBuilder;

/**
 * Created by jason on 4/13/15.
 */
public
class TestSpecs extends SpecBuilder {

  class CompositeParserSpecs {
    private String output;
    String input;

    {
      compositeParserTest("DelegatorInvokesDelegateRule")
          .grammarName("M")
          .startRule("s")
          .input("b")
          .expectOutput("S.a\n")
          .expectErrors(null)
          .slaveGrammars("S")
          .build();

      compositeParserTest("BringInLiteralsFromDelegate")
          .grammarName("M")
          .startRule("s")
          .input("=a")
          .expectOutput("S.a")
          .expectErrors(null)
          .slaveGrammars("S")
          .build();
      //.....

      CompositeParserTestMethod ct =
          compositeParserTest("DelegatesSeeSameTokenType")
              .grammarName("M")
              .withStartRule("s")
              .test(input = "aa", output = "S.x\nT.y\n")


              .slaveGrammars("S", "T")
              .build();

      ct.afterGrammar = "writeFile(tmpdir(), \"M.g4\", grammar);\n"
                        +
                        "ErrorQueue equeue = new ErrorQueue();\n"
                        +
                        "Grammar g = new Grammar(tmpdir()+\"/M.g4\", grammar, equeue);\n"
                        +
                        "String expectedTokenIDToTypeMap = \"{EOF=-1, B=1, A=2, C=3, WS=4}\";\n"
                        +
                        "String expectedStringLiteralToTypeMap = \"{'a'=2, 'b'=1, 'c'=3}\";\n"
                        +
                        "String expectedTypeToTokenList = \"[B, A, C, WS]\";\n"
                        +
                        "assertEquals(expectedTokenIDToTypeMap, g.tokenNameToTypeMap.toString());\n"
                        +
                        "assertEquals(expectedStringLiteralToTypeMap, org.antlr.v4.test.TestUtils.sort(g.stringLiteralToTypeMap).toString());\n"
                        +
                        "assertEquals(expectedTypeToTokenList, realElements(g.typeToTokenList).toString());\n"
                        +
                        "assertEquals(\"unexpected errors: \"+equeue, 0, equeue.errors.size());\n";


      parserTest("myTest")
          .grammarName("something")
          .withStartRule("start")
          .test(0,
                input = "abc",
                output = "cde"
               )
          .test(1,
                input = "adasdasd",
                output = "something big "
                         + "\n"
                         + "with multiple lines!"
               )
          .build();


    }
  }

  class CompositeLexerSpecs {
    {
      compositeLexerTest("LexerDelegatorInvokesDelegateRule")
          .grammarName("M")
          .input("abc")
          .expectOutput("S.A\n" +
                        "[@0,0:0='a',<3>,1:0]\n" +
                        "[@1,1:1='b',<1>,1:1]\n" +
                        "[@2,2:2='c',<4>,1:2]\n" +
                        "[@3,3:2='<EOF>',<-1>,1:3]\n")
          .expectErrors(null)
          .slaveGrammars("S")
          .build();

      compositeLexerTest("LexerDelegatorRuleOverridesDelegate")
          .grammarName("M")
          .input("ab")
          .expectOutput("M.A\n" +
                        "[@0,0:1='ab',<1>,1:0]\n" +
                        "[@1,2:1='<EOF>',<-1>,1:2]\n")
          //.expectErrors(null)
          .slaveGrammars("S");
    }
  }
}
