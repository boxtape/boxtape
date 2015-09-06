package io.boxtape.core.configuration

import io.boxtape.withPlaceholdersReplaced
import org.apache.commons.lang3.text.StrSubstitutor

/**
 * Project configuration -- allows components
 * to register properties that will be ultimately written out
 * and configure the running application
 *
 * Not the same as BoxtapeSettings -- which focusses on Boxtape itself
 */
class Configuration {

    private val properties: MutableMap<String, String> = hashMapOf()
    val vagrantSettings = VagrantSettings()
    fun registerPropertyWithDefault(propertyName: String, defaultValue: String) {
        properties.put(propertyName, defaultValue)
    }

    fun registerPropertyWithoutValue(propertyName:String) {
        if (!properties.containsKey(propertyName)) {
            properties.put(propertyName,"")
        }
    }

    fun registerProperty(property: String) {
        // remove ${ and } at either end
        val strippedProperty =
            if (property.startsWith("\${") && property.endsWith("}")) {
                property.substring(2, property.length() - 1)
            } else {
                property
            }
        if (strippedProperty.contains(":")) {
            val keyValue = strippedProperty.splitBy(":")
            registerPropertyWithDefault(keyValue.get(0), keyValue.get(1))
        } else {
            registerPropertyWithoutValue(strippedProperty)
        }
    }

    fun getValue(property: String): String? {
        return properties.get(property).withPlaceholdersReplaced(properties)
    }


    fun hasProperty(property: String): Boolean {
        return properties.contains(property)
    }

    fun asStrings(): List<String> {
        return properties.map { "${it.key}=${it.value.withPlaceholdersReplaced(properties)}" }
    }

    fun addForwardedPort(hostPort: String, guestPort: String) {
        vagrantSettings.addForwardedPort(hostPort, guestPort)
    }

    fun resolveProperties(source: Map<String, Any>): Map<String,Any> {
        fun internalResolveProperties(value:Any):Any {
            return when (value) {
                    is String -> value.withPlaceholdersReplaced(properties)
                    is List<*> -> value.map { internalResolveProperties(it as Any) }
                    is Map<*,*> -> resolveProperties(value as Map<String, Any>)
                    else -> value
                }
        }
        val result:MutableMap<String,Any> = hashMapOf()
        val updated = source.map { entry ->
            val resolved = internalResolveProperties(entry.value)
            Pair(entry.key,resolved)
        }
        result.plusAssign(updated)
        return result
    }


}
