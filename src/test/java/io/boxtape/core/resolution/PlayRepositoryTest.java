package io.boxtape.core.resolution;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.boxtape.cli.configuration.BoxtapeSettingsProvider;
import io.boxtape.cli.core.resolution.FileDownloader;
import io.boxtape.cli.core.resolution.PlayRepository;
import io.boxtape.cli.core.resolution.dispensary.LookupResult;
import io.boxtape.core.configuration.LoadableBoxtapeSettings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayRepositoryTest {

    private LoadableBoxtapeSettings settings;

    @Mock
    public FileDownloader downloader;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private PlayRepository repository;

    @Before
    public void setup() throws URISyntaxException, IOException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("samplePlays");
        File recipeDir = new File(resource.toURI());
        assertThat(recipeDir.exists(), is(true));
        settings = new BoxtapeSettingsProvider(
            recipeDir.getPath()
        ).build();
        settings.setPrimaryRecipePath(temporaryFolder.getRoot().getCanonicalPath());
        repository = new PlayRepository(Lists.newArrayList(), settings, downloader);
    }

    @Test
    public void returnsRecipiesFromLocalRepo() {
        assertThat(repository.getRecipes(), hasSize(1));
    }

    @Test
    public void downloadsMissingFiles() throws IOException {
        when(downloader.downloadContent(any(URL.class))).thenReturn("I am the content");
        Multimap<String, LookupResult> searchResults = getSearchResults();
        repository.downloadMissing(searchResults);

        verify(downloader).downloadContent(eq(new URL("http://localhost:8080/recipes/boxtape/core-plays/mysql@0.0.1")));
        File expected = new File(FilenameUtils.concat(temporaryFolder.getRoot().getPath(), "boxtape/core-plays/mysql/0.0.1/boxtape.yml"));
        assertThat(expected.exists(), is(true));
        assertThat(FileUtils.readFileToString(expected), is("I am the content"));
    }

    @Test
    public void givenFileExists_that_itIsNotAttemptedToDownload() throws MalformedURLException {
        repository.downloadMissing(getSearchResults());
        verify(downloader, never()).downloadContent(eq(new URL("http://localhost:8080/recipes/boxtape/core-plays/mysql@0.0.1")));
    }

    @NotNull
    private Multimap<String, LookupResult> getSearchResults() {
        Multimap<String, LookupResult> searchResults = ArrayListMultimap.create();
        searchResults.put("mysql:mysql-java:5.10", new LookupResult("boxtape/core-plays/mysql@0.0.1", "http://localhost:8080/recipes/boxtape/core-plays/mysql@0.0.1"));
        return searchResults;
    }
}
