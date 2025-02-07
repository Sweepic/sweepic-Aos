package com.umc.sweepic.domain.repository.sweep

import com.umc.sweepic.domain.model.sweep.Gallery

object TrashRepository {
    private val trashedList = mutableSetOf<Gallery>()
    private val listeners = mutableListOf<() -> Unit>()

    fun addToTrash(item: Gallery) {
        trashedList.add(item)
        notifyListeners()
    }

    fun removeFromTrash(item: Gallery) {
        trashedList.remove(item)
        notifyListeners()
    }

    fun getAllTrashed(): List<Gallery> = trashedList.toList()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }

}