package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * Created by jason on 3/28/15.
 */
public class StringJavaSource extends SimpleJavaFileObject {
    final String source;

    public StringJavaSource(String name, String source) {
        super(createURI(name), Kind.SOURCE);
        this.source = source;
    }

    @Override
    public CharSequence getCharContent(boolean b) throws IOException {
        return source;
    }

    static URI createURI(String name) {
        if (!name.endsWith(Kind.SOURCE.extension)) {
            name = name.replace('.', '/') + Kind.SOURCE.extension;
        }
        return URI.create("string:///" + name);
    }
}
