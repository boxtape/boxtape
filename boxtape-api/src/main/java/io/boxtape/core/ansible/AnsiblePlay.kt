package io.boxtape.core.ansible

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

JsonPropertyOrder("taskMap")
public class AnsiblePlay(val name: String) {

    JsonProperty("with_items")
    JsonInclude(JsonInclude.Include.NON_EMPTY)
    private var items: List<String> = emptyList();

    private var task: Task? = null;

    JsonProperty
    JsonInclude(JsonInclude.Include.NON_NULL)
    private var sudo: Boolean? = null;

    JsonAnyGetter
    private fun taskMap(): Map<*, *>? {
        val jsonMapper = ObjectMapper();
        val map = jsonMapper.convertValue(task,javaClass<Map<*,*>>())
        return map;
    }

    fun withTask(name: String): TaskBuilder {
        return TaskBuilder(name, this)
    }

    class TaskBuilder(val name: String, private val play: AnsiblePlay) {
        private val params: MutableList<Pair<String, String>> = arrayListOf()

        fun withArg(key: String, value: String): TaskBuilder {
            params.add(Pair(key, value))
            return this;
        }

        fun build(): AnsiblePlay {
            play.task = Task(name, params.toList())
            return play;
        }
    }

    fun withItems(vararg items: String): AnsiblePlay {
        return withItems(items.toArrayList())
    }

    fun withItems(items: List<String>): AnsiblePlay {
        this.items = items;
        return this
    }

    fun asYaml(): String {
        // HACK : The YAMLFactory seems to explode with custom writers
        // So, convert to a Map via Json, then out to YAML.
        // See https://github.com/FasterXML/jackson-module-scala/issues/192
        // (Project is unrelated, but test showing same issue)
        val jsonMapper = ObjectMapper()
        val map = jsonMapper.convertValue(this, javaClass<Map<*, *>>())
        val yamlMapper = ObjectMapper(YAMLFactory())
        return yamlMapper.writeValueAsString(map);
    }

    fun sudo(value: Boolean): AnsiblePlay {
        this.sudo = value
        return this
    }

}

