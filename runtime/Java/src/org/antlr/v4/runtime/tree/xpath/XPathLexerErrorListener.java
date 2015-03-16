package org.antlr.v4.runtime.tree.xpath;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.IRecognizer;
import org.antlr.v4.runtime.RecognitionException;

public class XPathLexerErrorListener extends BaseErrorListener {
	@Override
	public void syntaxError(IRecognizer<?, ?> IRecognizer, Object offendingSymbol,
							int line, int charPositionInLine, String msg,
							RecognitionException e)
	{
	}
}
