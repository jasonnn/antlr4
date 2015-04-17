package org.antlr.v4.test.rt.gen.aot.misc;

import org.antlr.v4.tool.DefaultToolListener;

import javax.tools.JavaFileObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 3/30/15.
 */
public class InMemoryTestContext {

//    public String stderrDuringParse;


    public final Map<String, ToolInput> toolInput = new HashMap<String, ToolInput>();
    public final List<JavaFileObject> javaSources = new ArrayList<JavaFileObject>();
    public final List<BytecodeFileObject> compiledFiles = new ArrayList<BytecodeFileObject>();


    public final TestingTool tool = new TestingTool(toolInput, javaSources);
    public final DefaultToolListener toolListener = new DefaultToolListener(tool);

    public final TestCompiler compiler = new TestCompiler(javaSources, compiledFiles);


    public void reset() {
        toolInput.clear();
        javaSources.clear();
        compiledFiles.clear();
    }


}
