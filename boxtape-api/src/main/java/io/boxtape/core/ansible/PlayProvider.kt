package io.boxtape.core.ansible

import io.boxtape.core.configuration.Configuration
import io.boxtape.core.LibraryArtifact

public interface PlayProvider {
    fun canProvideFor(libraryArtifact: LibraryArtifact): Boolean

    fun isNeeded(playbook: String): Boolean {
        return true;
    }

    fun provideVagrantConfiguration(): List<String> {
        return emptyList()
    }

    fun provideApplicationConfiguration() : List<String> {
        return emptyList()
    }
    fun provideRoles(libraryArtifact: LibraryArtifact, config: Configuration) : List<AnsibleRole> {
        return emptyList()
    }
    fun provide(libraryArtifact: LibraryArtifact, config: Configuration): List<AnsiblePlay> {
        return emptyList()
    }
}
