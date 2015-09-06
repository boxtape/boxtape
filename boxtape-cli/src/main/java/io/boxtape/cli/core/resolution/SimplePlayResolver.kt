package io.boxtape.cli.core.resolution

import com.google.common.collect.ArrayListMultimap
import io.boxtape.core.LibraryArtifact
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.cli.core.resolution.PlayResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

Component
public class SimplePlayResolver @Autowired constructor(val repository: PlayRepository) : PlayResolver {

    override fun resolve(dependencies: Iterable<LibraryArtifact>): ArrayListMultimap<LibraryArtifact, PlayProvider> {
        val result: ArrayListMultimap<LibraryArtifact, PlayProvider> = ArrayListMultimap.create()
        dependencies.forEach { dependency ->
            repository.playProviders.filter { it.canProvideFor(dependency) }
                .forEach { result.put(dependency, it) }
        }
        return result
    }

}
