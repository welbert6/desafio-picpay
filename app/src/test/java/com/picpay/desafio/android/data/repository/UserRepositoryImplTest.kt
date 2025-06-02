package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.model.UserResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Welbert on 01/06/2025
 */

class UserRepositoryImplTest {

    private var callCount = 0

    private val mockedList = listOf(UserResponse(1, "Welbert", "Moreira", "img.jpg"))

    private val fakeApi = object : PicPayService {
        override suspend fun getUsers(): List<UserResponse> {
            callCount++
            return mockedList
        }
    }

    @Test
    fun `should fetch from API and return result`() = runBlocking {
        val repository = UserRepositoryImpl(fakeApi)

        val result = repository.getUsers()

        assertEquals(1, result.size)
        assertEquals("Welbert", result[0].name)
        assertEquals(1, callCount)
    }

    @Test
    fun `should return cached result on second call`() = runBlocking {
        val repository = UserRepositoryImpl(fakeApi)

        repository.getUsers()
        val result = repository.getUsers()

        assertEquals(1, result.size)
        assertEquals("Welbert", result[0].name)
        assertEquals(1, callCount)
    }

    @Test
    fun `should refresh data when forceRefresh is true`() = runBlocking {
        val repository = UserRepositoryImpl(fakeApi)

        repository.getUsers()
        val refreshed = repository.getUsers(forceRefresh = true)

        assertEquals(1, refreshed.size)
        assertEquals("Welbert", refreshed[0].name)
        assertEquals(2, callCount)
    }
}