package io.boxtape.cli;

import java.util.List;
import com.beust.jcommander.JCommander;
import io.boxtape.cli.commands.ShellCommand;
import io.boxtape.core.Project;
import org.apache.maven.cli.MavenCli;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MavenCli mavenCli() {
        return new MavenCli();
    }

    @Bean
    public Project project() {
        return new Project();
    }
    @Bean
    public JCommander jCommander(Project project, List<ShellCommand> commandList) {
        final JCommander jCommander = new JCommander(project);
        commandList.forEach( command -> jCommander.addCommand(command.name(), command));
        return jCommander;
    }

}
