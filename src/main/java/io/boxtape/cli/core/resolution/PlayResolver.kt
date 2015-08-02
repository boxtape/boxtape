package io.boxtape.core.resolution

import com.google.common.collect.ArrayListMultimap
import io.boxtape.core.ansible.PlayProvider
import io.boxtape.core.Dependency

public interface PlayResolver {
    fun resolve(dependencies: Iterable<Dependency>): ArrayListMultimap<Dependency, PlayProvider>
}
