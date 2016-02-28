package org.tw.fluentdsl;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;

/**
 *
 * @author toben
 */
public class FluentDslErrorStrategy extends DefaultErrorStrategy {
    
    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw new RuntimeException(e);
    }

    @Override
    public Token recoverInline(Parser recognizer)
            throws RecognitionException {
        throw new RuntimeException(new InputMismatchException(recognizer));
    }

    /**
     * Make sure we don't attempt to recover from problems in subrules.
     * @param recognizer
     */
    @Override
    public void sync(Parser recognizer) {
    }
}
