package org.antlr.v4.test.impl;


import org.junit.runner.Description;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

/**
 * Created by jason on 4/8/15.
 */
public class NewAntlrDelegate extends DefaultTestDelegate {

   private static String pkgName(Class<?> c) {
        String name = c.getPackage().getName();
        if (name.endsWith("rt.java")) return "rt";
        if (name.endsWith("tool")) return "tool";
        return "other";
    }


    @Override
    public void testWillStart(Description description) {

        if (AntlrTestSettings.CREATE_PER_TEST_DIRECTORIES) {

            String testDirectory = pkgName(description.getTestClass()) +
                                   File.separatorChar +
                                   description.getTestClass().getSimpleName() +
                                   File.separatorChar +
                                   description.getMethodName();

            tmpdir = new File(AntlrTestSettings.BASE_TEST_DIR, testDirectory).getAbsolutePath();

        } else {

            tmpdir = new File(AntlrTestSettings.BASE_TEST_DIR,
                              description.getTestClass().getSimpleName()).getAbsolutePath();


        }
        if (!AntlrTestSettings.PRESERVE_TEST_DIR && new File(tmpdir).exists()) {
            eraseGeneratedFiles();
        }

    }


    static final PrintStream ORIG_OUT = System.out;
    static final PrintStream ORIG_ERR = System.err;

    String out;
    final ByteArrayOutputStream baos_out = new ByteArrayOutputStream();
    final ByteArrayOutputStream baos_err = new ByteArrayOutputStream();

    void beginCapture() {
        out = stderrDuringParse = null;
        baos_out.reset();
        baos_err.reset();
        System.setOut(new PrintStream(baos_out, true));
        System.setErr(new PrintStream(baos_err, true));
    }

    void endCapture() {
        System.setOut(ORIG_OUT);
        System.setErr(ORIG_ERR);
        String err;
        try {
            out = baos_out.toString("UTF-8");
            err = stderrDuringParse = baos_err.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        if (err.length() > 0) {
            System.err.println(err);
        }
    }


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

        beginCapture();
        try {
            test.test(input);
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
        test.debug = debug;
        test.profile = profile;

        beginCapture();
        try {
            test.test(input);
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

    static final String LEXER_TEST = "LexerTest";
    static final String PARSER_TEST = "ParserTest";

    static final String LEXER_TEST_FILE_NAME = LEXER_TEST + ".java";
    static final String PARSER_TEST_FILE_NAME = PARSER_TEST + ".java";

    enum Singleton {
        ;
        public static final NewAntlrDelegate INSTANCE = new NewAntlrDelegate();
    }

    public static AntlrTestDelegate getInstace() {
        return Singleton.INSTANCE;
    }

}
