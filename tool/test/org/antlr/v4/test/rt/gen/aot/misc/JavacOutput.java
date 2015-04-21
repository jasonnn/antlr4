package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 3/28/15.
 */
public
class JavacOutput {
  public
  JavacOutput(boolean success,
              List<Diagnostic<JavaFileObject>> diagnostics,
              String extraCompilerMessages,
              List<BytecodeFileObject> compiledClasses,
              List<JavaFileObject> sources) {
    this.success = success;
    this.diagnostics = diagnostics;
    this.extraCompilerMessages = extraCompilerMessages;
    this.compiledClasses = compiledClasses;
  }

  public
  JavacOutput() {
  }

  public
  void log() {
    for (Diagnostic<JavaFileObject> diagnostic : diagnostics) {
      if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
        System.err.println(diagnostic);
      }
    }
    if (!extraCompilerMessages.isEmpty()) {
      System.err.println("extra messages: " + extraCompilerMessages);
    }
  }

  boolean success;
  List<Diagnostic<JavaFileObject>> diagnostics;
  String extraCompilerMessages;
  List<BytecodeFileObject> compiledClasses;
  List<JavaFileObject> sources;

  public
  Map<String, byte[]> mapCompiledClasses() {
    Map<String, byte[]> map = new HashMap<String, byte[]>(compiledClasses.size());
    for (BytecodeFileObject compiledFile : compiledClasses) {
      map.put(compiledFile.getName(), compiledFile.baos.toByteArray());
    }
    return map;
  }

  @Override
  public
  String toString() {
    return "JavacOutput{" +
           "success=" + success +
           ", diagnostics=" + diagnostics +
           ", extraCompilerMessages='" + extraCompilerMessages + '\'' +
           ", compiledClasses=" + compiledClasses +
           ", sources=" + sources +
           '}';
  }
}
