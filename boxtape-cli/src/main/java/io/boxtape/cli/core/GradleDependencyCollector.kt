package io.boxtape.cli.core

import io.boxtape.core.LibraryArtifact
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.idea.IdeaProject
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency
import org.springframework.stereotype.Component
import java.io.File

@Component
class GradleDependencyCollector(val gradleHome: File? = null):DependencyCollector {

    override fun collect(project: Project): List<LibraryArtifact> {
        if (!project.hasFile("build.gradle")) {
            return listOf()
        }

        val result : MutableList<LibraryArtifact> = arrayListOf()
        val projectConnection = gradleConnector()
            .forProjectDirectory(project.projectHome())
            .connect();
        val gradle = projectConnection.getModel(javaClass<IdeaProject>())
        gradle.getModules().forEach { module ->
            module.getDependencies()
                .filter { it is IdeaSingleEntryLibraryDependency }
                .map {
                    (it as IdeaSingleEntryLibraryDependency).getGradleModuleVersion()
                }
                .mapTo(result,  { LibraryArtifact(it.getGroup(), it.getName(), it.getVersion())} )
        }
        return result.toList()
    }

    private fun gradleConnector(): GradleConnector {
        val gradle = GradleConnector.newConnector()
        if (gradleHome != null) {
            gradle.useInstallation(gradleHome)
        }
        return gradle
    }


    fun parseArtifactName(line: String): String {
        return ""
    }
}
