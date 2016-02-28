package org.tw.fluentdsl;

import java.io.IOException;
import static junit.framework.TestCase.assertNotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author toben
 */
public class ParseUtilsTest {
    
    public ParseUtilsTest() {
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
     * Test of parse method, of class ParseUtils.
     */
    @Test
    public void testParse() throws IOException {
        FlibParser parser = ParseUtils.parser(ParseUtils.class.getResourceAsStream("testfile.txt"));
        assertNotNull(parser);
       
        ParseTree tree = parser.start();
        System.out.println(tree.toStringTree(parser)); // print tree as text
    }
    
}
