package io.boxtape.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DependencyTest {

    final

    @Test
    public void matchesIfAllPropertiesMatch() {
        CoordinateComponentProvider componentProvider = new CoordinateComponentProvider("mysql","mysql-connector-java","5.1.2");
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "mysql-connector-java", "5.1.2")), is(true));
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("postgres", "mysql-connector-java", "5.1.2")), is(false));
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "postgres-connector-java", "5.1.2")), is(false));
    }

    @Test
    public void appliesVersionMatchingCorrectly() {
        CoordinateComponentProvider componentProvider = new CoordinateComponentProvider("mysql","mysql-connector-java",">5.0");
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "mysql-connector-java", "5.1.2")), is(true));
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "mysql-connector-java", "4.0.0")), is(false));
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "mysql-connector-java", "6.0.0")), is(true));
    }

    @Test
    public void matchesWithWildcardVersion() {
        CoordinateComponentProvider componentProvider = new CoordinateComponentProvider("mysql","mysql-connector-java","*");
        assertThat(componentProvider.canProvideFor(new LibraryArtifact("mysql", "mysql-connector-java", "5.1.2")), is(true));
    }


}
