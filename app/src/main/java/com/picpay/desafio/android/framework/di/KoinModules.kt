package com.picpay.desafio.android.framework.di

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
import com.picpay.desafio.android.presentation.viewmodel.UserListViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Welbert on 31/05/2025
 */




val networkModule = module {
    single {
        OkHttpClient.Builder().build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(PicPayService::class.java) }
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory<GetUsersUseCase> { GetUsersUseCaseImpl(repository =  get()) }
}

val viewModelModule = module {
    viewModel { UserListViewModel(get()) }
}

val appModules = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)