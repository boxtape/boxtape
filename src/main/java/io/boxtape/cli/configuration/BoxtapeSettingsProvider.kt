package io.boxtape.cli.configuration

import io.boxtape.core.configuration.BoxtapeSettings
import java.io.File

/**
 * Builds a Boxtape Configuration,
 * by scanning various locations for a .boxtapeConfig.
 *
 * If none is found, ultimately uses hard-coded defaults
 */
public class BoxtapeSettingsProvider(
    val additionalRecipePath: String? = null
) {

    fun build(): BoxtapeSettings {
        return BoxtapeSettings(additionalRecipePath)
    }
}
