package com.starlabs.restoapp.uc

import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginUCTest {

    @RelaxedMockK
    private lateinit var repository: UserRepository

    lateinit var useCase: LoginUC
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        useCase = LoginUC(repository)
    }

    @Test
    fun `when the api doesnt return anything then return null`() = runBlocking{
        //Given
        coEvery { repository.getUser("") } returns null

        //When
        val response = useCase.getUser("")

        //Then
        assert(response == null)

    }

    @Test
    fun `when the api returns something then return empty list`() = runBlocking{

        //Given
        coEvery { repository.currentUser } returns User()

        //When
        val response = useCase.getCurrentUser()

        //Then
        assert(response != null)

    }
}