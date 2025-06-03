package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Welbert on 31/05/2025
 */
class UserRepositoryImpl(
    private val api: PicPayService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    private var cache: List<User> = emptyList()


    override suspend fun getUsers(forceRefresh: Boolean): Result<List<User>, Unit> =
        withContext(coroutineDispatcher) {
            try {
                if (forceRefresh || cache.isEmpty()) {
                    cache = api.getUsers().map { it.toDomain() }
                }
                Result.Success(cache)
            } catch (e: Exception) {
                Result.Error(Unit)
            }
        }

}