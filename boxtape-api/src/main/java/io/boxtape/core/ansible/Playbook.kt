package io.boxtape.core.ansible

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import io.boxtape.asJson
import io.boxtape.asYaml


public data class Playbook(
    var hosts: String = "all",
    JsonProperty("remote_user") var  remoteUser: String = "vagrant",
    JsonInclude(JsonInclude.Include.NON_EMPTY) var vars: List<Pair<String, String>> = emptyList(),
    plays: List<AnsiblePlay> = emptyList()

) {
    // TODO : Hack -- See http://stackoverflow.com/questions/31663592/applying-a-role-under-sudo-user/31664215#31664215
    var sudo: Boolean = true;
    var tasks: MutableList<AnsiblePlay> = plays.toArrayList()
    private val roles: MutableList<AnsibleRole> = arrayListOf()
    fun addPlays(plays: List<AnsiblePlay>) {
        tasks.addAll(plays)
    }

    fun addRoles(roles: List<AnsibleRole>) {
        this.roles.addAll(roles)
    }

    JsonInclude(JsonInclude.Include.NON_EMPTY)
    JsonProperty("roles")
    fun getRoleDeclarations(): List<Map<String, Any>> {
        val result = this.roles.map { it.asPlaybookDeclaration() }
        return result
    }

    JsonIgnore
    fun getRequirementsYaml():String {
        return roles.asSequence().map { it.asRequirement() }
            .toList().asYaml()
    }
}

