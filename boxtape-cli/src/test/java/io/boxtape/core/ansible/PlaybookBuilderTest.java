package io.boxtape.core.ansible;

import com.google.common.collect.ArrayListMultimap;
import io.boxtape.core.ansible.plays.MySqlPlay;
import io.boxtape.core.configuration.Configuration;
import io.boxtape.core.LibraryArtifact;
import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class PlaybookBuilderTest {

    @Test
    public void buildsExpectedYaml() {
        ArrayListMultimap<LibraryArtifact, PlayProvider> providers = ArrayListMultimap.create();
        providers.put(new LibraryArtifact("mysql", "mysql-connector-java", "5.1.2"),
            new MySqlPlay());
        PlaybookBuilder builder = new PlaybookBuilder(
            providers, new Configuration()
        );

        String yaml = builder.asYaml();
        assertThat(yaml, notNullValue());
    }
}
