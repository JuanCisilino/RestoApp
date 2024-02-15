package com.starlabs.restoapp.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.starlabs.restoapp.model.Item
import kotlinx.coroutines.tasks.await
import java.util.Random
import java.util.UUID
import javax.inject.Inject

class ItemRepository@Inject constructor(
    private val db: DatabaseReference,
    private val storage: StorageReference
) {

    private lateinit var reference: StorageReference

    private lateinit var uploadTask: UploadTask
    private lateinit var urlTask: Task<Uri>

    var itemList: List<Item>? = null

    init {
        getItems()
    }
    fun createItem(item: Item): Item {

        item.itemId?.let { updateItem(item) }
            ?:run {
                item.itemId = createItemId()
                val newItem =
                    hashMapOf(
                        "item_id" to item.itemId,
                        "precio" to item.precio,
                        "imagen" to getUrl(),
                        "descripcion" to item.descripcion,
                        "titulo" to item.titulo,
                        "tipo" to item.tipo
                    )
                db.child(item.itemId?:"no generado").setValue(item)
            }
        return item
    }
    fun saveToDB(name: String, uri: Uri){
        uploadTask = saveImageToDB(name, uri)
        urlTask = uploadTask.continueWithTask { reference.downloadUrl }
    }
    fun deleteItem(itemId: String) {
        val postRef = db.child(itemId)
        postRef.removeValue()
    }

    private fun createItemId(): String {
        var itemId = "0"
        itemList?.let { list ->
            do {
                // Generar un ID único (en este caso, un UUID aleatorio)
                itemId = UUID.randomUUID().toString()
                // Verificar si el ID generado ya existe en la lista
            } while (list.any { it.itemId == itemId })
        }
        return itemId
    }
    private fun getItems() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<Item>()
                snapshot.children.forEach { data ->
                    data.getValue(Item::class.java)?.let {
                        if (!tempList.contains(it)) tempList.add(it)
                    }
                }
                itemList = tempList.sortedByDescending { it.tipo }
            }

            override fun onCancelled(error: DatabaseError) {
                itemList = null
            }
        })
    }
    private fun updateItem(item: Item) {
        db.child(item.itemId!!).setValue(item)
    }
    private fun saveImageToDB(name: String, uri: Uri): UploadTask {
        reference = storage.child(name)
        return reference.putFile(uri)
    }
    private fun getUrl(): String =
        if (urlTask.isComplete and urlTask.isSuccessful) urlTask.result.toString() else "aun no hay datos"
}