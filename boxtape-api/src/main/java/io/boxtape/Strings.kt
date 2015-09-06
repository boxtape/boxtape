package io.boxtape

import com.google.common.base.Splitter
import io.boxtape.mappers.Mappers
import org.apache.commons.lang3.text.StrLookup
import org.apache.commons.lang3.text.StrSubstitutor
import java.util.regex.Pattern

fun String.toPairList(keyValueSeperator: String = ":", itemSeperator: String = ","): List<Pair<String, String>> {
    return Splitter.on(itemSeperator)
        .trimResults()
        .withKeyValueSeparator(keyValueSeperator)
        .split(this)
        .map { Pair(it.key, it.value) }
}

fun String.toKeyValueMap(keyValueSeperator: String = ":", itemSeperator: String = ","): Map<String, String> {
    return this.toPairList(keyValueSeperator, itemSeperator).toMap()
}

fun Any.asYaml(): String {
    return Mappers.yamlMapper.writeValueAsString(this);
}
fun Any.asJson(): String {
    return Mappers.jsonMapper.writeValueAsString(this);
}

fun String?.withPlaceholdersReplaced(replacements: Map<String, String>): String {
    val lookup = object : StrLookup<String>() {
        override fun lookup(key: String?): String? {
            if (key == null) {
                return null
            }
            val parts = key.splitBy(":")
            val default = if (parts.size() > 1) parts[1] else null
            return replacements.getOrElse(parts[0]) { default }
        }

    }
    val substitutor = StrSubstitutor(lookup)
    return substitutor.replace(this)
}

fun String.hasPropertyPlaceholders(): Boolean {
    // TODO : DRY this up -- how do I declare a static value for extensions?
    val regex = Pattern.compile("\\$\\{(.+?)\\}");
    val matcher = regex.matcher(this);
    return matcher.matches()
}

fun String.getPropertyDeclarations(): List<String> {
    val regex = Pattern.compile("\\$\\{(.+?)\\}");
    val matcher = regex.matcher(this);
    val result: MutableList<String> = arrayListOf()
    while (matcher.find()) {
        val match = matcher.group()
        result.add(match.substring(2, match.length() - 1))
    }
    return result
}
