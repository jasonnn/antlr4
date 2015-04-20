package org.antlr.v4.test.rt.gen.aot;

import org.antlr.v4.test.rt.gen.*;

import java.io.*;
import java.util.Collection;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Logger;

/**
 * Created by jason on 4/14/15.
 */
public
class MyGenerator {
  static final
  Logger log = Logger.getLogger(MyGenerator.class.getName());


  Generator g;
  Collection<JUnitTestFile> testGroups;
  public JUnitTestFile currentFile;
  File allTestsDir;
  private File suiteDir;
  public File cwd;
  public File srcPkgDir;
  public String pkgName;


  void update(JUnitTestFile file) {
    File suiteDir = new File(allTestsDir, file.name);
    boolean b = suiteDir.mkdirs();
    assert b : suiteDir.getPath();
    this.suiteDir = suiteDir;
    this.currentFile = file;
  }

  void update(JUnitTestMethod test) {
    File cwd = new File(suiteDir, test.name);
    boolean b = cwd.mkdirs();
    assert b : cwd.getPath();
    this.cwd = cwd;
    // this.uri = cwd.toURI();
    File pkgDir = new File(cwd, "src/" + currentFile.name + "/Test" + test.name);
    b = pkgDir.mkdirs();
    assert b : pkgDir.getPath();
    srcPkgDir = pkgDir;
    pkgName = currentFile.name + ".Test" + test.name;

  }

  MyGenerator() throws Exception {
    g = Generator.javaTarget();
    File rootDir = allTestsDir = g.output = new File("/Users/jason/Desktop/tmp/tst");
    if (rootDir.exists()) Files.deleteRecurisvely(rootDir);
    boolean b = rootDir.mkdirs();
    assert b;

    testGroups = g.buildTests();
    assert testGroups != null;
    assert g.group != null;



    for (JUnitTestFile file : testGroups) {
      update(file);

      for (JUnitTestMethod unitTest : file.unitTests) {
        if (ConcreteParserTestMethod.class.isAssignableFrom(unitTest.getClass())) continue;
        update(unitTest);


        WriteInitialFilesPass.visit(unitTest, this);
        RunAntlrFromFSPass.visit(unitTest, this);
        WriteTestPass.visit(unitTest, this);
        CompileFromFSPass.visit(unitTest, this);

      }
    }

    jar();
  }


  void jar() throws IOException {
    File jarFile = new File(g.output, "tests.jar");
    Manifest manifest = new Manifest();
    JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(jarFile)), manifest);
    for (JUnitTestFile group : testGroups) {

      JarEntry groupDir = new JarEntry(group.name + '/'); //dirEntry(group.name);
      System.out.println("begin junittestfile " + groupDir.getName());
      out.putNextEntry(groupDir);
      out.closeEntry();

      for (JUnitTestMethod test : group.unitTests) {
        if (ConcreteParserTestMethod.class.isAssignableFrom(test.getClass())) continue;
        JarEntry testDir = new JarEntry(group.name + '/' + "Test" + test.name + '/');
        System.out.println("\tbegin test " + testDir);
        out.putNextEntry(testDir);
        out.closeEntry();


        File classesDir = Files.dir(g.output, group.name, test.name, "bin", group.name, "Test" + test.name);
        for (File file : Files.findChildren(classesDir, Files.CLASS_FILES)) {

          JarEntry entry = new JarEntry(group.name + '/' + "Test" + test.name + '/' + file.getName());
          System.out.println("\t\tbegin classfile: " + entry.getName());
          entry.setTime(file.lastModified());
          out.putNextEntry(entry);

          BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

          byte[] buffer = new byte[1024];
          while (true) {
            int count = in.read(buffer);
            if (count == -1)
              break;
            out.write(buffer, 0, count);
          }
          out.closeEntry();
          in.close();

        }

      }
    }
    out.close();

  }


  public static
  void main(String[] args) throws Exception {
    new MyGenerator();
  }
}
