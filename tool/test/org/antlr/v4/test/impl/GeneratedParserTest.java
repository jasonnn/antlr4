package org.antlr.v4.test.impl;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ProfilingATNSimulator;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Arrays;

/**
 * Created by jason on 3/23/15.
 */
public abstract class GeneratedParserTest {

    public String input = null;

    public boolean debug = false;
    public boolean profile = false;


    protected abstract Lexer createLexer(CharStream input);

    protected abstract Parser createParser(TokenStream tokens);

    protected abstract ParserRuleContext callStartRule(Parser parser);


    ProfilingATNSimulator profiler;


    private void nullCheck() {
        assert input != null : "input was null!";
    }

    public void testUnchecked() {
        try {
            test();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test() throws Exception {
        nullCheck();

        CharStream stream = new ANTLRInputStream(input);
        Lexer lexer = createLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Parser parser = createParser(tokens);

        if (debug) parser.addErrorListener(new DiagnosticErrorListener());

        parser.setBuildParseTree(true);

        if (profile) {
            profiler = new ProfilingATNSimulator(parser);
            parser.setInterpreter(profiler);
        }

        ParserRuleContext tree = callStartRule(parser);

        if(profile){
            System.out.println(Arrays.toString(profiler.getDecisionInfo()));
        }
        ParseTreeWalker.DEFAULT.walk(new TreeShapeListener(), tree);
    }
}
