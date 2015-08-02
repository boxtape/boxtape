package io.boxtape.cli.commands

import com.google.common.collect.ArrayListMultimap
import io.boxtape.asYaml
import io.boxtape.cli.Loggers
import io.boxtape.core.Dependency
import io.boxtape.core.MavenDependencyCollector
import io.boxtape.core.Project
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.ansible.Playbook
import io.boxtape.core.ansible.PlaybookBuilder
import io.boxtape.core.resolution.PlayResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Builds the various Ansible configuration files
 * to provision a box.
 *
 * Does not actually perform the configuration.
 */
Component
public class AnsibleCommand @Autowired constructor(
    val dependecyCollector : MavenDependencyCollector,
    val playResolver : PlayResolver
) : ShellCommand {

    override fun run(project: Project) {
        val dependencies = dependecyCollector.collect(project)
        val plays = playResolver.resolve(dependencies)
        logPlays(project, plays)
        val playbook = PlaybookBuilder(plays, project.config).build()

        writePlaybook(playbook, project)
        writeRequirements(playbook, project)
    }

    private fun logPlays(project: Project, plays: ArrayListMultimap<Dependency, PlayProvider>) {
        project.console.info("${plays.size()} plays were discovered")
        plays.asMap().forEach { entry ->
            entry.getValue().forEach { play ->
                val className = play.javaClass.getSimpleName()
                val packageName = play.javaClass.getCanonicalName()

                project.console.info("${entry.getKey().name()} matched to ${className}  (${packageName})")
            }
        }
    }

    private fun writePlaybook(playbook: Playbook, project: Project) {
        val playbookYaml = listOf(playbook).asYaml()
        project.write(".boxtape/playbook.yml", playbookYaml)
    }

    private fun writeRequirements(playbook: Playbook, project: Project) {
        val requirementsYaml = playbook.getRequirementsYaml()
        project.write(".boxtape/requirements.yml", requirementsYaml)
    }

    override fun name(): String {
        return "ansible"
    }

}
