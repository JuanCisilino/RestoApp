package com.starlabs.restoapp.model

data class MenuItem(
    var itemId: String?=null,
    var precio: Double?=null,
    var imagen: String?=null,
    var descripcion: String?=null,
    var titulo: String?=null,
    var tipo: String?=null,
    var cantidad: Int?=0
)
