package com.youssefmahmoud.pubdevsearcher

import java.io.File

class DataManager(private val filename: String) {
    fun saveList(list: List<String>) {
        File(filename).writeText(list.joinToString(separator = "\n"))
    }

    fun loadList(): List<String> {
        return if (File(filename).exists()) {
            File(filename).readLines()
        } else {
            emptyList()
        }
    }
}