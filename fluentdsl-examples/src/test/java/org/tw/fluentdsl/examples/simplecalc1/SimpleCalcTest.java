package org.tw.fluentdsl.examples.simplecalc1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toben
 */
public class SimpleCalcTest {
    
    public SimpleCalcTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of number method, of class SimpleCalc.
     */
    @Test
    public void testPlus() {
        SimpleCalc instance = new SimpleCalc();
        assertEquals(5, instance.number(3).plus().number(2).equals());
    }
    
    @Test
    public void testMinus() {
        SimpleCalc instance = new SimpleCalc();
        assertEquals(1, instance.number(3).minus().number(2).equals());
    }
    
}
