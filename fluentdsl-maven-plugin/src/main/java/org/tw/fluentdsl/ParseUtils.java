package org.tw.fluentdsl;

import java.io.IOException;
import java.io.InputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 *
 * @author toben
 */
public final class ParseUtils {
    public static FlibParser parser(InputStream source) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(source);
		FlibLexer lexer = new FlibLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FlibParser parser = new FlibParser(tokens);
        return parser;
    }
    
    private ParseUtils() {}
}
