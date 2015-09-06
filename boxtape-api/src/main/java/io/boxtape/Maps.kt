package io.boxtape

fun <K,V> Map<K,V>.withoutKeys(vararg keys:K):Map<K,V> {
    return this.filterKeys { !keys.contains(it) }
}
