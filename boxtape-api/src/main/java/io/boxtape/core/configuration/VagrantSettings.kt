package io.boxtape.core.configuration

public class VagrantSettings() {
    val forwardedPorts: MutableList<VagrantSettings.ForwardedPort> = arrayListOf()
    data class ForwardedPort(val host: String, val guest: String)

    fun addForwardedPort(hostPort: String, guestPort: String) {
        forwardedPorts.add(ForwardedPort(hostPort, guestPort))
    }
}
