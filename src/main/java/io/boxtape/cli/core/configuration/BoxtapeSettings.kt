package io.boxtape.core.configuration

public class BoxtapeSettings {
    fun getDefaultVagrantFile(): String {
        // TODO : Make this configurable
        val vagrantFile = this.javaClass.getClassLoader().getResource("Vagrantfile").readText()
        return vagrantFile
    }

    var projectConfigFilePath = ".boxtape/application.properties"
    var vagrantSettingsPath = ".boxtape/vagrantSettings.yml"
}
