package io.boxtape.core

import com.github.zafarkhaja.semver.Version

public data class CoordinateComponentProvider(
    /**
     * A coordinate range, which this provider
     * can provide a recipie for.  May contain a
     * version range
     */
    val coordinateRange: ArtifactCoordinates
) {
    constructor(groupId: String,
                artifactId: String,
                version: String) : this(DefaultCoordinates(groupId, artifactId, version));
    companion object {
        fun fromName(name:String):CoordinateComponentProvider {
            return CoordinateComponentProvider(
                ArtifactCoordinates.fromName(name)
            )
        }
    }
    /**
     * Indicates if this instance can provide a recipe
     * for the provided artifact coordinates.
     *
     */
    public fun canProvideFor(other: ArtifactCoordinates): Boolean {
        return coordinateRange.groupId == other.groupId &&
            coordinateRange.artifactId == other.artifactId &&
            SemVer.parse(other.version).satisfies(coordinateRange.version)
    }
}
