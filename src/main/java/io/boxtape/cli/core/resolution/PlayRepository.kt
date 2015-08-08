package io.boxtape.cli.core.resolution

import io.boxtape.cli.core.ansible.plays.DynamicPlayProvider
import io.boxtape.core.Recipe
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.configuration.BoxtapeSettings
import io.boxtape.yaml.Mappers
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.properties.Delegates

Component
public class PlayRepository @Autowired constructor(private val providers: List<PlayProvider>, val settings: BoxtapeSettings) {
    val log = LogFactory.getLog(this.javaClass)

    private val recipePaths: List<Path> by Delegates.lazy { discoverLocalRecipes() }
    val recipes: List<Recipe> by Delegates.lazy {
        recipePaths.map { path ->
            Mappers.Yaml.mapper().readValue(path.toFile(), javaClass<Recipe>())
        }
    }

    val dynamicPlays: List<PlayProvider> by Delegates.lazy {
        recipes.map { DynamicPlayProvider(it) }
    }

    val playProviders: List<PlayProvider> by Delegates.lazy {
        providers.plus(dynamicPlays)
    }

    private fun discoverLocalRecipes(): List<Path> {
        val matches: MutableList<Path> = arrayListOf();
        val visitor = object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (file.getFileName().toString().equals("boxtape.yml")) {
                    matches.add(file)
                    log.debug("Discovered local recipe at " + file.toFile().getCanonicalPath())
                }
                return FileVisitResult.CONTINUE
            }
        }
        settings.getRecipePaths().forEach { configPath ->
            log.info("Scanning ${configPath.toString()} for recipes")
            Files.walkFileTree(Paths.get(configPath.toURI()), visitor)
        }
        log.info("Discovered " + matches.size() + " local recipes")
        return matches.toList()
    }
}
