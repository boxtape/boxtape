package io.boxtape.core

import com.github.zafarkhaja.semver.Version

/**
 * A project dependency (ie., a set of maven co-ordinates).
 */
data class LibraryArtifact(coordinates: ArtifactCoordinates) : ArtifactCoordinates by coordinates {
    constructor(groupId: String,
                artifactId: String,
                version: String) : this(DefaultCoordinates(groupId, artifactId, version));

    companion object {
        fun fromName(name: String): LibraryArtifact {
            return LibraryArtifact(
                ArtifactCoordinates.fromName(name)
            )
        }
    }


    fun name(): String = "${groupId}:${artifactId}:${version}"
}
