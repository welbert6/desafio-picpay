package com.picpay.desafio.android.presentation

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.util.Result
import com.picpay.desafio.android.presentation.viewmodel.UserListViewModel
import com.picpay.desafio.android.presentation.state.UserUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Welbert on 02/06/2025
 */

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private lateinit var viewModel: UserListViewModel
    private lateinit var fakeUseCase: FakeGetUsersUseCase
    private lateinit var testScope: TestScope

    private val testDispatcher = StandardTestDispatcher()
    private val mockedUser = User(1, "Welbert", "Moreira", "img.jpg")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeGetUsersUseCase(Result.Success(listOf(mockedUser)))
        testScope = TestScope(testDispatcher)
        viewModel = UserListViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Loading then Success`() = testScope.runTest {
        val emissions = mutableListOf<UserUiState>()

        val job = launch {
            viewModel.uiState.collect {
                emissions.add(it)
                if (it is UserUiState.Success)
                    cancel()
            }
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        assertEquals(UserUiState.Loading, emissions[0])
        assertTrue(emissions.last() is UserUiState.Success)

        val success = emissions.last() as UserUiState.Success
        assertEquals(1, success.users.size)
        assertEquals("Welbert", success.users[0].name)

        job.cancel()
    }

    @Test
    fun `should emit Loading then Error`() = testScope.runTest {
        fakeUseCase.result = Result.Error(Unit)
        val emissions = mutableListOf<UserUiState>()

        val job = launch {
            viewModel.uiState.collect {
                emissions.add(it)
                if (it is UserUiState.Error) cancel()
            }
        }

        viewModel.fetchUsers()
        advanceUntilIdle()

        assertEquals(UserUiState.Loading, emissions[0])
        assertTrue(emissions.last() is UserUiState.Error)

        val error = emissions.last() as UserUiState.Error
        assertEquals("Erro ao carregar usuários", error.message)

        job.cancel()
    }

    // Fake UseCase
    private class FakeGetUsersUseCase(var result: Result<List<User>, Unit>) : GetUsersUseCase {
        override fun invoke(forceRefresh: Boolean): Flow<Result<List<User>, Unit>> = flow {
            emit(result)
        }
    }
}
