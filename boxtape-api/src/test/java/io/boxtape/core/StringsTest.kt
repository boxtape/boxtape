package io.boxtape.core

import io.boxtape.getPropertyDeclarations
import io.boxtape.hasPropertyPlaceholders
import io.boxtape.withPlaceholdersReplaced
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

public class StringsTest {
    @Test
    fun detectsPropertyNames() {
        assertTrue("\${myPropertyName}".hasPropertyPlaceholders())
        assertEquals("\${myPropertyName}".getPropertyDeclarations(), listOf("myPropertyName"))
    }

    @Test
    fun resolvesPropertiesThatHaveDefaults() {
        val replacements = mapOf("name" to "Marty")
        assertEquals("\${name}".withPlaceholdersReplaced(replacements), "Marty")
        assertEquals("\${name:Jimmy}".withPlaceholdersReplaced(replacements), "Marty")
        assertEquals("My name is \${name}".withPlaceholdersReplaced(replacements), "My name is Marty")
        assertEquals("My name is \${name:Jimmy}".withPlaceholdersReplaced(replacements), "My name is Marty")
        assertEquals("My name is \${firstName:Jimmy}".withPlaceholdersReplaced(replacements), "My name is Jimmy")
    }
}
