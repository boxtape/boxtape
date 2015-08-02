package io.boxtape.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DependencyTest {

    final Dependency dependency = new Dependency("mysql","mysql-connector-java","5.1.2");

    @Test
    public void matchesIfAllPropertiesMatch() {
        assertThat(dependency.matches(new Dependency("mysql","mysql-connector-java","5.1.2")), is(true));
        assertThat(dependency.matches(new Dependency("postgres","mysql-connector-java","5.1.2")), is(false));
        assertThat(dependency.matches(new Dependency("mysql","postgres-connector-java","5.1.2")), is(false));
    }

    @Test
    public void appliesVersionMatchingCorrectly() {
        assertThat(dependency.matches(new Dependency("mysql","mysql-connector-java",">5.0.0")), is(true));
        assertThat(dependency.matches(new Dependency("mysql","mysql-connector-java","<5.0.0")), is(false));
        assertThat(dependency.matches(new Dependency("mysql","mysql-connector-java","6.0.0")), is(false));
    }

    @Test
    public void matchesWithWildcardVersion() {
        assertThat(dependency.matches(new Dependency("mysql","mysql-connector-java","*")), is(true));
    }


}
