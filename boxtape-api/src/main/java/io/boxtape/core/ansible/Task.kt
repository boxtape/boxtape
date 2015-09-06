package io.boxtape.core.ansible

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.boxtape.yaml.TaskWriter


@JsonSerialize(using = TaskWriter::class )
public data class Task(val name:String, val args:List<Pair<String, String>>)

