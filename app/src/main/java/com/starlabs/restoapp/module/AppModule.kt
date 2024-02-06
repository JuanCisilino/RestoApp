package com.starlabs.restoapp.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.starlabs.restoapp.repository.UserRepository
import com.starlabs.restoapp.uc.LoginUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun provideUsersCollection() = Firebase.firestore.collection("restopizza_usuarios")

    private fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
    @Provides
    @Singleton
    fun provideUserRepo() = UserRepository(provideUsersCollection(), provideFirebaseAuthInstance())

    @Provides
    @Singleton
    fun provideLoginUC(userRepository: UserRepository) = LoginUC(userRepository)
}