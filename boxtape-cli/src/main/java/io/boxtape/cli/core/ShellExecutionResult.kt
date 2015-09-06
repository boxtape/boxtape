package io.boxtape.core

import io.boxtape.cli.Loggers
import org.apache.commons.exec.LogOutputStream
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.logging.LogFactory

public data class ShellExecutionResult(val outputPrefix:String) {
    private val log =  Loggers.BOXTAPE_CONSOLE
    var exitCode : Int = 0;
    private var stdOut:MutableList<String> = arrayListOf()
    private var stdErr:MutableList<String> = arrayListOf()
    val streamHandler = PumpStreamHandler(
        CollectingOutputStream(stdOut, { log.info(it)}),
        CollectingOutputStream(stdErr, { log.error(it)})
        )

    fun stdOut():List<String> = stdOut.toList()
    fun stdErr():List<String> = stdErr.toList()

    fun assertSuccessful(error:String = "Command execution failed") {
        if (exitCode != 0) {
            throw IllegalStateException(error)
        }
    }

    class CollectingOutputStream(val appendTarget:MutableList<String>, val writeToLog:(String) -> Unit) : LogOutputStream() {
        override fun processLine(line: String, logLevel: Int) {
            appendTarget.add(line)
            writeToLog(line)
        }

    }

    fun containedMessage(message: String): Boolean {
        return stdOut.any { it.contains(message) }
    }
}
