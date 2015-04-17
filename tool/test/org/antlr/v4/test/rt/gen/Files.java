package org.antlr.v4.test.rt.gen;

import java.io.*;
import java.util.*;

/**
 * Created by jason on 4/16/15.
 */
public
class Files {

  public static final File[] EMPTY_FILE_ARRAY = {};


  public static
  FileFilter endingWith(final String suffix) {
    return new FileFilter() {
      @Override
      public
      boolean accept(File file) {
        return file.getPath().endsWith(suffix);
      }
    };
  }

  public static
  FileFilter files(final FileFilter next) {
    return new FileFilter() {
      @Override
      public
      boolean accept(File file) {
        return file.isFile() && next.accept(file);
      }
    };
  }

  public static final FileFilter JAVA_FILES = files(endingWith(".java"));

  public static
  List<File> findChildren(File parent, FileFilter filter) {
    List<File> files = new ArrayList<File>();
    for (File file : children(parent)) {
      if (filter.accept(file)) files.add(file);
    }
    return files;
  }

  public static List<File> findJavaFiles(File dir){
    return findChildrenRecursively(dir,JAVA_FILES);
  }

  public static
  List<File> findChildrenRecursively(File root, FileFilter filter) {
    List<File> result = new ArrayList<File>();
    ArrayDeque<File> dq = new ArrayDeque<File>();
    dq.add(root);
    while (!dq.isEmpty()) {
      for (File file : children(dq.pop())) {
        if (file.isDirectory()) dq.addLast(file);
        if (filter.accept(file)) result.add(file);
      }
    }
    return result;
  }


  public static
  File[] children(File parent) {
    File[] result = parent.listFiles();
    return result == null ? EMPTY_FILE_ARRAY : result;
  }

  public static
  void deleteRecurisvely(File... roots) {
    for (File root : roots) {
      for (File file : children(root)) {
        if (file.isDirectory()) {
          deleteRecurisvely(children(file));
        }
        file.delete();
      }
      root.delete();
    }

  }

  public static
  List<File> recursiveFiles(File root) {
    List<File> result = new ArrayList<File>();
    ArrayDeque<File> dq = new ArrayDeque<File>();
    dq.add(root);
    while (!dq.isEmpty()) {
      for (File file : children(dq.pop())) {
        if (file.isDirectory()) {
          dq.addLast(file);
        } else {
          result.add(file);
        }
      }
    }
    return result;
  }


  static
  void writeLines(File file, String... lines) {
    assert file != null;
    assert lines != null;
    assert !file.isDirectory();
    Writer w = null;
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      w = bw;
      Iterator<String> it = Arrays.asList(lines).iterator();
      while (it.hasNext()) {
        bw.write(it.next());
        if (it.hasNext()) bw.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (w != null) {
        try {
          w.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }
}
