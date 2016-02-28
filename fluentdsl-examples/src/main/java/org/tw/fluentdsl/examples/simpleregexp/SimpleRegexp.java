package org.tw.fluentdsl.examples.simpleregexp;

import org.tw.fluentdsl.examples.regexp.Start;
import org.tw.fluentdsl.examples.regexp.Start1OneOrMore;

/**
 *
 * @author toben
 */
public class SimpleRegexp implements Start {

    @Override
    public Start1OneOrMore accept(String text) {
        return helper.accept(text);
    }

    @Override
    public Start1OneOrMore OneOrMore(String text) {
        return helper.OneOrMore(text);
    }

    @Override
    public Start1OneOrMore Optional(String text) {
        return helper.Optional(text);
    }

    private RegExpHelper helper = new RegExpHelper();
    
    class RegExpHelper implements Start1OneOrMore {

        StringBuilder b = new StringBuilder();
        
        @Override
        public Start1OneOrMore accept(String text) {
            b.append(text);
            return this;
        }

        @Override
        public Start1OneOrMore OneOrMore(String text) {
            b.append("(").append(text).append(")+");
            return this;
        }

        @Override
        public Start1OneOrMore Optional(String text) {
            b.append("(").append(text).append(")?");
            return this;
        }

        @Override
        public String build() {
            return b.toString();
        }
        
    }
    
}
