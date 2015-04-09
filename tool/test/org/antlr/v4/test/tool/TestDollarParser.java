package org.antlr.v4.test.tool;

import org.antlr.v4.test.AntlrTestcase;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

public class TestDollarParser extends AntlrTestcase {

	@Test
	public void testSimpleCall() throws Exception {
		String grammar = "grammar T;\n" +
	                  "a : ID  { System.out.println( $parser.getSourceName() ); }\n" +
	                  "  ;\n" +
	                  "ID : 'a'..'z'+ ;\n";
		String found = execParser("T.g4", grammar, "TParser", "TLexer", "a", "x", true);
		assertThat(found, containsString(getClass().getSimpleName()));
		assertThat(stderrDuringParse(),isEmptyOrNullString());
	}

}
