package io.boxtape.cli

import com.beust.jcommander.JCommander
import io.boxtape.cli.commands.ShellCommand
import io.boxtape.cli.configuration.BoxtapeSettingsProvider
import io.boxtape.cli.core.Project
import io.boxtape.core.configuration.BoxtapeSettings
import org.apache.maven.cli.MavenCli
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
public open class AppConfig {

    Autowired var environment:Environment? = null

    Bean open fun mavenCli() = MavenCli()
    Bean open fun settings():BoxtapeSettings {
//        val recipePath = environment!!getProperty("recipePath")
        return BoxtapeSettingsProvider(
//            recipePath
        ).build()
    }

    Bean open fun project(settings: BoxtapeSettings) = Project(settings = settings)

    Bean open fun jCommander(project: Project, settings:BoxtapeSettings, commandList: List<ShellCommand>): JCommander {
        val jcommander = JCommander(listOf(project,settings))
        commandList.forEach { jcommander.addCommand(it.name(), it) }
        return jcommander
    }

}
