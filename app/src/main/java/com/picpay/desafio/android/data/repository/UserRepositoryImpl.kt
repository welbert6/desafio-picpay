package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository

/**
 * Created by Welbert on 31/05/2025
 */
class UserRepositoryImpl(
    private val api: PicPayService
) : UserRepository {

    private var cache: List<User> = emptyList()

    override suspend fun getUsers(forceRefresh: Boolean): List<User> {
        if (forceRefresh || cache.isEmpty()) {
            cache = api.getUsers().map { it.toDomain() }
        }
        return cache
    }
}