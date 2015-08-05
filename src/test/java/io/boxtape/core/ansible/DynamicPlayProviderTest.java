package io.boxtape.core.ansible;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.boxtape.cli.core.ansible.plays.DynamicPlayProvider;
import io.boxtape.core.Dependency;
import io.boxtape.core.Recipe;
import io.boxtape.core.configuration.Configuration;
import io.boxtape.core.configuration.VagrantSettings;
import io.boxtape.yaml.Mappers;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class DynamicPlayProviderTest {

    Dependency dependency = new Dependency("mysql","mysql-connector-java","5.10");
    Recipe recipe;
    DynamicPlayProvider playProvider;
    @Before
    public void setup() throws IOException {
        String yaml = IOUtils.toString(ClassLoader.getSystemResourceAsStream("example.yml"));
        ObjectMapper mapper = Mappers.Yaml.mapper();
        recipe = mapper.readValue(yaml, Recipe.class);
        playProvider = new DynamicPlayProvider(recipe);

    }
    @Test
    public void detectsPlaceholdersInRoles() {
        // example.yaml has nested ${} properties
        Configuration configuration = new Configuration();
        playProvider.provideRoles(dependency,configuration);

        assertThat(configuration.hasProperty("dbName"), is(true));
        assertThat(configuration.getValue("dbName"), is("boxtape_example"));
    }

    @Test
    public void registersDeclaredProperties() {
        Configuration configuration = new Configuration();
        playProvider.provideRoles(dependency, configuration);

        assertThat(configuration.getValue("spring.datasource.url"), is("jdbc:mysql://localhost:33060/boxtape_example"));
    }

    @Test
    public void registersPortForwarding() {
        Configuration configuration = new Configuration();
        playProvider.provideRoles(dependency, configuration);

        assertThat(configuration.getVagrantSettings().getForwardedPorts(), hasSize(1));
        VagrantSettings.ForwardedPort forwardedPort =
            configuration.getVagrantSettings().getForwardedPorts().get(0);
        assertThat(forwardedPort.getHost(), is("33060"));
        assertThat(forwardedPort.getGuest(), is("3306"));
    }
}
