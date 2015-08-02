package io.boxtape.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.boxtape.core.configuration.BoxtapeSettings;
import io.boxtape.core.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.cli.MavenCli;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

public class MavenDependencyCollectorTest {

    MavenDependencyCollector collector;
    File sampleProjectHome;
    Project sampleProject;
    @Before
    public void setup() throws IOException {
        setPathToSampleProject();

        MavenCli cli = new MavenCli();
        collector = new MavenDependencyCollector(cli);

        sampleProject = new Project(sampleProjectHome.getCanonicalPath() , new Configuration(), new BoxtapeSettings());

    }

    /**
     * We don't know where the test will run from (especially in certiain CI environments)
     * so work out where sample-project is.
     */
    private void setPathToSampleProject() throws IOException {
        ArrayList<String> executionLocation = Lists.newArrayList(StringUtils.split(new File(".").getCanonicalPath(), "/"));
        List<String> pathsToProject = executionLocation.subList(0, executionLocation.indexOf("boxtape") + 1);
        pathsToProject.add("sample-project");
        sampleProjectHome = new File("/" + Joiner.on("/").join(pathsToProject));
        assertThat("Sample project not found at " + sampleProjectHome.getCanonicalPath(), sampleProjectHome.exists(), is(true));
    }

    @Test
    public void givenSampleProject_shouldResolveDependencies() throws IOException {
        List<Dependency> dependencies = collector.collect(sampleProject);
        Dependency mySql = new Dependency("mysql","mysql-connector-java","5.1.34");
        assertThat(dependencies, not(empty()));
        assertThat(dependencies, hasItem(mySql));
    }


}
