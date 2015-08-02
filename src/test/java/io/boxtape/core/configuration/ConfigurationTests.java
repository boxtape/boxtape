package io.boxtape.core.configuration;

import java.util.List;
import org.junit.Test;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

public class ConfigurationTests {

    @Test
    public void configValuesAreCorrectlySubstituted() {
        Configuration configuration = new Configuration();
        configuration.registerPropertyWithDefault("connectionString","jdbc:mysql://${serverIp}/test");
        configuration.registerPropertyWithDefault("serverIp", "192.168.0.1");

        List<String> strings = configuration.asStrings();
        assertThat(strings,hasItem("serverIp=192.168.0.1"));
        assertThat(strings,hasItem("connectionString=jdbc:mysql://192.168.0.1/test"));
    }
}
