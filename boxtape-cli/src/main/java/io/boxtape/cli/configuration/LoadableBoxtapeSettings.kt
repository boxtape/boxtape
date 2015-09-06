package io.boxtape.core.configuration

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import io.boxtape.cli.configuration.LoadableBoxtapeConfig
import org.apache.commons.io.FilenameUtils
import java.io.File
@Parameters(separators = "=")
open data public class LoadableBoxtapeSettings(
    Parameter(names = arrayOf("--recipePath", "-r")) val additionalRecipePath: String?,
    config: LoadableBoxtapeConfig
) : LoadableBoxtapeConfig by config {
    fun getDefaultVagrantFile(): String {
        // TODO : Make this configurable
        val vagrantFile = this.javaClass.getClassLoader().getResource("Vagrantfile").readText()
        return vagrantFile
    }

    open fun getRecipePaths():List<File> {
        return if (additionalRecipePath != null && File(additionalRecipePath).exists()) {
            getConfigPaths().plus(File(additionalRecipePath))
        } else {
            getConfigPaths()
        }
    }

    fun getConfigPaths(): List<File> {
        return listOf(
            File(FilenameUtils.concat(System.getProperty("user.home"), ".boxtape/")),
            File(FilenameUtils.concat(System.getProperty("user.dir"), ".boxtape/"))
        ).filter({ it.exists() })
    }

    /**
     * Primary recipe path.  Where content is downloaded
     * to
     */
    open var primaryRecipePath = FilenameUtils.concat("${System.getProperty("user.home")}",".boxtape/recipes")

    /**
     * The path to the config file that Boxtape will write
     * for the project when running.  Contains things like
     * generated usernames, connection strings, etc
     *
     * Relative to the project root
     */
    var projectConfigFilePath = ".boxtape/application.properties"

    /**
     * Relative to the project root
     */
    var vagrantSettingsPath = ".boxtape/vagrantSettings.yml"
}
