package io.boxtape.cli.core

import io.boxtape.core.LibraryArtifact
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Primary @Component
public class CompositeDependencyCollector @Autowired constructor(
    gradle:GradleDependencyCollector,
    maven:MavenDependencyCollector
) : DependencyCollector {
    val collectors = listOf(gradle,maven)
    override fun collect(project: Project): List<LibraryArtifact> {
        val result:MutableList<LibraryArtifact> = arrayListOf()
        collectors.forEach { collector ->
            result.addAll(collector.collect(project))
        }
        return result
    }

}
