package io.boxtape.core

import com.github.zafarkhaja.semver.Version
import com.google.common.base.Splitter

/**
 * Lenient parser of SemVer versions
 *
 * Will pad missing components (eg., 51 -> 51.0.0)
 */
object SemVer {
    public fun parse(version:String): Version {
        val parts = version.splitBy(".")
        return when (parts.size()) {
            1 -> Version.valueOf("${version}.0.0")
            2 -> Version.valueOf("${version}.0")
            3 -> Version.valueOf(version)
            else -> Version.valueOf(parts.subList(0,3).join("."))
        }

    }
}
