package org.antlr.v4.test.rt.gen.aot.misc;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Created by jason on 3/28/15.
 */
class BytecodeFileObject extends SimpleJavaFileObject {
    public final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    protected BytecodeFileObject(String name) {
        super(URI.create("bytes:///" + name.replace('.', '/')), Kind.CLASS);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return baos;
    }

    public byte[] getBytes(){
        return baos.toByteArray();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BytecodeFileObject that = (BytecodeFileObject) o;

        return baos.equals(that.baos);

    }

    @Override
    public int hashCode() {
        return baos.hashCode();
    }


}
