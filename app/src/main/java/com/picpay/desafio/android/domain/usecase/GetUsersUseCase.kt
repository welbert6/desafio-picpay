package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

/**
 * Created by Welbert on 31/05/2025
 */

class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<User> {
        return repository.getUsers(forceRefresh)
    }
}