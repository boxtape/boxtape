package io.boxtape.cli.configuration

import io.boxtape.core.configuration.BoxtapeSettings
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

    fun build(): BoxtapeSettings {
        val config = ConfigFactory.create(javaClass<BoxtapeConfig>())
        return BoxtapeSettings(additionalRecipePath,config)
    }
}
