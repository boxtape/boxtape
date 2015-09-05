package io.boxtape.cli.core.resolution

import com.google.common.collect.ArrayListMultimap
import io.boxtape.cli.core.resolution.dispensary.DispensaryPlayResolver
import io.boxtape.core.LibraryArtifact
import io.boxtape.core.ansible.PlayProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
public class CompositePlayResolver
    @Autowired constructor(val localResolver:SimplePlayResolver,
                           val remoteResolver:DispensaryPlayResolver)
    : PlayResolver {
    val resolvers = listOf(localResolver,remoteResolver)
    override fun resolve(dependencies: Iterable<LibraryArtifact>): ArrayListMultimap<LibraryArtifact, PlayProvider> {
        val result : ArrayListMultimap<LibraryArtifact, PlayProvider> = ArrayListMultimap.create()
        resolvers.forEach { result.putAll(it.resolve(dependencies)) }
        return result
    }

}


