package io.boxtape.cli.commands

import io.boxtape.core.Project

public interface ShellCommand {
    fun name():String
    fun run(project: Project)
}
