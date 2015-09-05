package io.boxtape.cli.core.ansible.plays

import io.boxtape.core.CoordinateComponentProvider
import io.boxtape.core.LibraryArtifact
import io.boxtape.core.Recipe
import io.boxtape.core.ansible.AnsibleRole
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.configuration.Configuration
import io.boxtape.getPropertyDeclarations
import io.boxtape.withoutKeys


public class DynamicPlayProvider(val recipe: Recipe) : PlayProvider {

    override fun canProvideFor(libraryArtifact: LibraryArtifact): Boolean {
        return recipe.resolutions.map { CoordinateComponentProvider.fromName(it) }
            .any { it.canProvideFor(libraryArtifact) }
    }

    override fun provideApplicationConfiguration() : List<String> {
        return emptyList()
    }

    override fun provideRoles(libraryArtifact: LibraryArtifact, config: Configuration) : List<AnsibleRole> {
        registerDeclaredProperties(config)
        registerForwardedPorts(config);
        return recipe.roles.map {

            val args = it.withoutKeys("name","src")

            registerConfigProperties(config,args.toList())
            AnsibleRole(
                name = it.get("name") as String,
                src = it.get("src") as String,
                args = config.resolveProperties(args).toList()
            )
        }
    }

    private fun registerForwardedPorts(config: Configuration) {
        recipe.forwardedPorts.forEach {
            config.addForwardedPort(it.host,it.guest)
        }
    }

    private fun registerDeclaredProperties(config: Configuration) {
        recipe.properties.forEach { config.registerPropertyWithDefault(it.key,it.value) }
    }

    private fun registerConfigProperties(config: Configuration, args: List<Pair<String, Any>>) {
        fun checkForConfigValues(values:Collection<*>) {
            values.forEach { value ->
                when (value) {
                    is String -> checkAndRegisterStringProperty(config, value)
                    is Collection<*> -> checkForConfigValues(value)
                    is Map<*,*> -> checkForConfigValues(value.values())
                }
            }
        }

        checkForConfigValues(args.map { it.second })
    }

    private fun checkAndRegisterStringProperty(config: Configuration, value: String) {
        value.getPropertyDeclarations().forEach {
            config.registerProperty(it)
        }
    }
}
