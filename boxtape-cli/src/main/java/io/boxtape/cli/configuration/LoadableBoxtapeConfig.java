package io.boxtape.cli.configuration;

import java.io.File;
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
public interface LoadableBoxtapeConfig extends Config {

    @Key("dispensary.url")
    String dispensaryUrl();

    @Key("dispensary.username")
    String dispensaryUsername();

    @Key("dispensary.password")
    String dispensaryPassword();

    @Key("cache.home")
    File cacheHome();
}
