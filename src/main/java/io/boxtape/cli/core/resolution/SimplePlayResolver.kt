package io.boxtape.cli.core.resolution

import com.google.common.collect.ArrayListMultimap
import io.boxtape.core.Dependency
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.cli.core.resolution.PlayResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

Component
public class SimplePlayResolver @Autowired constructor(val repository: PlayRepository) : PlayResolver {

    override fun resolve(dependencies: Iterable<Dependency>): ArrayListMultimap<Dependency, PlayProvider> {
        val result: ArrayListMultimap<Dependency, PlayProvider> = ArrayListMultimap.create()
        dependencies.forEach { dependency ->
            repository.playProviders.filter { it.canProvideFor(dependency) }
                .forEach { result.put(dependency, it) }
        }
        return result
    }

}
