package com.picpay.desafio.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.util.Result
import com.picpay.desafio.android.presentation.state.UserUiState
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

    fun fetchUsers(forceRefresh: Boolean = false) {
        _uiState.value = UserUiState.Loading
        viewModelScope.launch {
            getUsersUseCase(forceRefresh).collect {
                when (it) {
                    is Result.Error ->  _uiState.value =
                        UserUiState.Error("Erro ao carregar usuários" )

                    is Result.Success -> _uiState.value = UserUiState.Success(it.data)
                }
            }
        }
    }
}