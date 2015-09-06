package io.boxtape.core.ansible;

import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class AnsiblePlayTest {

    @Test
    public void buildsYamlCorrectly() {
        AnsiblePlay play = new AnsiblePlay("example of an apt task")
            .withTask("apt").withArg("name","\"{{ item }}\"").build()
            .withItems("git","gdebi");
        String yaml = play.asYaml();
        assertThat(yaml, notNullValue());
    }
}
