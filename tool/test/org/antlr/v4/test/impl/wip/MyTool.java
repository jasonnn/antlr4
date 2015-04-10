package org.antlr.v4.test.impl.wip;

import org.antlr.v4.Tool;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.Grammar;

import java.io.*;
import java.nio.charset.Charset;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jason on 4/9/15.
 */
class MyTool extends Tool {
    private static final Logger log = Logger.getLogger(MyTool.class.getName());

    public MyTool(String... args) {
        super(args);
    }

    protected String getEncoding() {
        return grammarEncoding != null ? grammarEncoding : Charset.defaultCharset().name();
    }

    protected OutputStream createOutputStream(Grammar g, String fileName) throws IOException {
        File outputDir = getOutputDirectory(g.fileName);
        if (!outputDir.exists()) outputDir.mkdirs();
        File outputFile = new File(outputDir, fileName);
        return new FileOutputStream(outputFile);
    }


    @Override
    public void error(ANTLRMessage msg) {
        super.error(msg);
        errors.map(msg.fileName, msg);
    }

    @Override
    public Writer getOutputFileWriter(Grammar g, String fileName) throws IOException {
        if (outputDirectory == null) return new StringWriter();

        OutputStream outputStream = createOutputStream(g, fileName);

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            DigestOutputStream dos = new DigestOutputStream(outputStream, digest);
            digestMap.put(fileName, dos);
            return new BufferedWriter(new OutputStreamWriter(dos, getEncoding()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    MultiMap<String, ANTLRMessage> errors = new MultiMap<String, ANTLRMessage>();
    final Map<String, DigestOutputStream> digestMap = new TreeMap<String, DigestOutputStream>();

    @Override
    public void processGrammarsOnCommandLine() {
        super.processGrammarsOnCommandLine();
        File hashesFile = new File(inputDirectory, "hashes"); //hashesFile(g);


        Map<String, byte[]> hashes = mapHashes();

        if (!hashesFile.exists()) {
            log.info("no hashes file exists... doing full codegen");
            writeHashFile(hashesFile, mapHashes());
        } else {
            Map<String, byte[]> origHashes = readHashFile(hashesFile);

            boolean uptodate = compareHashes(origHashes, hashes);
            log.info("hashes up to date? " + uptodate);
            if (!uptodate) {
                writeHashFile(hashesFile, hashes);
            }

        }
    }

    @Override
    public void processNonCombinedGrammar(Grammar g, boolean gencode) {
        super.processNonCombinedGrammar(g, gencode);
//        if (!gencode) return;
//
//        if (!errors.containsKey(g.fileName)) {
//            File hashesFile = hashesFile(g);
////
//            if (!hashesFile.exists()) {
//                log.info("no hashes file exists... doing full codegen");
//                new CodeGenPipeline(g).process();
//                writeHashFile(hashesFile, mapHashes());
//            } else {
//                Map<String, byte[]> origHashes = readHashFile(hashesFile);
//                new CodeGenPipeline(g).process();
//                Map<String, byte[]> newHashes = mapHashes();
//
//                boolean uptodate = compareHashes(origHashes, newHashes);
//                log.info("hashes up to date? " + uptodate);
//                if(!uptodate){
//                    writeHashFile(hashesFile,newHashes);
//                }
//
//            }
        //}


    }

    boolean compareHashes(Map<String, byte[]> origHashes, Map<String, byte[]> newHashes) {
        if (origHashes.size() != newHashes.size()) {
            log.log(Level.WARNING, "size chage: \n {0} \n {1}", new Object[]{origHashes.keySet(), newHashes.keySet()});
            return false;
        }

        Iterator<Map.Entry<String, byte[]>> origIter = origHashes.entrySet().iterator();
        Iterator<Map.Entry<String, byte[]>> newIter = newHashes.entrySet().iterator();
        while (origIter.hasNext()) {
            if (!newIter.hasNext()) return false;
            Map.Entry<String, byte[]> e0 = origIter.next();
            Map.Entry<String, byte[]> e1 = newIter.next();
            if (!e0.getKey().equals(e1.getKey())) {
                log.log(Level.WARNING, "file name diff: {0} -> {1}", new Object[]{e0.getKey(), e1.getKey()});
                return false;
            }
            if (!Arrays.equals(e0.getValue(), e1.getValue())) return false;
        }
        return true;

    }

    File hashesFile(Grammar g) {
        return new File(getOutputDirectory(g.fileName), "hashes");
    }

    static Map<String, byte[]> readHashFile(File theFile) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(theFile)));
            Object object = ois.readObject();
            ois.close();
            return (Map<String, byte[]>) object;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void writeHashFile(File theFile, Map<String, byte[]> map) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(theFile));
            oos.writeObject(map);
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Map<String, byte[]> mapHashes() {

        Map<String, byte[]> hashes = new TreeMap<String, byte[]>();
        for (Map.Entry<String, DigestOutputStream> entry : digestMap.entrySet()) {
            hashes.put(entry.getKey(), entry.getValue().getMessageDigest().digest());
        }
        return hashes;
    }


}
