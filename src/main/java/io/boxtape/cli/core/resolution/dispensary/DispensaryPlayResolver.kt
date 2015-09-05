package io.boxtape.cli.core.resolution.dispensary

import com.google.common.collect.ArrayListMultimap
import com.mashape.unirest.http.Unirest
import io.boxtape.asJson
import io.boxtape.cli.core.resolution.PlayRepository
import io.boxtape.cli.core.resolution.PlayResolver
import io.boxtape.core.LibraryArtifact
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.mappers.Mappers
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
public class DispensaryPlayResolver
@Autowired constructor(val config: DispenaryConfig, val repository:PlayRepository) : PlayResolver {

    val log = LogFactory.getLog(this.javaClass)

    override fun resolve(dependencies: Iterable<LibraryArtifact>): ArrayListMultimap<LibraryArtifact, PlayProvider> {
        val request = DispensaryLookupRequest(
            requirements = dependencies.map { it.name() }
        )
        log.info("Requesting to dispensary at ${config.url} : \n ${request.asJson()}")
        val response = Unirest.post("${config.url}/lookup")
            .header("Content-Type","application/json")
            .body(request)
//            .asJson()

            .asObject(javaClass<DispensaryLookupResultSet>())

//        val json = response.getBody()
        val resultSet = response.getBody() // Mappers.jsonMapper.convertValue(json, javaClass<DispensaryLookupResultSet>() )
        val downloaded = repository.downloadMissing(resultSet.matches)
        val providersByArtifact:ArrayListMultimap<LibraryArtifact,PlayProvider> = ArrayListMultimap.create()
        downloaded.entries()
        downloaded.asMap().forEach { entry ->
            val libraryArtifact = dependencies.first { it.name().equals(entry.getKey()) }
            providersByArtifact.putAll(libraryArtifact, entry.getValue())
        }
        return providersByArtifact
    }
}


