package org.antlr.v4.test.impl.wip;

import org.antlr.v4.Tool;
import org.antlr.v4.test.ErrorQueue;
import org.antlr.v4.test.impl.StreamVacuum;
import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.DefaultToolListener;

import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by jason on 4/9/15.
 */
public class CachingDelegate extends NewAntlrDelegate {

    @Override
    public Tool createTool(String... args) {
        return new MyTool(args);
    }

    @Override
    public boolean generateAndBuildRecognizer(String grammarFileName,
                                              String grammarStr,
                                              String parserName,
                                              String lexerName,
                                              boolean defaultListener,
                                              String... extraOptions) {
        ErrorQueue equeue =
                antlr(grammarFileName, grammarStr, defaultListener, extraOptions);

        if (!equeue.errors.isEmpty()) {
            return false;
        }

        List<String> files = new ArrayList<String>();
        if (lexerName != null) {
            files.add(lexerName + ".java");
        }
        if (parserName != null) {
            files.add(parserName + ".java");
            Set<String> optionsSet = new HashSet<String>(Arrays.asList(extraOptions));
            if (!optionsSet.contains("-no-listener")) {
                files.add(grammarFileName.substring(0, grammarFileName.lastIndexOf('.')) + "BaseListener.java");
            }
            if (optionsSet.contains("-visitor")) {
                files.add(grammarFileName.substring(0, grammarFileName.lastIndexOf('.')) + "BaseVisitor.java");
            }
        }
        return compile(files.toArray(new String[files.size()]));
    }

    @Override
    public ErrorQueue antlr(String grammarFileName, boolean defaultListener, String... extraOptions) {
        String[] opts = makeAntlrOptions(grammarFileName, extraOptions);
        Tool antlr = createTool(opts);
        ErrorQueue equeue = new ErrorQueue(antlr);
        antlr.addListener(equeue);
        if (defaultListener) {
            antlr.addListener(new DefaultToolListener(antlr));
        }
        antlr.processGrammarsOnCommandLine();

        if (!defaultListener && !equeue.errors.isEmpty()) {
            System.err.println("antlr reports errors from " + tmpdir);
            System.err.printf("\toptions: %s%n", Arrays.toString(opts));
            for (ANTLRMessage msg : equeue.errors) {
                System.err.printf("\t%s%n", msg);
            }
            System.err.println();
            // System.out.println("!!!\ngrammar:");
            // System.out.println(new String(Utils.readFile(tmpdir + "/" + grammarFileName)));
            // System.out.println("###");
        }
        if (!defaultListener && !equeue.warnings.isEmpty()) {
            System.err.printf("antlr reports warnings from %s%n\toptions: %s%n", tmpdir, Arrays.toString(opts));
            for (ANTLRMessage msg : equeue.warnings) {
                System.err.printf("\t%s%n", msg);
            }
            System.err.println();
        }

        return equeue;
    }

    @Override
    public String execClass(String className) {
        try {

            final Class<?> mainClass = loadCompiledClass(className);
            final Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            PipedInputStream stdoutIn = new PipedInputStream();
            PipedInputStream stderrIn = new PipedInputStream();
            PipedOutputStream stdoutOut = new PipedOutputStream(stdoutIn);
            PipedOutputStream stderrOut = new PipedOutputStream(stderrIn);
            StreamVacuum stdoutVacuum = new StreamVacuum(stdoutIn);
            StreamVacuum stderrVacuum = new StreamVacuum(stderrIn);

            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(stdoutOut));
            try {
                PrintStream originalErr = System.err;
                try {
                    System.setErr(new PrintStream(stderrOut));
                    stdoutVacuum.start();
                    stderrVacuum.start();
                    mainMethod.invoke(null, (Object) new String[]{new File(tmpdir, "input").getAbsolutePath()});
                } finally {
                    System.setErr(originalErr);
                }
            } finally {
                System.setOut(originalOut);
            }

            stdoutOut.close();
            stderrOut.close();
            stdoutVacuum.join();
            stderrVacuum.join();
            String output = stdoutVacuum.toString();
            if (stderrVacuum.toString().length() > 0) {
                this.stderrDuringParse = stderrVacuum.toString();
                System.err.println("exec stderrVacuum: " + stderrVacuum);
            }
            return output;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
