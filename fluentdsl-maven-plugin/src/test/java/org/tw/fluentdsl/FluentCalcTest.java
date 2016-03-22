package org.tw.fluentdsl;

import java.io.IOException;
import java.util.regex.Matcher;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
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
public class FluentCalcTest {

    public FluentCalcTest() {
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

    @Test
    public void testSomeMethod() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:b;");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
    }

    @Test
    public void testSomeMethod2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:b|c|d;");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 3);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertEquals(idef.getMethods().get(1).getName(), "c");
        assertEquals(idef.getMethods().get(2).getName(), "d");
    }

    @Test
    public void testFollower() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:b c;");
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "c");
    }

    @Test
    public void testSyntacticProblem() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:b (c | d);");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 2);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "c");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(1).getName(), "d");
    }
    
    @Test
    public void testSyntacticProblem2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:(c | d) b;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 2);
        assertEquals(idef.getMethods().get(0).getName(), "c");
        assertEquals(idef.getMethods().get(1).getName(), "d");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "b");
        assertEquals(idef.getMethods().get(1).getReturnType().getMethods().get(0).getName(), "b");
    }

    @Test
    public void testFollower2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("options { correctMethodNames=false; } a:b (c | b) d;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 3);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 2);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "c");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(1).getName(), "b");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().get(0).getName(), "d");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(1).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(1).getReturnType().getMethods().get(0).getName(), "d");
    }

    @Test
    public void testFollower3() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:(b c) | d;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 2);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "c");
        assertEquals(idef.getMethods().get(1).getName(), "d");
        assertNull(idef.getMethods().get(1).getReturnType());
    }

    @Test
    public void testInterface() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("c:x|y; a:#c;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertFalse(idef.isHidden());
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 2);
        assertEquals(idef.getMethods().get(0).getName(), "x");
        assertNull(idef.getMethods().get(0).getReturnType());
        assertEquals(idef.getMethods().get(1).getName(), "y");
        assertNull(idef.getMethods().get(1).getReturnType());
    }

    @Test
    public void testInterface2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("#a:b;");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertTrue(idef.isHidden());
    }

    @Test
    public void testInterface3() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number | (number #op number) | (number #bop number);#bop: and | or;#op: multi | div;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 7);
        Idef idef = calc.getIfaces().get("expr");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 3);
    }

    @Test
    public void testInterface4() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number op number;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 3);
        Idef idef = calc.getIfaces().get("expr");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "number");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "op");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().get(0).getName(), "number");
    }

    @Test
    public void testInterface5() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number | (number op number) | (number bop number);");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 5);
        Idef idef = calc.getIfaces().get("expr");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 3);
    }

    @Test
    public void testInterface6() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("expr: number 'NumberToOp' op 'OpToNumber' number;");
        assertEquals(calc.getIfaces().size(), 3);
        Idef idef = calc.getIfaces().get("expr");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "number");
        assertEquals(idef.getMethods().get(0).getReturnType().getName(), "NumberToOp");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getName(), "op");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getName(), "OpToNumber");
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().get(0).getName(), "number");

    }

    @Test
    public void testOptionsPackage() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("options { package='org.tw'; } a: b | c;");
        assertEquals(calc.getOptions().get("package"), "org.tw");
    }
    
    @Test
    public void testMethodSignature() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a: b(String param1);");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertEquals(idef.getMethods().get(0).getParams().toString(), "{param1=String}");
    }
    
    @Test
    public void testMethodSignatureWithReturnType() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a: b(String param1) returns String;");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertEquals(idef.getMethods().get(0).getParams().toString(), "{param1=String}");
        assertEquals(idef.getMethods().get(0).getSimpleReturnType(), "String");
    }
    
    @Test
    public void testMethodSignatureWithReturnType2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a: b returns String;");
        assertEquals(calc.getIfaces().size(), 1);
        Idef idef = calc.getIfaces().get("a");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertTrue(idef.getMethods().get(0).getParams().isEmpty());
        assertEquals(idef.getMethods().get(0).getSimpleReturnType(), "String");
    }
    
    @Test
    public void testMethodSignatureWithReturnType3() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a: d #c; #c: b returns String;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 3);
        Idef idef = calc.getIfaces().get("a");
        assertFalse(idef.isHidden());
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "d");
        assertTrue(idef.getMethods().get(0).getParams().isEmpty());
        assertEquals(idef.getMethods().get(0).getReturnType().getMethods().get(0).getSimpleReturnType(), "String");
    }
    
    @Test
    public void testMulti() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:b+ c;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 2);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(2, idef.getMethods().get(0).getReturnType().getMethods().size());
        assertEquals("b", idef.getMethods().get(0).getReturnType().getMethods().get(0).getName());
        assertEquals("c", idef.getMethods().get(0).getReturnType().getMethods().get(1).getName());
    }
    
    @Test
    public void testMulti2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("a:(b d)+ c;");
        new WriteInterfaces(calc);
        assertEquals(calc.getIfaces().size(), 4);
        Idef idef = calc.getIfaces().get("a");
        assertEquals(idef.getName(), "a");
        assertEquals(idef.getMethods().size(), 1);
        assertEquals(idef.getMethods().get(0).getName(), "b");
        assertNotNull(idef.getMethods().get(0).getReturnType());
        assertEquals(1, idef.getMethods().get(0).getReturnType().getMethods().size());
        assertEquals("d", idef.getMethods().get(0).getReturnType().getMethods().get(0).getName());
        assertEquals(2, idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().size());
        assertEquals("b", idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().get(0).getName());
        assertEquals("c", idef.getMethods().get(0).getReturnType().getMethods().get(0).getReturnType().getMethods().get(1).getName());      
    }
    
    @Test
    public void testNamePattern1() {
        String[][]  data = new String[][] {
            {"test","test"},
            {"test5","test"},
            {"test_6795","test_"},
        };
        
        for (String[] item : data) {
            Matcher m = FluentCalc.NAME_PATTERN.matcher(item[0]);
            assertTrue(m.find());
            assertEquals(m.group(1), item[1]);
        }   
    }
    
    @Test
    public void testMultiProblem1() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("select: column+ table+;");
        new WriteInterfaces(calc);
        assertEquals(3, calc.getIfaces().size());
        Idef i = calc.getIfaces().get("select1OneOrMoreOneOrMore");
        assertEquals(1, i.getMethods().size());
    }
    
    @Test
    public void testMultiProblem2() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("select: column+ table+ where;");
        new WriteInterfaces(calc);
        assertEquals(3, calc.getIfaces().size());
        Idef i = calc.getIfaces().get("select2OneOrMoreOneOrMore");
        assertEquals(2, i.getMethods().size());
    }
    
    @Test
    public void testSqlProblem1() throws IOException, FluentDslException {
        FluentCalc calc = new FluentCalc("#builder: build returns String;\n" +
                "select: column+ table+ (where | #builder) (orderby | #builder) #builder;");
        new WriteInterfaces(calc);
    }
}
