package io.boxtape.cli.core

import io.boxtape.core.LibraryArtifact

public interface DependencyCollector {
    fun collect(project:Project):List<LibraryArtifact>
}
