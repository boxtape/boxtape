package io.boxtape.cli

import com.beust.jcommander.JCommander
import com.mashape.unirest.http.Unirest
import io.boxtape.cli.commands.ShellCommand
import io.boxtape.cli.configuration.BoxtapeSettingsProvider
import io.boxtape.cli.core.Project
import io.boxtape.cli.core.resolution.dispensary.DispenaryConfig
import io.boxtape.cli.io.UnirestJacksonMapper
import io.boxtape.core.configuration.LoadableBoxtapeSettings
import org.apache.maven.cli.MavenCli
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
public open class AppConfig {

    init {
        Unirest.setObjectMapper(UnirestJacksonMapper())
    }
    Autowired var environment:Environment? = null

    Bean open fun mavenCli() = MavenCli()
    Bean open fun settings(): LoadableBoxtapeSettings {
//        val recipePath = environment!!getProperty("recipePath")
        return BoxtapeSettingsProvider(
//            recipePath
        ).build()
    }

    Bean open fun dispensaryConfig(settings:LoadableBoxtapeSettings):DispenaryConfig {
        return DispenaryConfig(
            settings.dispensaryUrl(),
            settings.dispensaryUsername(),
            settings.dispensaryPassword()
        )

    }

    Bean open fun project(settings: LoadableBoxtapeSettings) = Project(settings = settings)

    Bean open fun jCommander(project: Project, settings: LoadableBoxtapeSettings, commandList: List<ShellCommand>): JCommander {
        val jcommander = JCommander(listOf(project,settings))
        commandList.forEach { jcommander.addCommand(it.name(), it) }
        return jcommander
    }

}
