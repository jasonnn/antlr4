package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Created by jason on 3/28/15.
 */
class ToolGeneratedJavaSource extends SimpleJavaFileObject {


  final ByteArrayOutputStream sourceBuffer = new ByteArrayOutputStream();
  final
  Charset charset;

  ToolGeneratedJavaSource(String name, Charset charset) {
    super(createURI(name), Kind.SOURCE);
    this.charset = charset;
  }

  static
  URI createURI(String name) {
    if (!name.endsWith(Kind.SOURCE.extension)) {
      name = name.replace('.', '/') + Kind.SOURCE.extension;
    }
    return URI.create("antlr:///" + name);
  }

  @Override
  public
  CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return new String(sourceBuffer.toByteArray(), charset);
  }

  @Override
  public
  OutputStream openOutputStream() throws IOException {
    return sourceBuffer;
  }

}
