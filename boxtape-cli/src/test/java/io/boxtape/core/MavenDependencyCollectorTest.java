package io.boxtape.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import io.boxtape.cli.configuration.LoadableBoxtapeConfig;
import io.boxtape.cli.core.MavenDependencyCollector;
import io.boxtape.cli.core.Project;
import io.boxtape.core.configuration.Configuration;
import io.boxtape.core.configuration.LoadableBoxtapeSettings;
import org.apache.maven.cli.MavenCli;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MavenDependencyCollectorTest {

    @Mock
    LoadableBoxtapeConfig config;
    MavenDependencyCollector collector;
    File sampleProjectHome;
    Project sampleProject;
    @Before
    public void setup() throws IOException, URISyntaxException {
        MavenCli cli = new MavenCli();
        collector = new MavenDependencyCollector(cli);
        sampleProjectHome = new File(ClassLoader.getSystemResource("pom.xml").toURI()).getParentFile();
        sampleProject = new Project(sampleProjectHome.getCanonicalPath() , new Configuration(), new LoadableBoxtapeSettings(null,config));

    }


    @Test
    public void givenSampleProject_shouldResolveDependencies() throws IOException {
        List<LibraryArtifact> dependencies = collector.collect(sampleProject);
        LibraryArtifact mySql = new LibraryArtifact("mysql","mysql-connector-java","5.1.34");
        assertThat(dependencies, not(empty()));
        assertThat(
            dependencies.stream().anyMatch(it -> it.name().equals(mySql.name())),
            is(true));
    }


}
