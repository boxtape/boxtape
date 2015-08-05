package io.boxtape.core.resolution;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import com.google.common.collect.Lists;
import io.boxtape.cli.configuration.BoxtapeSettingsProvider;
import io.boxtape.cli.core.resolution.PlayRepository;
import io.boxtape.core.configuration.BoxtapeSettings;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class PlayRepositoryTest {

    private BoxtapeSettings settings;

    @Before
    public void setup() throws URISyntaxException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("samplePlays");
        File recipeDir = new File(resource.toURI());
        assertThat(recipeDir.exists(), is(true));
        settings = new BoxtapeSettingsProvider(
            recipeDir.getPath()
        ).build();
    }

    @Test
    public void returnsRecipiesFromLocalRepo() {
        PlayRepository repository = new PlayRepository(Lists.newArrayList(),settings);
        assertThat(repository.getRecipes(), hasSize(1));
    }


}
