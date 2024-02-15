package com.starlabs.restoapp.model

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Item(
    var itemId: String?=null,
    var precio: Double?=null,
    var imagen: String?=null,
    var descripcion: String?=null,
    var titulo: String?=null,
    var tipo: String?=null

): Serializable, DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    fun mapToMenuItem() = MenuItem(
        itemId = this.itemId,
        precio = this.precio,
        imagen = this.imagen,
        descripcion = this.descripcion,
        titulo = this.titulo,
        tipo = this.tipo,
        cantidad = 0
    )
    fun createItem(itemData: Map<String, Any>): Item{
        this.itemId = itemData["itemId"].toString()
        this.precio = itemData["precio"].toString().toDouble()
        this.imagen = itemData["imagen"].toString()
        this.descripcion = itemData["descripcion"].toString()
        this.titulo = itemData["titulo"].toString()
        this.tipo = itemData["tipo"].toString()
        return this
    }

    fun createHash(item: Item) =
        hashMapOf(
            "itemId" to item.itemId,
            "precio" to item.precio,
            "imagen" to item.imagen,
            "descripcion" to item.descripcion,
            "titulo" to item.titulo,
            "tipo" to item.tipo
        )

    fun updateItem(updatedItem: Item): Item {
        this.itemId = updatedItem.itemId
        this.precio = updatedItem.precio
        this.imagen = updatedItem.imagen
        this.descripcion = updatedItem.descripcion
        this.titulo = updatedItem.titulo
        this.tipo = updatedItem.tipo
        return this
    }
}

enum class Tipo {
    BEBIDA,
    COMIDA
}