package org.tw.fluentdsl.examples.simpleregexp;

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
public class SimpleRegexpTest {
    
    public SimpleRegexpTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    SimpleRegexp instance;
    
    @Before
    public void setUp() {
        instance = new SimpleRegexp();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of accept method, of class SimpleRegexp.
     */
    @Test
    public void testAccept() {
        assertEquals("test", instance.accept("test").build());
    }

    /**
     * Test of OneOrMore method, of class SimpleRegexp.
     */
    @Test
    public void testOneOrMore() {
        assertEquals("(test)+", instance.OneOrMore("test").build());
    }

    /**
     * Test of Optional method, of class SimpleRegexp.
     */
    @Test
    public void testOptional() {
        assertEquals("(test)?", instance.Optional("test").build());
    }
}
