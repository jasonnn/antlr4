package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by jason on 3/28/15.
 */
class ToolGeneratedJavaSource extends SimpleJavaFileObject {
    ToolGeneratedJavaSource(String name) {
        super(createURI(name), Kind.SOURCE);
       // this.generatedFrom = g;
    }

    static URI createURI(String name) {
        if (!name.endsWith(Kind.SOURCE.extension)) {
            name = name.replace('.', '/') + Kind.SOURCE.extension;
        }
        return URI.create("antlr:///" + name);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return sourceBuffer.toString("UTF-8");
    }

   // Grammar generatedFrom;

    ByteArrayOutputStream sourceBuffer = new ByteArrayOutputStream();

    @Override
    public OutputStream openOutputStream() throws IOException {
        return sourceBuffer;
    }
}
