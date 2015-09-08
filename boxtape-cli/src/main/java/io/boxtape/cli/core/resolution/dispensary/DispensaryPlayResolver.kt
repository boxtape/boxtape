package io.boxtape.cli.core.resolution.dispensary

import com.google.common.collect.ArrayListMultimap
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import io.boxtape.asJson
import io.boxtape.cli.Loggers
import io.boxtape.cli.core.resolution.PlayRepository
import io.boxtape.cli.core.resolution.PlayResolver
import io.boxtape.core.LibraryArtifact
import io.boxtape.core.ansible.PlayProvider
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.Optional

@Component
public class DispensaryPlayResolver
@Autowired constructor(val config: DispenaryConfig, val repository: PlayRepository) : PlayResolver {

    val log = LogFactory.getLog(this.javaClass)
    val consoleLog = Loggers.BOXTAPE_CONSOLE

    override fun resolve(dependencies: Iterable<LibraryArtifact>): ArrayListMultimap<LibraryArtifact, PlayProvider> {
        val request = DispensaryLookupRequest(
            requirements = dependencies.map { it.name() }
        )
        log.info("Requesting to dispensary at ${config.url} : \n ${request.asJson()}")
        val resultSet = getLookupResult(request)
        val providersByArtifact: ArrayListMultimap<LibraryArtifact, PlayProvider> = ArrayListMultimap.create()
        if (resultSet.isPresent()) {
            val downloaded = repository.downloadMissing(resultSet.get().matches)

            downloaded.entries()
            downloaded.asMap().forEach { entry ->
                val libraryArtifact = dependencies.first { it.name().equals(entry.getKey()) }
                providersByArtifact.putAll(libraryArtifact, entry.getValue())
            }
        }
        return providersByArtifact
    }

    private fun getLookupResult(request: DispensaryLookupRequest): Optional<DispensaryLookupResultSet> {
        try {
            val response = Unirest.post("${config.url}/lookup")
                .header("Content-Type", "application/json")
                .body(request)
                .asObject(javaClass<DispensaryLookupResultSet>())
            val resultSet = response.getBody() // Mappers.jsonMapper.convertValue(json, javaClass<DispensaryLookupResultSet>() )
            return Optional.of(resultSet)
        } catch (e: UnirestException) {
            consoleLog.warn("It looks like the dispensary at ${config.url} is down.")
            consoleLog.warn("Attempts to connect returned the following error: ${e.getMessage()}")
            return Optional.empty()
        }
    }
}


