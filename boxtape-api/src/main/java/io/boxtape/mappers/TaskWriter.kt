package io.boxtape.yaml

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.boxtape.core.ansible.Task

class TaskWriter : JsonSerializer<Task>() {
    override fun serialize(task: Task, gen: JsonGenerator, provider: SerializerProvider) {
        val value = task.args.map { "${it.first}=${it.second}" }.joinToString(" ")
        gen.writeStringField(task.name,value)
    }
}

