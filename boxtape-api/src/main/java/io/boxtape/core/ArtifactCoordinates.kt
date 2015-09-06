package io.boxtape.core

/**
 * Describes a qualified, versioned artifact
 * (eg., Maven coordinates)
 */
interface ArtifactCoordinates {
    val groupId: String
    val artifactId: String
    val version: String

    companion object {
        fun fromName(name: String): ArtifactCoordinates {
            val parts = name.splitBy(":")
            return DefaultCoordinates(parts[0], parts[1], parts[2])
        }
    }
}

data class DefaultCoordinates(
    override val groupId: String,
    override val artifactId: String,
    override val version: String):ArtifactCoordinates {

}

