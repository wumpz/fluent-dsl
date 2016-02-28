package org.tw.fluentdsl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author toben
 */
public class WriteInterfacesTest {
    
    public WriteInterfacesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUpMethod() throws Exception {
    }

    @After
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of writeInterfaceDown method, of class WriteInterfaces.
     * @throws java.io.IOException
     * @throws org.tw.fluentdsl.FluentDslException
     */
    @Test
    public void testIface() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number | number op number | number bop number;");
        final Map<String, String> sources = new HashMap<>();
        new WriteInterfaces(calc) {
            @Override
            protected void writeInterfaceDown(String name, String source) {
                sources.put(name, source); 
                super.writeInterfaceDown(name, source);
            }
        };
        assertEquals(sources.size(), 5);
        assertTrue(sources.containsKey("Expr"));
    }
    
    @Test
    public void testIface2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number 'NumberToOp' op 'OpToNumber' number;");
        final Map<String, String> sources = new HashMap<>();
        new WriteInterfaces(calc) {
            @Override
            protected void writeInterfaceDown(String name, String source) {
                sources.put(name, source); 
                super.writeInterfaceDown(name, source);
            }
        };
        assertEquals(sources.size(), 3);
        assertTrue(sources.containsKey("Expr"));
        assertTrue(sources.containsKey("NumberToOp"));
        assertTrue(sources.containsKey("OpToNumber"));
    }
    
    @Test
    public void testIface3() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a: b returns String;");
        final Map<String, String> sources = new HashMap<>();
        new WriteInterfaces(calc) {
            @Override
            protected void writeInterfaceDown(String name, String source) {
                sources.put(name, source); 
                super.writeInterfaceDown(name, source);
            }
        };
        assertEquals(sources.size(), 1);
        
    }
    
}
