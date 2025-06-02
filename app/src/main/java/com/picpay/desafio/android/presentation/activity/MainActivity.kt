package com.picpay.desafio.android.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.presentation.extensions.hide
import com.picpay.desafio.android.presentation.extensions.show
import com.picpay.desafio.android.presentation.userlist.UserListAdapter
import com.picpay.desafio.android.presentation.userlist.UserListViewModel
import com.picpay.desafio.android.presentation.userlist.UserUiState
import com.picpay.desafio.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: UserListViewModel by viewModel()
    private val adapter = UserListAdapter()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UserUiState.Loading -> showLoad()
                        is UserUiState.Success -> showData(uiState.users)
                        is UserUiState.Error -> showError(uiState.message)

                    }
                }
            }
        }
    }

    private fun showData(listUsers: List<User>) {
        binding.progressBar.hide()
        binding.recyclerView.show()
        adapter.users = listUsers
    }

    private fun showError(message: String) {
        binding.progressBar.hide()
        binding.recyclerView.hide()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoad() {
        binding.progressBar.show()
        binding.recyclerView.hide()
    }
}