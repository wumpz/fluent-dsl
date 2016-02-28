package org.tw.fluentdsl.examples.simplecalc1;

/**
 *
 * @author toben
 */
public class SimpleCalc implements Expr {

    @Override
    public Expr2 number(final int a) {
        return new Expr2() {
            @Override
            public Expr4 plus() {
                return new Expr4() {
                    @Override
                    public Expr6 number(final int b) {
                        return new Expr6() {
                            @Override
                            public int equals() {
                                return a+b;
                            }
                        };
                    }
                };
            }

            @Override
            public Expr4 minus() {
                return new Expr4() {
                    @Override
                    public Expr6 number(final int b) {
                        return new Expr6() {
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
