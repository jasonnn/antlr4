package org.antlr.v4.test.rt.gen.aot.misc;

import org.hamcrest.Matchers;

import javax.tools.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.assertThat;

/**
 * Created by jason on 3/11/15.
 */
public class TestCompiler {

//    static final ThreadLocal<TestCompiler> INSTANCES = new ThreadLocal<TestCompiler>() {
//        @Override
//        protected TestCompiler initialValue() {
//            return new TestCompiler();
//        }
//    };
//
//    public static TestCompiler getInstace() {
//        return INSTANCES.get();
//    }

    //"-d", tmpdir, "-cp", tmpdir + pathSep + CLASSPATH
    static final List<String> DEFAULT_COMPILE_OPTIONS =
            Arrays.asList("-g", "-source", "1.6", "-target", "1.6", "-implicit:class", "-Xlint:-options");

    InMemoryFileManager fileManager;
    JavaCompiler compiler;
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

    List<String> options = new ArrayList<String>();

    List<JavaFileObject> sources;

    List<BytecodeFileObject> compiledFiles;


    public TestCompiler(List<JavaFileObject> javaSources, List<BytecodeFileObject> compiledFiles) {
        this.sources = javaSources;
        this.compiledFiles = compiledFiles;
        initCompiler();

    }

    void initCompiler() {
        JavaCompiler cmp = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager mgr = cmp.getStandardFileManager(diagnosticCollector, Locale.getDefault(), Charset.forName("UTF-8"));
        assertThat(diagnostics.getDiagnostics(), Matchers.empty());
        this.compiler = cmp;
        this.fileManager = new InMemoryFileManager(mgr, compiledFiles);
    }

    TestCompiler() {
        this(new ArrayList<JavaFileObject>(), new ArrayList<BytecodeFileObject>());
    }

    public TestCompiler reset() {
        if (!DEFAULT_COMPILE_OPTIONS.equals(options)) {
            options = new ArrayList<String>(DEFAULT_COMPILE_OPTIONS);
        }
        if (!diagnostics.getDiagnostics().isEmpty()) {
            diagnostics = new DiagnosticCollector<JavaFileObject>();
        }
        if (!compiledFiles.isEmpty()) {
            compiledFiles.clear();
        }


        try {
            fileManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initCompiler();
        return this;
    }

    public JavacOutput compile() {
        return compile(sources);
    }


    public JavacOutput compile(JavaFileObject... files) {
        return compile(Arrays.asList(files));
    }

    public JavacOutput compile(List<JavaFileObject> files) {
        Writer out = new StringWriter();
        Iterable<String> options = DEFAULT_COMPILE_OPTIONS;
        Iterable<String> classes = Collections.emptyList();
        JavaCompiler.CompilationTask task = compiler.getTask(out, fileManager, diagnostics, options, classes, files);

        boolean success = task.call();

        JavacOutput result = new JavacOutput();
        result.success = success;
        //noinspection unchecked
        result.diagnostics = new ArrayList(diagnostics.getDiagnostics());
        result.extraCompilerMessages = out.toString();

        Map<String, byte[]> compiledClasses = new HashMap<String, byte[]>(fileManager.compiledFiles.size());

        for (BytecodeFileObject compiledFile : fileManager.compiledFiles) {
            compiledClasses.put(compiledFile.getName(), compiledFile.baos.toByteArray());
        }

        result.compiledClasses = compiledClasses;
        result.sources = files;

        return result;
    }

    static class InMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        public final List<BytecodeFileObject> compiledFiles;// = new ArrayList<BytecodeFileObject>();

        protected InMemoryFileManager(StandardJavaFileManager fileManager, List<BytecodeFileObject> compiledFiles) {
            super(fileManager);
            this.compiledFiles = compiledFiles;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                BytecodeFileObject object = new BytecodeFileObject(className);
                compiledFiles.add(object);
                return object;
            }
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }

        @Override
        public String toString() {
            return "InMemoryFileManager{" +
                    "compiledFiles=" + compiledFiles +
                    '}';
        }
    }

}
