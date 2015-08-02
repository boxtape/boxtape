package io.boxtape.cli;

import com.beust.jcommander.JCommander;
import io.boxtape.cli.commands.ShellCommand;
import io.boxtape.core.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ShellRunner implements CommandLineRunner{

    @Autowired
    JCommander commander;
    @Autowired
    Project project;
    @Override
    public void run(final String... args) throws Exception {
        commander.parse(args);
        JCommander parsedCommander = commander.getCommands().get(commander.getParsedCommand());
        ShellCommand command = (ShellCommand) parsedCommander.getObjects().get(0);
        project.getConsole().info("Running " + command.name());
        command.run(project);
    }
}
