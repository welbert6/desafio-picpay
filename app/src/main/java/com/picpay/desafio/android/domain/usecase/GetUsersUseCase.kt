package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Created by Welbert on 31/05/2025
 */

interface GetUsersUseCase{
    operator fun invoke(forceRefresh: Boolean = false): Flow<Result<List<User>,Unit>>
}

class GetUsersUseCaseImpl(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val repository: UserRepository
) : GetUsersUseCase{

    override operator fun invoke(forceRefresh: Boolean): Flow<Result<List<User>,Unit>> {
        return flow {
            emit(repository.getUsers(forceRefresh))
        }.flowOn(coroutineDispatcher)
    }
}