package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


/**
 * Created by Welbert on 01/06/2025
 */


@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private lateinit var fakeApi: FakeApi
    private lateinit var repository: UserRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    private val mockedList = listOf(UserResponse(1, "Welbert", "Moreira", "img.jpg"))

    @Before
    fun setup() {
        fakeApi = FakeApi(mockedList)
        repository = UserRepositoryImpl(fakeApi, testDispatcher)
    }

    @Test
    fun `should fetch from API and return success`() = runTest(testDispatcher) {
        val result = repository.getUsers()

        assertTrue(result is Result.Success)
        result as Result.Success
        assertEquals(1, result.data.size)
        assertEquals("Welbert", result.data[0].name)
        assertEquals(1, fakeApi.callCount)
    }

    @Test
    fun `should return cached result on second call`() = runTest(testDispatcher)  {
        repository.getUsers()
        val result = repository.getUsers()

        assertTrue(result is Result.Success)
        result as Result.Success
        assertEquals(1, result.data.size)
        assertEquals(1, fakeApi.callCount) // só uma chamada
    }

    @Test
    fun `should refresh data when forceRefresh is true`() = runTest(testDispatcher)  {
        repository.getUsers()
        val result = repository.getUsers(forceRefresh = true)

        assertTrue(result is Result.Success)
        assertEquals(2, fakeApi.callCount)
    }

    @Test
    fun `should return error when API throws`() = runTest(testDispatcher)  {
        fakeApi.shouldThrow = true
        val result = repository.getUsers(forceRefresh = true)

        assertTrue(result is Result.Error)
    }

    private class FakeApi(
        private val response: List<UserResponse>
    ) : PicPayService {

        var callCount = 0
        var shouldThrow = false

        override suspend fun getUsers(): List<UserResponse> {
            callCount++
            if (shouldThrow) throw RuntimeException("API Failure")
            return response
        }
    }
}