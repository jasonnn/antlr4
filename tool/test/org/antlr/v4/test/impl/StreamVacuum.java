package org.antlr.v4.test.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by jason on 3/24/15.
 */
public class StreamVacuum extends Thread {
    StringBuilder buf = new StringBuilder();
    Reader in;

    public StreamVacuum(InputStream in) {
        this.in = new InputStreamReader(in);
    }

    @Override
    public void run() {
        try {
            int next;
            while ((next=in.read())!=-1){
                buf.append((char)next);
            }
        } catch (IOException ioe) {
            System.err.println("can't read output from process");
        }
    }

    @Override
    public String toString() {
        return buf.toString();
    }
}