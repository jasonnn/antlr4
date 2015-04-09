package org.antlr.v4.test.impl;


import java.io.File;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jason on 3/26/15.
 */
public class InProcessTestDelegate extends DefaultTestDelegate {


    private static final Logger LOGGER = Logger.getLogger(InProcessTestDelegate.class.getName());

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
            LOGGER.log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    enum Singleton {
        ;
        public static final InProcessTestDelegate INSTANCE = new InProcessTestDelegate();
    }

    public static AntlrTestDelegate getInstace() {
        return Singleton.INSTANCE;
    }
}
