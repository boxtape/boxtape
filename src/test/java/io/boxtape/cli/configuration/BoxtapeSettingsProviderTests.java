package io.boxtape.cli.configuration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import io.boxtape.core.configuration.BoxtapeSettings;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class BoxtapeSettingsProviderTests {

    @Test
    public void givenValidPathForAdditionalRecipes_that_theyAreIncluded() throws URISyntaxException {
        URL resource = ClassLoader.getSystemClassLoader().getResource("samplePlays");
        File recipeDir = new File(resource.toURI());
        assertThat(recipeDir.exists(), is(true));
        BoxtapeSettings settings = new BoxtapeSettingsProvider(
            recipeDir.getPath()
        ).build();

        assertThat(settings.getRecipePaths(), hasItem(recipeDir));
    }
}
