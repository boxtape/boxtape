package io.boxtape.cli.core.resolution.dispensary

import com.google.common.collect.Multimap
import org.apache.commons.io.FilenameUtils
import java.io.File

public data class DispensaryLookupRequest(
    val requirements:List<String>
)

public data class DispensaryLookupResultSet(val matches: Multimap<String,LookupResult>)
public data class LookupResult(val name: String, val url: String) {
    fun toPath(): String {
        return name.replace("@","/")
    }

    fun toPath(root: String): String {
        return FilenameUtils.concat(root,toPath())
    }

    fun toBoxtapeFilePath(root:String):String {
        return FilenameUtils.concat(toPath(root), "boxtape.yml")
    }
    fun toBoxtapeFile(root:String): File {
        return File(toBoxtapeFilePath(root))
    }
}
