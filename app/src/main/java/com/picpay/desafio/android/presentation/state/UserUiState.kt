package com.picpay.desafio.android.presentation.state

import com.picpay.desafio.android.domain.model.User

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<User>) : UserUiState()
    data class Error(val message: String) : UserUiState()
}