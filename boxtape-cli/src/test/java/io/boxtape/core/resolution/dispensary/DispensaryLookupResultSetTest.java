package io.boxtape.core.resolution.dispensary;

import java.io.IOException;
import java.util.Collection;
import io.boxtape.cli.core.resolution.dispensary.DispensaryLookupResultSet;
import io.boxtape.cli.core.resolution.dispensary.LookupResult;
import io.boxtape.mappers.Mappers;
import org.junit.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class DispensaryLookupResultSetTest {
    @Test
    public void canDeserialize() throws IOException {
        String json = "{\n" +
            "    \"matches\": {\n" +
            "        \"mysql:mysql-connector-java:5.1.34\": [\n" +
            "            {\n" +
            "                \"name\": \"boxtape/boxtape-core-plays/src/main/resources/plays/mysql/boxtape.yml/MySql:0.0.1\",\n" +
            "                \"url\": \"http://localhost/recipes/boxtape/boxtape-core-plays/src/main/resources/plays/mysql/boxtape.yml/MySql:0.0.1\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";
        DispensaryLookupResultSet resultSet = Mappers.jsonMapper.readValue(json, DispensaryLookupResultSet.class);
        Collection<LookupResult> lookupResults = resultSet.getMatches().get("mysql:mysql-connector-java:5.1.34");
        assertThat(lookupResults, hasSize(1));
    }
}
