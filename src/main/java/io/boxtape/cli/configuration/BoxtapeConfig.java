package io.boxtape.cli.configuration;

import java.io.File;
import java.util.Optional;
import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

// classpath:resources/.boxtapeConfig (defaults)
// userHome/.boxtape/.boxtapeConfig
// projectRoot/.boxtapeConfig
// commandLine settings

@LoadPolicy(LoadType.MERGE)
@Sources({"file:boxtapeConfig.properties",
    "file:~/.boxtape/boxtapeConfig.properties",
    "classpath:boxtapeConfig.properties"})
public interface BoxtapeConfig extends Config {

    @Key("dispensary.url")
    String dispensaryUrl();

    @Key("dispensary.username")
    Optional<String> dispensaryUsername();

    @Key("dispensary.password")
    Optional<String> dispensaryPassword();

    @Key("cache.home")
    File cacheHome();
}
