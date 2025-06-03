package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Welbert on 01/06/2025
 */

@OptIn(ExperimentalCoroutinesApi::class)
class GetUsersUseCaseTest {

    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var useCase: GetUsersUseCaseImpl

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setup() {
        fakeRepository = FakeUserRepository()
        useCase = GetUsersUseCaseImpl(testDispatcher, fakeRepository)
    }

    @Test
    fun `should emit Success with user list`() = runTest(testScheduler) {
        val result = useCase().first()

        assertTrue(result is Result.Success)
        result as Result.Success
        assertEquals(1, result.data.size)
        assertEquals("Welbert", result.data.first().name)
    }

    @Test
    fun `should propagate forceRefresh flag to repository`() = runTest(testScheduler) {
        useCase(forceRefresh = true).first()
        assertTrue(fakeRepository.receivedForceRefresh)
    }

    private class FakeUserRepository : UserRepository {
        var receivedForceRefresh = false

        override suspend fun getUsers(forceRefresh: Boolean): Result<List<User>, Unit> {
            receivedForceRefresh = forceRefresh
            return Result.Success(listOf(User(1, "Welbert", "Moreira", "img.jpg")))
        }
    }
}


