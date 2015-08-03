package io.boxtape.core.configuration;

import java.util.List;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
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

    @Test
    public void whenRegisteringProperty_and_propertyContainsDefaultValue_that_itIsRegistered() {
        Configuration configuration = new Configuration();
        configuration.registerProperty("${dbName:MyDb}");
        assertThat(configuration.getValue("dbName"), is("MyDb"));
    }

    @Test
    public void whenRegisteringExistingProperty_and_newRegistrationDoesntHaveValue_that_valueIsNotOverridden() {
        Configuration configuration = new Configuration();
        configuration.registerPropertyWithDefault("dbName","myDb");
        configuration.registerPropertyWithoutValue("dbName");
        assertThat(configuration.getValue("dbName"), is("myDb"));
    }

    @Test
    public void canRegisterPropertyWithoutValue() {
        Configuration configuration = new Configuration();
        assertThat(configuration.hasProperty("dbName"), is(false));
        configuration.registerPropertyWithoutValue("dbName");
        assertThat(configuration.hasProperty("dbName"), is(true));
    }
}
