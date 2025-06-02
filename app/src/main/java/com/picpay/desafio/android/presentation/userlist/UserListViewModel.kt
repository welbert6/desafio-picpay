package com.picpay.desafio.android.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by Welbert on 31/05/2025
 */

class UserListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        fetchUsers()
    }

    fun fetchUsers(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            try {
                _uiState.value = UserUiState.Loading
                val users = getUsersUseCase(forceRefresh)
                _uiState.value = UserUiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UserUiState.Error("Erro ao carregar usuários")
            }
        }
    }
}