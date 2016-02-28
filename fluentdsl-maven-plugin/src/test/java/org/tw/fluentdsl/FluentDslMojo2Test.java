package org.tw.fluentdsl;

import java.io.File;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 *
 * @author toben
 */
public class FluentDslMojo2Test extends AbstractMojoTestCase {

//    public void testSomething()
//            throws Exception {
//        File pom = getTestFile("target/test-classes/maven/pom.xml");
//        assertNotNull(pom);
//        assertTrue(pom.exists());
//        
//        final MavenProject mvnProject = new MavenProject() ;
//        mvnProject.setFile( pom.getParentFile() ) ;
//
//        FluentDslMojo myMojo = (FluentDslMojo) lookupConfiguredMojo(mvnProject, "fluentdsl");
//        assertNotNull(myMojo);
//        myMojo.execute();
//    }
    
    public void testfluentDsl1()
            throws Exception {
        FluentDslMojo mojo = new FluentDslMojo();
        mojo.setInputDirectory(new File("target/test-classes/examples"));
        mojo.setIncludes("fluentdsl1.fdsl");
        mojo.setOutputDirectory(new File("target/generated-sources/fluentdsl1"));
        mojo.execute();
        
        File f = new File("target/generated-sources/fluentdsl1/Expr.java");
        assertTrue(f.exists());
        assertTrue(f.length()>40);
    }
    
    public void testfluentDsl2()
            throws Exception {
        FluentDslMojo mojo = new FluentDslMojo();
        mojo.setInputDirectory(new File("target/test-classes/examples"));
        mojo.setIncludes("fluentdsl2.fdsl");
        mojo.setOutputDirectory(new File("target/generated-sources/fluentdsl2"));
        mojo.execute();
        File f = new File("target/generated-sources/fluentdsl2/org/tw/dsl1/Expr.java");
        assertTrue(f.exists());
        assertTrue(f.length()>60);
    }

}
