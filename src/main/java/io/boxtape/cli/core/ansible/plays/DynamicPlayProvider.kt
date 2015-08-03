package io.boxtape.cli.core.ansible.plays

import io.boxtape.core.Dependency
import io.boxtape.core.Recipie
import io.boxtape.core.ansible.AnsibleRole
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.configuration.Configuration
import io.boxtape.withoutKeys
import java.util.regex.Pattern


public class DynamicPlayProvider(val recipie: Recipie) : PlayProvider {

    override fun canProvideFor(dependency: Dependency): Boolean {
        return recipie.resolutions.map { Dependency.fromName(it) }
            .any { dependency.matches(it) }
    }

    override fun provideApplicationConfiguration() : List<String> {
        return emptyList()
    }

    override fun provideRoles(dependency: Dependency, config: Configuration) : List<AnsibleRole> {
        registerDeclaredProperties(config)
        registerForwardedPorts(config);
        return recipie.roles.map {
            val args = it.withoutKeys("name","src").toList()
            registerConfigProperties(config,args)
            AnsibleRole(
                name = it.get("name") as String,
                src = it.get("src") as String,
                args = it.withoutKeys("name","src").toList()
            )
        }
    }

    private fun registerForwardedPorts(config: Configuration) {
        recipie.forwardedPorts.forEach {
            config.addForwardedPort(it.host,it.guest)
        }
    }

    private fun registerDeclaredProperties(config: Configuration) {
        recipie.properties.forEach { config.registerPropertyWithDefault(it.key,it.value) }
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
        val regex = Pattern.compile("\\$\\{(.+?)\\}");
        val matcher = regex.matcher(value);
        while (matcher.find()) {
            config.registerProperty(matcher.group())
        }
    }
}
