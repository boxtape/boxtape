package io.boxtape.cli.core.resolution

import com.google.common.collect.ArrayListMultimap
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.LibraryArtifact

public interface PlayResolver {
    fun resolve(dependencies: Iterable<LibraryArtifact>): ArrayListMultimap<LibraryArtifact, PlayProvider>
}
