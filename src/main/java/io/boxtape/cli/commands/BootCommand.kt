package io.boxtape.cli.commands

import io.boxtape.core.Project
import org.apache.commons.exec.CommandLine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

Component
public class BootCommand
    Autowired constructor(val vagrantUp : VagrantUpCommand,
                          val ansible: AnsibleCommand)
    : ShellCommand {
    override fun run(project: Project) {
//        vagrantUp.run(project)
        ansible.run(project)

        val ip = "10.0.2.15"
        project.config.registerPropertyWithDefault("serverIp",ip)

        project.writeConfigurationFile()
        val cmd = project.command("mvn")
            .addArgument("spring-boot:run")
        project.run(cmd)
    }

    override fun name() = "boot"

}
