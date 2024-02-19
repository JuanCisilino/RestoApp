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

    private var currentUser: User? = null

    fun signInWithCredentials(credential: AuthCredential) =
        firebaseAuthInstance.signInWithCredential(credential)

    fun signOut() = firebaseAuthInstance.signOut()

    fun setCurrentUser(user: User) {
        currentUser = user
    }

    fun getCurrentUser() = currentUser
    fun setFirebaseUser(firebaseUser: FirebaseUser) {
        currentUser = User().convert(firebaseUser)
    }
    suspend fun createUser(user: User): User? {
        getUser(user.email!!)

        currentUser?.rol
            ?.let { currentUser = User().updateUser(user) }
            ?:run {
                val newUser =
                    hashMapOf(
                        "name" to user.name,
                        "email" to user.email,
                        "photo" to user.photo,
                        "rol" to "user",
                        "active" to user.active,
                        "qr" to user.qr,
                        "onPlace" to user.onPlace
                    )

                db.document(user.email!!).set(newUser)
                user.rol = "user"
                currentUser = user
            }

        return currentUser
    }

    suspend fun updateUser(user: User): User {
        val userDocumentRef = db.document(user.email?:"")

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
                    currentUser = User().mapUser(it)
                    currentUser
                }
                ?:run { null }
        } else {
            null
        }
    }

    fun newGetUser(email: String) = db.document(email).get()
}