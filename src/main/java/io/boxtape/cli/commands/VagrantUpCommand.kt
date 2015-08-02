package io.boxtape.cli.commands

import io.boxtape.core.Project
import org.apache.commons.exec.CommandLine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Starts and provisions a vagrant box.
 * Defers to the Ansible command to build the playbooks,
 * then installs requirements.yaml, and finally provisions the vagrant box.
 */
Component
public class VagrantUpCommand @Autowired constructor(
    private val ansible: AnsibleCommand
) : ShellCommand {
    override fun name(): String = "up"

    override fun run(project: Project) {
        writeVagrantFile(project)

        // The ansible task may change the vagrant settings,
        // so needs to run first
        ansible.run(project)

        project.writeVagrantSettings()
        project.writeConfigurationFile()

        project.run("ansible-galaxy install -f -r .boxtape/requirements.yml")

        val result = project.run("vagrant up")
        if (result.containedMessage("VirtualBox VM is already running.")) {
            project.console.info("Vagrant box is already running, provisioning instead")
            project.run("vagrant provision")
        }
    }

    private fun discoverGuestIp(project: Project): String {
        // http://stackoverflow.com/questions/14870900/how-to-find-vagrant-ip
        val sshString = "ip address show eth0 | grep 'inet ' | sed -e 's/^.*inet //' -e 's/\\/.*$//'"
        // todo : write an error log
        val cmd = CommandLine("vagrant").addArgument("ssh").addArgument("-c").addArgument(sshString,false)
        val result = project.run(cmd)
        result.assertSuccessful("Unable to determine ip address of guest -- check error log for more details")
        val ip = result.stdOut().last()
        return ip;
    }

    private fun writeVagrantFile(project: Project) {
        project.write("Vagrantfile", project.settings.getDefaultVagrantFile())
    }

}
