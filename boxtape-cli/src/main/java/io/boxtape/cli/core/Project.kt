package io.boxtape.cli.core

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import io.boxtape.asYaml
import io.boxtape.cli.Loggers
import io.boxtape.core.ShellExecutionResult
import io.boxtape.core.configuration.LoadableBoxtapeSettings
import io.boxtape.core.configuration.Configuration
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.ExecuteException
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils
import org.apache.commons.logging.Log
import java.io.File

data class Project(Parameter(names = arrayOf("--project", "-p")) val projectPath: String? = SystemUtils.getUserDir().getCanonicalPath(),
                   val config: Configuration = Configuration(),
                   val settings: LoadableBoxtapeSettings
) {
    init {
        if (!projectHome().exists() || !projectHome().isDirectory()) {
            throw IllegalArgumentException("${projectPath} does not exist, or is not a directory")
        }
    }

    val console: Log = Loggers.BOXTAPE_CONSOLE

    fun projectHome(): File {
        return File(projectPath)
    }

    fun write(filename: String, content: String) {
        val fullFilename = if (filename.startsWith("/")) filename else FilenameUtils.concat(projectHome().canonicalPath, filename)
        val path = FilenameUtils.getFullPath(fullFilename)
        FileUtils.forceMkdir(File(path))
        File(fullFilename).writeText(content)
    }

    fun run(command: CommandLine, outputPrefix: String = ""): ShellExecutionResult {
        val executor = DefaultExecutor();
        val result = ShellExecutionResult(outputPrefix)
        executor.setWorkingDirectory(projectHome())
        executor.setStreamHandler(result.streamHandler)
        try {
            result.exitCode = executor.execute(command)
        } catch (e: ExecuteException) {
            result.exitCode = e.getExitValue()
        }
        return result
    }

    fun run(command: String, outputPrefix: String = ""): ShellExecutionResult = run(CommandLine.parse(command), outputPrefix)

    fun writeConfigurationFile() {
        write(settings.projectConfigFilePath, config.asStrings().joinToString("\n"))
    }

    fun writeVagrantSettings() {
        write(settings.vagrantSettingsPath, config.vagrantSettings.asYaml())
    }

    fun command(name: String): CommandLine {
        return CommandLine(name)
    }

    fun hasFile(path: String): Boolean {
        return File(FilenameUtils.concat(projectHome().canonicalPath, path)).exists()
    }


}

