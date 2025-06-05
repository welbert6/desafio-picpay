package com.picpay.desafio.android.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repository.UserRepositoryImpl
import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
import retrofit2.converter.gson.GsonConverterFactory
import com.picpay.desafio.android.presentation.viewmodel.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin


/**
 * Created by Welbert on 01/06/2025
 */

val testModules =  module {
    single {
        Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(PicPayService::class.java) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory<GetUsersUseCase> { GetUsersUseCaseImpl(repository =  get()) }
    viewModel { UserListViewModel(get()) }
}

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TestApplication)
            modules(
                testModules
            )
        }
    }
}
