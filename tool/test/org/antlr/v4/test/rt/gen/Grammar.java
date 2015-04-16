package org.antlr.v4.test.rt.gen;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Grammar {

	public String fileName;
	public String grammarName;
	public String[] lines;
	public ST template;

	/**
	 * @param fileName the name of the st file. don't add the  '.st' suffix.
	 *                 i.e. (for CompositeLexers) LexerDelegatorInvokesDelegateRule
	 * @param grammarName the name of the grammar contained in the st.
	 *                    i.e. for CompositeLexers/LexerDelegatorInvokesDelegateRule the grammar name would be 'M'
	 */
	public Grammar(String fileName, String grammarName) {
		this.fileName = fileName;
		this.grammarName = grammarName;
	}

	/**
	 * @param grammarDir the dir containing {@link Grammar#fileName}
	 * @throws Exception
	 */
	public void load(File grammarDir) throws Exception {
		assert grammarDir.exists();
		assert grammarDir.isDirectory();

		template = loadGrammar(grammarDir, fileName);
	}

	protected ST loadGrammar(File grammarDir, String grammarFileName) throws Exception {
		File file = new File(grammarDir, grammarFileName + ".st");

		InputStream input = new FileInputStream(file);
		try {
			byte[] data = new byte[(int)file.length()];
			int next = 0;
			while(input.available()>0) {
				int read = input.read(data, next, data.length - next);
				next += read;
			}
			String s = new String(data);
			return new ST(s);
		} finally {
			input.close();
		}
	}

	public void generate(STGroup group) {
		template.add("grammarName", grammarName);
		template.groupThatCreatedThisInstance = group; // so templates get interpreted
		lines = template.render().split("\n");
		for(int i=0;i<lines.length;i++)
			lines[i] = Generator.escape(lines[i]);
	}

}
