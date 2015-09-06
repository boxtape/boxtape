package io.boxtape.core

import io.boxtape.core.configuration.VagrantSettings

data class Recipe(
    val name: String,
    val resolutions: List<String>,
    val forwardedPorts: List<VagrantSettings.ForwardedPort>,
    val roles: List<Map<String,Any>>,
    val properties: Map<String,String>
    ) {
}
