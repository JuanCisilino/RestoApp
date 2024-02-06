package com.starlabs.restoapp.ui.login

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.starlabs.restoapp.helpers.LoadState
import com.starlabs.restoapp.model.User
import com.starlabs.restoapp.uc.LoginUC
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    @RelaxedMockK
    private lateinit var useCase: LoginUC

    private lateinit var viewModel: LoginViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(useCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter(){
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewmodel is created at the first time, get all items and set it, then validate live data is not null`() = runTest {

        //Given
        coEvery { useCase.getCurrentUser() } returns User()

        //When
        viewModel.getUser("")


        //Then
        assert(viewModel.userLiveData.value != null)
    }

    @Test
    fun `when search returns null validate live data value as null`() = runTest {
        //Given
        coEvery { useCase.getCurrentUser() } returns null

        //When
        viewModel.getCurrentUser()

        //Then
        assert(viewModel.currentUser == null)
    }

    @Test
    fun `when search returns null validate live data state value as null`() = runTest {
        //Given
        coEvery { useCase.getUser("") } returns null

        //When
        viewModel.getUser("")

        //Then
        assert(viewModel.loadStateLiveData.value == LoadState.Error)
    }


}