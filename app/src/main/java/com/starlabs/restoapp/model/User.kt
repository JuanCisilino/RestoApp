package com.starlabs.restoapp.model

import com.google.firebase.auth.FirebaseUser

data class User(
    var email: String?=null,
    var name: String?=null,
    var rol: String?=null,
    var photo: String?=null,
    var active: Boolean?= false,
    var qr: String?= null,
    var onPlace: Boolean?= false,
    var error: ErrorResponse?= null
) {

    fun convert(user: FirebaseUser): User{
        this.email = user.email.toString()
        this.name = user.displayName.toString()
        this.photo = user.photoUrl.toString()
        return this
    }

    fun mapUser(userData: Map<String, Any>): User{
        this.email = userData["email"].toString()
        this.name = userData["name"].toString()
        this.rol = userData["rol"].toString()
        this.photo = userData["photo"].toString()
        return this
    }

    fun createUserMap(user: User) =
        hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "photo" to user.photo,
            "rol" to user.rol,
            "active" to user.active,
            "qr" to user.qr,
            "onPlace" to user.onPlace
        ).toMap()
    fun updateUser(updatedUser: User): User {
        this.email = updatedUser.email
        this.name = updatedUser.name
        this.rol = updatedUser.rol
        this.photo = updatedUser.photo
        this.active = updatedUser.active
        this.qr = updatedUser.qr
        this.onPlace = updatedUser.onPlace
        this.error = updatedUser.error
        return this
    }
}
