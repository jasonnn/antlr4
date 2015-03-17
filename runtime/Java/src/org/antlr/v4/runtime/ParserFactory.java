package org.antlr.v4.runtime;

/**
 * Created by jason on 3/16/15.
 */
public interface ParserFactory {
    Parser createParser(TokenStream tokenStream);
}
