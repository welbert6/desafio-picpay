package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.util.Result

/**
 * Created by Welbert on 31/05/2025
 */

interface UserRepository {
    suspend fun getUsers(forceRefresh: Boolean = false): Result<List<User>, Unit>
}