package com.starlabs.restoapp.uc

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.starlabs.restoapp.model.Item
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.repository.ItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ABMUC@Inject constructor(private val itemRepository: ItemRepository) {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    suspend fun getMenuItems() = withContext(dispatcher) {
        itemRepository.itemList?.map { it.mapToMenuItem() }
    }

    suspend fun getItems() = withContext(dispatcher) {
        itemRepository.itemList
    }

    suspend fun createItem(item: Item) = withContext(dispatcher) {
        itemRepository.createItem(item)
    }

    suspend fun deleteItem(itemId: String) = withContext(dispatcher) {
        itemRepository.deleteItem(itemId)
    }

    suspend fun saveToDB(name: String, uri: Uri) = withContext(dispatcher) {
        itemRepository.saveToDB(name, uri)
    }
}