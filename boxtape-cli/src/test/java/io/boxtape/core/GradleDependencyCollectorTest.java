package io.boxtape.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import io.boxtape.cli.configuration.LoadableBoxtapeConfig;
import io.boxtape.cli.core.GradleDependencyCollector;
import io.boxtape.cli.core.Project;
import io.boxtape.core.configuration.Configuration;
import io.boxtape.core.configuration.LoadableBoxtapeSettings;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GradleDependencyCollectorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    GradleDependencyCollector collector;

    @Mock
    LoadableBoxtapeConfig config;
    Project sampleProject;

    static File gradleHome;
    @BeforeClass
    public static void setupGradle() throws IOException, URISyntaxException, ZipException {
        File unzipDir = new File(FilenameUtils.concat(FileUtils.getTempDirectoryPath(), "gradle"));
        FileUtils.forceMkdir(unzipDir);
        FileUtils.forceDeleteOnExit(unzipDir);

        URL gradleZip = ClassLoader.getSystemResource("gradle-2.6-bin.zip");
        File gradleZipFile = new File(gradleZip.toURI());
        ZipFile zipFile = new ZipFile(gradleZipFile);
        zipFile.extractAll(unzipDir.getCanonicalPath());
        gradleHome = new File(FilenameUtils.concat(unzipDir.getPath(), "gradle-2.6/"));
        assertThat(gradleHome.exists(), is(true));
    }

    @Before
    public void setup() throws IOException, URISyntaxException {

        String projectDir = System.getProperty("user.dir");
        if (!projectDir.endsWith("boxtape-cli")) {
            projectDir = FilenameUtils.concat(projectDir, "boxtape-cli");
        }

        assertThat("No project found at " + projectDir, new File(projectDir).exists(), is(true));
        sampleProject = new Project(projectDir, new Configuration(), new LoadableBoxtapeSettings(null, config));
        collector = new GradleDependencyCollector(gradleHome);
    }

    @Test
    public void discoversDependencies() {
        List<LibraryArtifact> artifacts = collector.collect(sampleProject);
        assertThat(
            artifacts.stream().anyMatch(artifact -> artifact.name().equals("com.fasterxml.jackson.core:jackson-core:2.6.1")),
            is(true));
    }
}
