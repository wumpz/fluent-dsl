package org.tw.fluentdsl.examples.simpleregexp;

import org.tw.fluentdsl.examples.regexp.Start;
import org.tw.fluentdsl.examples.regexp.StartOneOrMore;

/**
 *
 * @author toben
 */
public class SimpleRegexp implements Start {

    @Override
    public StartOneOrMore accept(String text) {
        return helper.accept(text);
    }

    @Override
    public StartOneOrMore OneOrMore(String text) {
        return helper.OneOrMore(text);
    }

    @Override
    public StartOneOrMore Optional(String text) {
        return helper.Optional(text);
    }

    private RegExpHelper helper = new RegExpHelper();
    
    class RegExpHelper implements StartOneOrMore {

        StringBuilder b = new StringBuilder();
        
        @Override
        public StartOneOrMore accept(String text) {
            b.append(text);
            return this;
        }

        @Override
        public StartOneOrMore OneOrMore(String text) {
            b.append("(").append(text).append(")+");
            return this;
        }

        @Override
        public StartOneOrMore Optional(String text) {
            b.append("(").append(text).append(")?");
            return this;
        }

        @Override
        public String build() {
            return b.toString();
        }
        
    }
    
}
