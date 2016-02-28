package org.tw.fluentdsl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 *
 * @author toben
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class FluentDslMojo extends AbstractMojo {

    @Parameter(defaultValue = "${basedir}/target/generated-sources/fluentdsl", property = "outputDir", required = false)
    protected File outputDirectory;

    @Parameter(defaultValue = "${basedir}/src/main/fluentdsl", property = "inputDir", required = false)
    protected File inputDirectory;

    @Parameter(property = "includes")
    protected String[] includes;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    //project.addCompileSourceRoot("<DIRECTORY-PATH-HERE>");
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(inputDirectory);
        if (includes == null || includes.length > 0) {
            scanner.setIncludes(includes);
        } else {
            scanner.setIncludes(new String[]{"**/*.fdsl"});
        }
        
        getLog().info("working with scanner " + scanner + " on directory " + scanner.getBasedir().getAbsolutePath());
        scanner.scan();

        for (String file : scanner.getIncludedFiles()) {
            getLog().info("working on file " + file);
            try {
                FluentCalc calc = new FluentCalc(new FileInputStream(new File(inputDirectory, file)));
                String p = calc.getOptions().get("package");
                final File outputDir;
                if (p!=null) {
                    outputDir = new File(outputDirectory, p.replace(".", "/"));
                } else {
                    outputDir = outputDirectory;
                }
                outputDir.mkdirs();
                new WriteInterfaces(calc) {
                    @Override
                    protected void writeInterfaceDown(String name, String source) {
                        try (FileOutputStream fos = new FileOutputStream(new File(outputDir, name + ".java"));) {
                            fos.write(source.getBytes());
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(FluentDslMojo.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(FluentDslMojo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
            } catch (Exception ex) {
                Logger.getLogger(FluentDslMojo.class.getName()).log(Level.SEVERE, null, ex);
                throw new MojoFailureException("problem generating interfaces ", ex);
            }
        }

        if (project != null) {
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        }
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setInputDirectory(File inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    public void setIncludes(String... includes) {
        this.includes = includes;
    }
}
