package org.antlr.v4.test.rt.gen;


import org.antlr.v4.Tool;
import org.antlr.v4.test.impl.wip.NewTestCodeGenerator;
import org.stringtemplate.v4.ST;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by jason on 4/14/15.
 */
public class MyGenerator {

    Generator g;
    Collection<JUnitTestFile> testFiles;

    MyGenerator() throws Exception {
        g = Generator.javaTarget();
        g.output = cwd = new File("/Users/jason/Desktop/tmp/tst");

        testFiles = g.buildTests();
        assert testFiles != null;
        assert g.group != null;
        run();
    }

    JUnitTestFile currentFile;

    private void run() throws Exception {
        for (JUnitTestFile testFile : testFiles) {
            currentFile = testFile;
            handleTestFile(testFile);
        }
    }

    private String generateTestCode(JUnitTestFile test) throws Exception {
        ST template = g.group.getInstanceOf("TestFile");
        template.add("file", test);
        return template.render();
    }


    File cwd;

    File dir(String path){
        File f = new File(cwd,path);
        f.mkdirs();
        return f;
    }
    File file(String path){
        return new File(cwd,path);
    }

    private void handleTestFile(JUnitTestFile testFile) {
        File baseDir = dir(testFile.name);

        for (JUnitTestMethod test : testFile.unitTests) {
           this.cwd = new File(baseDir, test.name);
            cwd.mkdir();



            if (test instanceof LexerTestMethod) {
                if (test instanceof CompositeLexerTestMethod) {
                    handleCompositeLexer((CompositeLexerTestMethod) test);
                } else {
                    handleLexer((LexerTestMethod) test);
                }
            } else if (test instanceof ParserTestMethod) {
                if (test instanceof CompositeParserTestMethod) {
                    handleCompositeParser((CompositeParserTestMethod) test);
                } else {
                    handleParser((ParserTestMethod) test);
                }
            } else if (test instanceof AbstractParserTestMethod) {
                handleAbstractParser((AbstractParserTestMethod) test);
            } else if (test instanceof ConcreteParserTestMethod) {
                handleConcreteParser((ConcreteParserTestMethod) test);
            } else {
                System.err.println("????");
            }




            //File antlrDir = new File(testBaseDir, "antlr4"); antlrDir.mkdir();

//      if (test.grammar.template != null) {
//        String grammarTxt = test.grammar.template.render();
//        writeText(new File(cwd, test.grammar.fileName + ".g4"), grammarTxt);
//      }

            if (test.input != null) {
                writeText(file("input"), test.input);
            }

            if (test.expectedOutput != null) {
                writeText(file("output"), test.expectedOutput);
            }

            if (test.expectedErrors != null) {
                writeText(file("errors"), test.expectedErrors);
            }


        }

    }

    private void handleConcreteParser(ConcreteParserTestMethod test) {

    }

    private void handleAbstractParser(AbstractParserTestMethod test) {

    }

    private void handleParser(ParserTestMethod test) {
        writeGrammar(test.grammar);
        runTool(test);

        File tst = new File(cwd, "src/" + currentFile.name + "/" + test.name+"/ParserTest.java");
        String pkg = currentFile.name + '.' + test.name;
        String lexerJavaName = test.grammar.grammarName + "Lexer";
        String parserJavaName = test.grammar.grammarName + "Parser";
        writeText(tst, NewTestCodeGenerator.generateParserTest(pkg,lexerJavaName,parserJavaName,test.startRule));
        compile(test);

    }

    private void handleCompositeParser(CompositeParserTestMethod test) {
        writeGrammar(test.grammar);
        for (Grammar grammar : test.slaveGrammars) {
            writeGrammar(grammar);
        }
        runTool(test);
        File tst = new File(cwd, "src/" + currentFile.name + "/" + test.name+"/ParserTest.java");
        String pkg = currentFile.name + '.' + test.name;
        String lexerJavaName = test.grammar.grammarName + "Lexer";
        String parserJavaName = test.grammar.grammarName + "Parser";
        writeText(tst, NewTestCodeGenerator.generateParserTest(pkg, lexerJavaName, parserJavaName, test.startRule));
        compile(test);


    }

    private void handleLexer(LexerTestMethod test) {
        writeGrammar(test.grammar);
        runTool(test);

        File tst = new File(cwd, "src/" + currentFile.name + "/" + test.name+"/LexerTest.java");
        String lexerJavaName = test.grammar.grammarName;// + "Lexer";
        writeText(tst, NewTestCodeGenerator.generateLexerTest(currentFile.name + '.' + test.name, test.grammar.grammarName));

        compile(test);
    }

    private void handleCompositeLexer(CompositeLexerTestMethod test) {
        writeGrammar(test.grammar);
        for (Grammar grammar : test.slaveGrammars) {
            writeGrammar(grammar);
        }
        runTool(test);
        File tst = new File(cwd, "src/" + currentFile.name + "/" + test.name+"/LexerTest.java");
        //tst.mkdirs();
        String lexerJavaName = test.grammar.grammarName ;//+ "Lexer";

        writeText(tst, NewTestCodeGenerator.generateLexerTest(currentFile.name + '.' + test.name, lexerJavaName));
        compile(test);

    }

    static final DiagnosticListener<JavaFileObject> listener=new DiagnosticListener<JavaFileObject>() {
        @Override
        public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
            System.out.println(diagnostic);
        }
    };

    void compile(JUnitTestMethod m){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager manager = compiler.getStandardFileManager(listener, Locale.getDefault(), Charset.forName("UTF-8"));

        File[] files = new File(cwd, "src/" + currentFile.name + "/" + m.name).listFiles();
        assert files!=null;
        List<JavaFileObject> fileList = new ArrayList<JavaFileObject>();
        for (File file : files) {
            if(file.getName().endsWith(".java")){
                fileList.add(manager.getJavaFileObjects(file).iterator().next());
            }
        }

        try {
            manager.setLocation(StandardLocation.SOURCE_PATH, Collections.singleton(new File(cwd,"src")));
            File bin = new File(cwd,"bin");
            bin.mkdir();
            manager.setLocation(StandardLocation.CLASS_OUTPUT,Collections.singleton(bin));
        } catch (IOException e) {
            e.printStackTrace();
        }


        JavaCompiler.CompilationTask task = compiler.getTask(new OutputStreamWriter(System.err), manager, listener, Collections.<String>emptySet(), Collections.<String>emptySet(), fileList);
        task.call();

        try {
            manager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void runTool(JUnitTestMethod m) {

        class MyTool extends Tool {
            void setHasOutput(){
                haveOutputDir=true;
            }

            public void addInput(String file){
                grammarFiles.add(file);
            }
        }


        MyTool tool = new MyTool();
        tool.gen_visitor = true;
        tool.gen_listener = true;

        tool.inputDirectory=cwd.getAbsoluteFile();
        tool.libDirectory=cwd.getAbsolutePath();
        tool.genPackage = currentFile.name + '.' + m.name;

        File pkgDir = new File(cwd, "src/" + currentFile.name + "/" + m.name);
        pkgDir.mkdirs();

        tool.outputDirectory=pkgDir.getAbsolutePath();
        tool.setHasOutput();

        tool.addInput(new File(cwd,m.grammar.grammarName + ".g4").getAbsolutePath());

        tool.processGrammarsOnCommandLine();


    }

    void writeGrammar(Grammar grammar) {
        writeText(new File(cwd, grammar.grammarName + ".g4"), grammar.template.render());
    }


    static void writeText(File file, String text) {
        assert file != null;
        assert text != null;
        try {

            FileWriter fw = new FileWriter(file);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        new MyGenerator();
    }
}
