package com.starlabs.restoapp.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.starlabs.restoapp.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository@Inject constructor(
    private val db: CollectionReference,
    private val firebaseAuthInstance: FirebaseAuth
) {

    var currentUser: User? = null

    fun signInWithCredentials(credential: AuthCredential) =
        firebaseAuthInstance.signInWithCredential(credential)

    fun signOut() = firebaseAuthInstance.signOut()
    fun setFirebaseUser(firebaseUser: FirebaseUser) {
        currentUser = User().convert(firebaseUser)
    }
    suspend fun createUser(user: User): User? {

        val existingUser = user.email?.let { getUser(it) }

        if (existingUser?.userId.isNullOrEmpty()) {
            val newUser =
                hashMapOf(
                    "name" to user.name,
                    "email" to user.email,
                    "photo" to user.photo,
                    "rol" to user.rol,
                    "active" to user.active,
                    "qr" to user.qr,
                    "onPlace" to user.onPlace
                )

            val documentReference = db.add(newUser).await()
            user.userId = documentReference.id
        }

        currentUser = User().updateUser(user)
        return currentUser
    }

    suspend fun updateUser(user: User): User {
        val userDocumentRef = db.document(user.userId?:"")

        val updatedUserData = User().createUserMap(user)

        userDocumentRef.update(updatedUserData).await()

        currentUser = User().updateUser(user)
        return currentUser!!
    }

    suspend fun deleteUser(userId: String) {

        val userDocumentRef = db.document(userId)
        currentUser = User()
        userDocumentRef.delete().await()
    }

    suspend fun getUser(email: String): User? {
        val userDocumentRef = db.document(email)

        val documentSnapshot = userDocumentRef.get().await()

        return if (documentSnapshot.exists()) {
            val userData = documentSnapshot.data?.toMap()

            userData
                ?.let {
                    currentUser = User().createUser(it)
                    currentUser
                }
                ?:run { null }
        } else {
            null
        }
    }
}