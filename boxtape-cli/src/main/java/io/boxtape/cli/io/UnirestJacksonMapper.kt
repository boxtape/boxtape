package io.boxtape.cli.io

import com.mashape.unirest.http.ObjectMapper
import io.boxtape.mappers.Mappers

public class UnirestJacksonMapper : ObjectMapper {
    override fun <T> readValue(value: String?, valueType: Class<T>?): T {
        return Mappers.jsonMapper.readValue(value,valueType)
    }

    override fun writeValue(value: Any?): String? {
        return Mappers.jsonMapper.writeValueAsString(value)
    }

}
