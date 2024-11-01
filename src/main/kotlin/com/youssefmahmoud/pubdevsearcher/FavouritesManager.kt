package com.youssefmahmoud.pubdevsearcher

import java.io.File

interface FavouritesChangeListener {
    fun onFavouriteAdded(favourite: String)
    fun onFavouriteRemoved(favourite: String)
}

class FavouritesManager {
    companion object {
        private fun getFavouritesFilePath(): String {
            val filePath: String = when {
                System.getProperty("os.name").startsWith("Windows") -> System.getenv("APPDATA") ?: System.getProperty("user.home") + "/AppData/Roaming"
                System.getProperty("os.name").startsWith("Mac") -> System.getProperty("user.home") + "/Library/Application Support"
                else -> System.getProperty("user.home") + "/.config"  // Default for Linux
            }
            return "$filePath/favourites.txt"
        }

        val instance: FavouritesManager by lazy { FavouritesManager() }
    }

    private val dataManager = DataManager(getFavouritesFilePath())
    private var favourites: MutableList<String> = dataManager.loadList().toMutableList()
    private val listeners: MutableList<FavouritesChangeListener> = mutableListOf()

    fun getFavourites(): List<String> {
        return favourites.toList()
    }

    fun saveFavourites() {
        dataManager.saveList(favourites)
    }

    fun addFavourite(favourite: String) {
        if (!favourites.contains(favourite)) {
            favourites.add(favourite)
            saveFavourites()
            notifyFavouriteAdded(favourite)
        }
    }

    fun removeFavourite(favourite: String) {
        if (favourites.remove(favourite)) {
            saveFavourites()
            notifyFavouriteRemoved(favourite)
        }
    }

    fun isFavourite(favourite: String): Boolean {
        return favourites.contains(favourite)
    }

    fun addListener(listener: FavouritesChangeListener) {
        listeners.add(listener)
    }

    private fun notifyFavouriteAdded(favourite: String) {
        listeners.forEach { it.onFavouriteAdded(favourite) }
    }

    private fun notifyFavouriteRemoved(favourite: String) {
        listeners.forEach { it.onFavouriteRemoved(favourite) }
    }
}