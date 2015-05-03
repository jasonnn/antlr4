package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.Files;
import org.antlr.v4.test.rt.gen.JUnitTestMethod;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by jason on 4/16/15.
 */
public
class CompileFromFSVisitor extends TestBuildingVisitor<Void, MyGenerator> {
  public static final CompileFromFSVisitor INSTANCE = new CompileFromFSVisitor();

  public static
  void visit(JUnitTestMethod testMethod, MyGenerator generator) {
    testMethod.accept(INSTANCE, generator);
  }

  @Override
  public
  Void visitTest(JUnitTestMethod test, MyGenerator ctx) {

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager manager = compiler.getStandardFileManager(ERROR_LISTENER,
                                                                      Locale.getDefault(),
                                                                      Charset.forName("UTF-8"));


    File srcDir = new File(ctx.cwd, "src");

    assert srcDir.exists();
    assert srcDir.isDirectory();


    Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjectsFromFiles(Files.findJavaFiles(srcDir));

    assert compilationUnits.iterator().hasNext();

    try {
      manager.setLocation(StandardLocation.SOURCE_PATH, Collections.singleton(srcDir));
      File bin = new File(ctx.cwd, "bin");
      boolean b = bin.mkdir();
      assert b;
      manager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(bin));
    } catch (IOException e) {
      e.printStackTrace();
    }

    JavaCompiler.CompilationTask task = compiler.getTask(new OutputStreamWriter(System.err),
                                                         manager,
                                                         ERROR_LISTENER,
                                                         Collections.<String>emptySet(),
                                                         Collections.<String>emptySet(),
                                                         compilationUnits);
    task.call();

    try {
      manager.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  static final DiagnosticListener<JavaFileObject> ERROR_LISTENER = new DiagnosticListener<JavaFileObject>() {
    @Override
    public
    void report(Diagnostic<? extends JavaFileObject> diagnostic) {
      if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
        System.err.println(diagnostic);
      }
    }
  };
}
