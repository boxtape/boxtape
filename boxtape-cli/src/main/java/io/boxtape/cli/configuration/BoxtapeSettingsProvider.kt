package io.boxtape.cli.configuration

import io.boxtape.core.configuration.LoadableBoxtapeSettings
import org.aeonbits.owner.ConfigFactory

/**
 * Builds a Boxtape Configuration,
 * by scanning various locations for a .boxtapeConfig.
 *
 * If none is found, ultimately uses hard-coded defaults
 */
public class BoxtapeSettingsProvider(
    val additionalRecipePath: String? = null
) {

    fun build(): LoadableBoxtapeSettings {
        val config = ConfigFactory.create(javaClass<LoadableBoxtapeConfig>())
        return LoadableBoxtapeSettings(additionalRecipePath,config)
    }
}
