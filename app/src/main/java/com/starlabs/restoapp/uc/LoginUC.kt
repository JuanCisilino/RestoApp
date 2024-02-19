package com.starlabs.restoapp.uc

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUC@Inject constructor(private val userRepository: UserRepository) {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    suspend fun getUser(email: String) = withContext(dispatcher) {
        userRepository.getUser(email)
    }

    suspend fun newGetUser(email: String) = withContext(dispatcher) {
        userRepository.newGetUser(email)
    }

    suspend fun createUser(user: User) = withContext(dispatcher) {
        userRepository.createUser(user)
    }

    fun setCurrentUser(user: User) = userRepository.setCurrentUser(user)
    fun setFirebaseUser(firebaseUser: FirebaseUser) {
        userRepository.setFirebaseUser(firebaseUser)
    }

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun signInWithCredentials(credential: AuthCredential) =
        userRepository.signInWithCredentials(credential)

    fun signOut() = userRepository.signOut()

}