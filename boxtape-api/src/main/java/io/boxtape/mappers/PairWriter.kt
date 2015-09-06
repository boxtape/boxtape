package io.boxtape.yaml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class PairWriter : JsonSerializer<Pair<Any, Any>>() {

    override fun serialize(value: Pair<Any, Any>?, gen: JsonGenerator?, provider: SerializerProvider?) {
        if (value == null) {
            return
        }
        gen!!.writeObjectField(value.first.toString(), value.second)
    }

}
