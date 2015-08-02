package io.boxtape.core.configuration

import io.boxtape.core.configuration.Configuration

/**
 * Builds a Boxtape Configuration,
 * by scanning various locations for a .boxtapeConfig.
 *
 * If none is found, ultimately uses hard-coded defaults
 */
public class BoxtapeSettingsProvider {

    fun build():BoxtapeSettings {
        // TODO
        return BoxtapeSettings()
    }
}
