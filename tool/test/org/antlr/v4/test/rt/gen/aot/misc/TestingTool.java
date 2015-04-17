package org.antlr.v4.test.rt.gen.aot.misc;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.v4.Tool;
import org.antlr.v4.misc.Graph;
import org.antlr.v4.tool.BuildDependencyGenerator;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarAST;
import org.antlr.v4.tool.ast.GrammarRootAST;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 3/11/15.
 */
public
class TestingTool extends Tool {

  void setHasOutput() {
    haveOutputDir = true;
  }

  public
  void addInput(String file) {
    grammarFiles.add(file);
  }

  public final Map<String, Grammar> grammarMap = new HashMap<String, Grammar>();

  public final Map<String, ToolInput> grammarSources;

  public final List<JavaFileObject> generatedJavaSources;

  public
  TestingTool() {
    grammarEncoding = "UTF-8";
    grammarSources = new HashMap<String, ToolInput>();
    generatedJavaSources = new ArrayList<JavaFileObject>();
  }

  public
  TestingTool(Map<String, ToolInput> grammarSources, List<JavaFileObject> javaSources) {
    this.grammarSources = grammarSources;
    this.generatedJavaSources = javaSources;
    grammarEncoding = "UTF-8";
  }

  public
  void reset() {
    grammarMap.clear();
    grammarSources.clear();
    generatedJavaSources.clear();
  }


  public
  TestingTool addGrammarSource(String name, String code) {
    grammarSources.put(name, new ToolInput(name, code));
    return this;
  }


  public
  void run() {
    List<GrammarRootAST> sortedGrammars = mySortGrammarByTokenVocab();

    for (GrammarRootAST t : sortedGrammars) {
      final Grammar g = createGrammar(t);
      g.fileName = t.fileName;

      grammarMap.put(g.ast.getGrammarName(), g);
      if (gen_dependencies) {
        BuildDependencyGenerator dep = new BuildDependencyGenerator(this, g);
        System.out.println(dep.getDependencies().render());

      } else if (errMgr.getNumErrors() == 0) {
        process(g, true);
      }
    }
  }

  public
  List<GrammarRootAST> mySortGrammarByTokenVocab() {
//		System.out.println(fileNames);
    Graph<String> g = new Graph<String>();
    List<GrammarRootAST> roots = new ArrayList<GrammarRootAST>();
    for (ToolInput input : grammarSources.values()) {


      ANTLRStringStream in = new ANTLRStringStream(input.code);
      GrammarRootAST t = parse(input.name, in);

      if (t == null) continue; // came back as error node
      if (t.hasErrors) continue;
      roots.add(t);
      t.fileName = input.name;
      String grammarName = t.getChild(0).getText();

      GrammarAST tokenVocabNode = findOptionValueAST(t, "tokenVocab");
      // Make grammars depend on any tokenVocab options
      if (tokenVocabNode != null) {
        String vocabName = tokenVocabNode.getText();
        g.addEdge(grammarName, vocabName);
      }
      // add cycle to graph so we always process a grammar if no error
      // even if no dependency
      g.addEdge(grammarName, grammarName);

    }

    List<String> sortedGrammarNames = g.sort();
//		System.out.println("sortedGrammarNames="+sortedGrammarNames);

    List<GrammarRootAST> sortedRoots = new ArrayList<GrammarRootAST>();
    for (String grammarName : sortedGrammarNames) {
      for (GrammarRootAST root : roots) {
        if (root.getGrammarName().equals(grammarName)) {
          sortedRoots.add(root);
          break;
        }
      }
    }

    return sortedRoots;
  }

  Grammar load(ToolInput input) {
    GrammarRootAST ast = parse(input.name, new ANTLRStringStream(input.code));
    Grammar grammar = createGrammar(ast);
    grammar.fileName = input.name;
    return grammar;
  }

  ToolInput findInput(String name) {
    if (!name.endsWith(".g4")) name = name + ".g4";
    ToolInput input = grammarSources.get(name);
    if (input != null) return input;

    for (ToolInput toolInput : grammarSources.values()) {
      if (toolInput.name.endsWith(name)) return toolInput;
    }
    throw new NullPointerException("couldnt find input: " + name);
  }

  Grammar load(String name) {
    ToolInput input = findInput(name);
    assert input != null : "couldnt find source for: " + name;
    return load(input);
  }

  static
  String grammarDir(Grammar g) {
    String name = g.fileName;
    return name.substring(0, name.lastIndexOf(File.separatorChar));
  }

  public
  Grammar loadImportedGrammar(Grammar g, GrammarAST nameNode) throws IOException {
    Grammar gr = load(nameNode.getText());
    String dir = grammarDir(g);
    if (!gr.fileName.startsWith(dir)) {
      String name = gr.fileName;
      gr.fileName = dir + name;

    }
    return gr;
  }


  @Override
  public
  Writer getOutputFileWriter(Grammar g, String fileName) throws IOException {
    if (fileName.endsWith(".java")) {
      ToolGeneratedJavaSource gen = new ToolGeneratedJavaSource(g, fileName);
      generatedJavaSources.add(gen);
      return gen.openWriter();
    }
    return new StringWriter();
  }

  public
  void run(String grammarFileName) {
    Grammar g = load(grammarFileName);
    assert g != null : "problem creating grammar: " + grammarFileName;
    process(g, true);
  }
}
