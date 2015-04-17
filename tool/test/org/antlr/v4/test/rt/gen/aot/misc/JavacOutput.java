package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 3/28/15.
 */
public class JavacOutput {
    public JavacOutput(boolean success, List<Diagnostic<JavaFileObject>> diagnostics, String extraCompilerMessages, Map<String, byte[]> compiledClasses,List<JavaFileObject> sources) {
        this.success = success;
        this.diagnostics = diagnostics;
        this.extraCompilerMessages = extraCompilerMessages;
        this.compiledClasses = compiledClasses;
    }

    public JavacOutput() {
    }

    boolean success;
    List<Diagnostic<JavaFileObject>> diagnostics;
    String extraCompilerMessages;
    Map<String, byte[]> compiledClasses;
    List<JavaFileObject> sources;

    @Override
    public String toString() {
        return "JavacOutput{" +
                "success=" + success +
                ", diagnostics=" + diagnostics +
                ", extraCompilerMessages='" + extraCompilerMessages + '\'' +
                ", compiledClasses=" + compiledClasses +
                ", sources=" + sources +
                '}';
    }
}
