package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Welbert on 01/06/2025
 */

class GetUsersUseCaseTest {

    private var receivedForceRefresh = false
    private val mockedUser = User(1, "Welbert", "Moreira", "img.jpg")

    private val fakeRepository = object : UserRepository {
        override suspend fun getUsers(forceRefresh: Boolean): List<User> {
            receivedForceRefresh = forceRefresh
            return listOf(mockedUser)
        }
    }

    @Test
    fun `should return user list from repository`() = runBlocking {
        val useCase = GetUsersUseCase(fakeRepository)

        val result = useCase()

        assertEquals(1, result.size)
        assertEquals("Welbert", result.first().name)
    }

    @Test
    fun `should pass forceRefresh to repository`() = runBlocking {
        val useCase = GetUsersUseCase(fakeRepository)

        useCase(forceRefresh = true)

        assertEquals(true, receivedForceRefresh)
    }
}