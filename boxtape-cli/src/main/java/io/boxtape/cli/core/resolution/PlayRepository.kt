package io.boxtape.cli.core.resolution

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import io.boxtape.cli.core.ansible.plays.DynamicPlayProvider
import io.boxtape.cli.core.resolution.dispensary.LookupResult
import io.boxtape.core.Recipe
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.configuration.LoadableBoxtapeSettings
import io.boxtape.mappers.Mappers
import org.apache.commons.io.FileUtils
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.nio.file.Path
import kotlin.properties.Delegates

Component
public class PlayRepository @Autowired constructor(
    private val providers: List<PlayProvider>,
    val settings: LoadableBoxtapeSettings,
    private val downloader: FileDownloader
) {
    val log = LogFactory.getLog(this.javaClass)

    private val recipePaths: List<Path> by Delegates.lazy { discoverLocalRecipes() }
    val recipes: List<Recipe> by Delegates.lazy {
        recipePaths.map { path -> readPathToRecipe(path) }
            .filterNotNull()
    }

    private fun readPathToRecipe(path: Path): Recipe? {
        try {
            return Mappers.yamlMapper.readValue(path.toFile(), javaClass<Recipe>())
        } catch (e: Exception) {
            log.error("${path.toFile().getPath()} threw an error whilst parsing.  Ignoring. " + e.getMessage())
            return null;
        }
    }

    val dynamicPlays: List<PlayProvider> by Delegates.lazy {
        recipes.map { DynamicPlayProvider(it) }
    }

    val playProviders: List<PlayProvider> by Delegates.lazy {
        providers.plus(dynamicPlays)
    }

    private fun discoverLocalRecipes(): List<Path> {
        val discovered: MutableList<Path> = arrayListOf()
        settings.getRecipePaths()
            .forEach {
                it.walkBottomUp()
                    .filter { it.isDirectory() || it.name == "boxtape.yml" }
                    .toList()
                    .filter { it.name == "boxtape.yml" }
                    .forEach { discovered.add(it.toPath()) }
            }
        return discovered.toList()
    }

    fun downloadMissing(results: Multimap<String, LookupResult>): Multimap<String, PlayProvider> {

        val resultsNotPresentLocally = Multimaps.filterValues(results, { entry ->
            !entry.toBoxtapeFile(settings.primaryRecipePath).exists()
        })
        val downloadedByMatchName: Multimap<String,PlayProvider> = ArrayListMultimap.create()
        resultsNotPresentLocally.entries().forEach {
            val downloaded = downloadAndConvert(it.getValue())
            if (downloaded != null) {
                downloadedByMatchName.put(it.getKey(),downloaded)
            }
        }
        return downloadedByMatchName;
//        return Multimaps.transformValues(resultsNotPresentLocally, { downloadAndConvert(it) })
    }

    private fun downloadAndConvert(lookupResult: LookupResult): DynamicPlayProvider? {
        return sequenceOf(lookupResult)
            .map { lookupResult -> downloadToFile(lookupResult) }
            .map { file -> readPathToRecipe(file.toPath()) }
            .filterNotNull()
            .map { recipe -> DynamicPlayProvider(recipe) }
            .firstOrNull()
    }

    private fun downloadToFile(it: LookupResult): File {
        val url = URL(it.url)
        val targetPath = it.toBoxtapeFilePath(settings.primaryRecipePath)
        log.info("Downloading ${url.toString()} to ${targetPath}")
        val content = downloader.downloadContent(url)
        val targetFile = File(targetPath)
        FileUtils.forceMkdir(targetFile.parent)
        FileUtils.writeStringToFile(targetFile, content)
        return targetFile
    }
}
