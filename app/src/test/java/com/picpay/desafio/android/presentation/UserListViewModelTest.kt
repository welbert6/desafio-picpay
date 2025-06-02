package com.picpay.desafio.android.presentation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.userlist.UserListViewModel
import com.picpay.desafio.android.presentation.userlist.UserUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Created by Welbert on 01/06/2025
 */


@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var viewModel: UserListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUsersUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUsers should emit Success on success`() = testScope.runTest {
        val userList = listOf(User(1, "Welbert", "Moreira", "img.jpg"))
        coEvery { getUsersUseCase(any()) } returns userList

        viewModel = UserListViewModel(getUsersUseCase)

        advanceUntilIdle()

        val result = viewModel.uiState.value
        assert(result is UserUiState.Success)
        assertEquals(userList, (result as UserUiState.Success).users)
    }

    @Test
    fun `fetchUsers should emit Error on exception`() = testScope.runTest {
        coEvery { getUsersUseCase(any()) } throws RuntimeException("Erro")

        viewModel = UserListViewModel(getUsersUseCase)

        advanceUntilIdle()

        val result = viewModel.uiState.value
        assert(result is UserUiState.Error)
        assertEquals("Erro ao carregar usuários", (result as UserUiState.Error).message)
    }
}