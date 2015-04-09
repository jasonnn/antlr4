package org.antlr.v4.test.impl;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

/**
 * Created by jason on 4/8/15.
 */
public class NewAntlrDelegate extends InProcessTestDelegate {
    static final PrintStream ORIG_OUT = System.out;
    static final PrintStream ORIG_ERR = System.err;

    String out;
    final ByteArrayOutputStream baos_out = new ByteArrayOutputStream();
    final ByteArrayOutputStream baos_err = new ByteArrayOutputStream();

    void beginCapture() {
        baos_out.reset();
        baos_err.reset();
        System.setOut(new PrintStream(baos_out, true));
        System.setErr(new PrintStream(baos_err, true));

    }

    void endCapture() {
        try {
            out = baos_out.toString("UTF-8");
            stderrDuringParse = baos_err.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (stderrDuringParse.length() > 0) {
            System.err.println(stderrDuringParse);
        }

    }

    static final String LEXER_TEST = "LexerTest";
    static final String PARSER_TEST = "ParserTest";

    static final String LEXER_TEST_FILE_NAME = LEXER_TEST + ".java";
    static final String PARSER_TEST_FILE_NAME = PARSER_TEST + ".java";

    @Override
    public String execLexer(String grammarFileName,
                            String grammarStr,
                            String lexerName,
                            String input,
                            boolean showDFA) {
        boolean success = generateAndBuildRecognizer(grammarFileName,
                                                     grammarStr,
                                                     null,
                                                     lexerName);
        assertTrue(success);
        writeLexerTestFile(lexerName, showDFA);
        compile(LEXER_TEST_FILE_NAME);

        GeneratedLexerTest test = createLexerTestInstance();

        test.showDFA = showDFA;
        test.input = input;

        beginCapture();
        try {
            test.testUnchecked();
        } finally {
            endCapture();
        }

        return out;
    }

    @Override
    public String execParser(String grammarFileName,
                             String grammarStr,
                             String parserName,
                             String lexerName,
                             String startRuleName,
                             String input,
                             boolean debug,
                             boolean profile) {
        boolean success = generateAndBuildRecognizer(grammarFileName,
                                                     grammarStr,
                                                     parserName,
                                                     lexerName,
                                                     "-visitor");
        assertTrue(success);

        this.stderrDuringParse = null;

        if (parserName == null) {
            writeLexerTestFile(lexerName, false);
        } else {
            writeParserTestFile(parserName,
                                lexerName,
                                startRuleName,
                                debug,
                                profile);
        }

        compile(PARSER_TEST_FILE_NAME);

        GeneratedParserTest test = createParserTestInstance();
        test.input = input;
        test.debug = debug;
        test.profile = profile;

        beginCapture();
        try {
            test.testUnchecked();
        } finally {
            endCapture();
        }


        return out;

    }

    @Override
    protected void writeParserTestFile(String parserName,
                                       String lexerName,
                                       String parserStartRuleName,
                                       boolean debug,
                                       boolean profile) {
        writeFile(tmpdir,
                  PARSER_TEST_FILE_NAME,
                  NewTestCodeGenerator.generateParserTest(lexerName, parserName, parserStartRuleName));
    }

    @Override
    protected void writeLexerTestFile(String lexerName, boolean showDFA) {
        writeFile(tmpdir, LEXER_TEST_FILE_NAME, NewTestCodeGenerator.generateLexerTest(lexerName));
    }

    GeneratedParserTest createParserTestInstance() {
        try {
            return loadCompiledClass(PARSER_TEST).asSubclass(GeneratedParserTest.class).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    GeneratedLexerTest createLexerTestInstance() {
        try {
            return loadCompiledClass(LEXER_TEST).asSubclass(GeneratedLexerTest.class).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
