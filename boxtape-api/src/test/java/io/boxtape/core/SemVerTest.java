package io.boxtape.core;

import com.github.zafarkhaja.semver.Version;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SemVerTest {

    @Test
    public void parsesWithTolerance() {
        assertThat(SemVer.INSTANCE$.parse("51"), is(Version.valueOf("51.0.0")));
        assertThat(SemVer.INSTANCE$.parse("51.1"), is(Version.valueOf("51.1.0")));
        assertThat(SemVer.INSTANCE$.parse("51.1.2"), is(Version.valueOf("51.1.2")));
        assertThat(SemVer.INSTANCE$.parse("51.1.2.3"), is(Version.valueOf("51.1.2")));
    }
}
