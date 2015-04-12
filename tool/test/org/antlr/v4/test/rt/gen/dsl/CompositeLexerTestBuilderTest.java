package org.antlr.v4.test.rt.gen.dsl;

import org.antlr.v4.test.rt.gen.CompositeLexerTestMethod;
import org.antlr.v4.test.rt.gen.dsl.impl.FactoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jason on 4/11/15.
 */
public class CompositeLexerTestBuilderTest {
    @Test
    public void testDoesIdeaKnowWhatToDoWithThis() throws Exception {
        CompositeLexerTestBuilder builder = new CompositeLexerTestBuilder();
        CompositeLexerTestMethod test =
                builder.test("LexerDelegatorInvokesDelegateRule")
                       .grammarName("M")
                       .input("abc")
                       .expectedOutput("S.A\n" +
                                       "[@0,0:0='a',<3>,1:0]\n" +
                                       "[@1,1:1='b',<1>,1:1]\n" +
                                       "[@2,2:2='c',<4>,1:2]\n" +
                                       "[@3,3:2='<EOF>',<-1>,1:3]\n")
                       .expectedErrors(null)
                       .slaveGrammars("S");

        assertEquals("LexerDelegatorInvokesDelegateRule", test.name);
        assertEquals("M", test.grammar.grammarName);
        assertEquals("abc", test.input);
        assertEquals(test.expectedOutput,
                     "S.A\\n[@0,0:0='a',<3>,1:0]\\n[@1,1:1='b',<1>,1:1]\\n[@2,2:2='c',<4>,1:2]\\n[@3,3:2='<EOF>',<-1>,1:3]\\n");
        assertEquals(null, test.expectedErrors);
        assertEquals("S", test.slaveGrammars[0].grammarName);

    }

    class TestModuleBuilder {
        BuilderApi.GrammarName<BuilderApi.Input<BuilderApi.ExpectedOutput<BuilderApi.ExpectedErrors<BuilderApi.SlaveGrammars<CompositeLexerTestMethod>>>>>
        compositeLexerTest(String name) {
            return new CompositeLexerTestBuilder().test(name);
        }

    }

    @Test
    public void testAsSubType() throws Exception {
        class MySuite extends CompositeLexerTestBuilder {
            {

                test("LexerDelegatorInvokesDelegateRule")
                        .grammarName("M")
                        .input("abc")
                        .expectedOutput("S.A\n" +
                                        "[@0,0:0='a',<3>,1:0]\n" +
                                        "[@1,1:1='b',<1>,1:1]\n" +
                                        "[@2,2:2='c',<4>,1:2]\n" +
                                        "[@3,3:2='<EOF>',<-1>,1:3]\n")
                        .expectedErrors(null)
                        .slaveGrammars("S");

            }
        }
        class MyOtherSuite extends TestModuleBuilder {
            {
                compositeLexerTest("LexerDelegatorInvokesDelegateRule")
                        .grammarName("M")
                        .input("abc")
                        .expectedOutput("S.A\n" +
                                        "[@0,0:0='a',<3>,1:0]\n" +
                                        "[@1,1:1='b',<1>,1:1]\n" +
                                        "[@2,2:2='c',<4>,1:2]\n" +
                                        "[@3,3:2='<EOF>',<-1>,1:3]\n")
                        .expectedErrors(null)
                        .slaveGrammars("S");

            }

        }
    }

    @Test
    public void testFactory() throws Exception {
        class CompositeSuite extends FactoryImpl {
            BuilderApi.CompositeLexerTestBuilder test(String name) {
                return compositeLexerTest(name);
            }

            {
                test("LexerDelegatorInvokesDelegateRule")
                        .grammarName("M")
                        .input("abc")
                        .expectedOutput("S.A\n" +
                                        "[@0,0:0='a',<3>,1:0]\n" +
                                        "[@1,1:1='b',<1>,1:1]\n" +
                                        "[@2,2:2='c',<4>,1:2]\n" +
                                        "[@3,3:2='<EOF>',<-1>,1:3]\n")
                        .expectedErrors(null)
                        .slaveGrammars("S");
                compositeLexerTest("LexerDelegatorInvokesDelegateRule")
                        .grammarName("M")
                        .input("abc")
                        .expectedOutput("S.A\n" +
                                        "[@0,0:0='a',<3>,1:0]\n" +
                                        "[@1,1:1='b',<1>,1:1]\n" +
                                        "[@2,2:2='c',<4>,1:2]\n" +
                                        "[@3,3:2='<EOF>',<-1>,1:3]\n")
                        .expectedErrors(null)
                        .slaveGrammars("S");
            }


        }

    }
}