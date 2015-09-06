package io.boxtape.mappers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.boxtape.yaml.PairWriter

public object Mappers {
    val yamlMapper = ObjectMapper(YAMLFactory())
    val jsonMapper = ObjectMapper()

    init {
        val localModule = SimpleModule("Boxtape custom mappers")
        localModule.addSerializer(javaClass<Pair<Any, Any>>(), PairWriter())
        val modules = listOf(KotlinModule(),GuavaModule(), localModule)

        listOf(yamlMapper,jsonMapper).forEach { mapper ->
            modules.forEach { module ->
                System.out.println("Registered module ${module.getModuleName()}")
                mapper.registerModule(module)
            }
        }
    }

}
