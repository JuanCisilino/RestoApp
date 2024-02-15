package com.starlabs.restoapp.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.starlabs.restoapp.repository.ItemRepository
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

    private fun provideItemsCollection() = FirebaseDatabase.getInstance().getReference("restopizza_menu")
    private fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    private fun provideStorageInstance() = FirebaseStorage.getInstance().getReference("restopizza_images")
    @Provides
    @Singleton
    fun provideUserRepo() = UserRepository(provideUsersCollection(), provideFirebaseAuthInstance())

    @Provides
    @Singleton
    fun provideItemRepo() = ItemRepository(provideItemsCollection(), provideStorageInstance())
    @Provides
    @Singleton
    fun provideLoginUC(userRepository: UserRepository) = LoginUC(userRepository)
}