package io.boxtape.core

import org.apache.maven.cli.MavenCli
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.regex.Pattern


@Component
public class MavenDependencyCollector @Autowired constructor(val mvn: MavenCli) {

    val ALPHA_CHARACTER = Pattern.compile("[a-zA-Z]");
    val GROUP_ID = 0
    val ARTIFACT_ID = 1
    val VERSION = 3

    fun collect(project: Project):List<Dependency> {
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val outputFile = File.createTempFile("dependencies",".txt")
        System.setProperty("maven.multiModuleProjectDirectory" , project.projectHome().canonicalPath)
        mvn.doMain(arrayOf("dependency:tree","-Dscope=compile", "-DoutputFile=${outputFile.getCanonicalPath()}"), project.projectHome().canonicalPath, printStream, printStream)

        val output = outputFile.readLines()
            .map { trimTreeSyntax(it) }
            .map {
                val parts = it.splitBy(":")
                Dependency(parts[GROUP_ID], parts[ARTIFACT_ID], parts[VERSION])
            }
        return output
    }

    private fun trimTreeSyntax(line: String): String {
        val matcher = ALPHA_CHARACTER.matcher(line)
        return if (matcher.find()) line.substring(matcher.start()) else ""
    }
}
