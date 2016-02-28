/*
 * #%L
 * fluentdsl-maven-plugin
 * %%
 * Copyright (C) 2016 fluentdsl
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
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
