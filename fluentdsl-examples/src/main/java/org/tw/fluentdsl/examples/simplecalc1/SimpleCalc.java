package org.tw.fluentdsl.examples.simplecalc1;

/**
 *
 * @author toben
 */
public class SimpleCalc implements Expr {

    @Override
    public Expr1 number(final int a) {
        return new Expr1() {
            @Override
            public Expr2 plus() {
                return new Expr2() {
                    @Override
                    public Expr3 number(final int b) {
                        return new Expr3() {
                            @Override
                            public int equals() {
                                return a+b;
                            }
                        };
                    }
                };
            }

            @Override
            public Expr2 minus() {
                return new Expr2() {
                    @Override
                    public Expr3 number(final int b) {
                        return new Expr3() {
                            @Override
                            public int equals() {
                                return a-b;
                            }
                        };
                    }
                };
            }
        };
    }
    
}
