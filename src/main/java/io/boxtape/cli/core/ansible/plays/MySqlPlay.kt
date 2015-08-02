package io.boxtape.core.ansible.plays

import io.boxtape.core.configuration.Configuration
import io.boxtape.core.Dependency
import io.boxtape.core.ansible.AnsibleRole
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.toKeyValueMap
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

Component
public class MySqlPlay : PlayProvider {

    // TODO: https://github.com/bennojoy/mysql

    val MY_SQL = Dependency("mysql", "mysql-connector-java", "*")
    override fun canProvideFor(dependency: Dependency): Boolean {
        return dependency.matches(MY_SQL)
    }


    override fun provideRoles(dependency: Dependency, config: Configuration): List<AnsibleRole> {
        val dbName = "boxtape_example" // TODO : Randomize
        val dbUser = "boxtapeUser"
        val dbPassword = "B0xT4p3C0nf1g"

        config.registerPropertyWithDefault("spring.datasource.url", "jdbc:mysql://localhost:33060/${dbName}")
        config.registerPropertyWithDefault("spring.datasource.username",dbUser)
        config.registerPropertyWithDefault("spring.datasource.password",dbPassword)

        config.addForwardedPort("33060","3306")
        return listOf(AnsibleRole(
            name = "mysql",
            src = "bennojoy.mysql",
            args = listOf(
                Pair("sudo", "yes"),
                Pair("mysql_db", listOf("name=${dbName}".toKeyValueMap("="))),
                Pair("mysql_users", listOf(
                    "name=${dbUser}, pass=${dbPassword}, priv=\"*.*:ALL\", host=10.0.2.%".toKeyValueMap("="),
                    "name=${dbUser}, pass=${dbPassword}, priv=\"*.*:ALL\"".toKeyValueMap("=")))
            )
        ))
    }

    //    override fun provide(dependency: Dependency, config: Configuration): List<AnsiblePlay> {
    //        return listOf(AnsiblePlay("Install & configure MySQL")
    //            .withTask("apt")
    //                .withArg("update_cache","yes")
    //                .withArg("name","\"{{ item }}\"").build()
    //            .withItems("mysql-server")
    //            .sudo(true))
    //    }
}
