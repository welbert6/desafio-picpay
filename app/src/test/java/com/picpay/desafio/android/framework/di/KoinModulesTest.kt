package com.picpay.desafio.android.framework.di

import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.userlist.UserListViewModel
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * Created by Welbert on 01/06/2025
 */


class KoinModulesTest : KoinTest {

    private val useCase: GetUsersUseCase by inject()
    private val repository: UserRepository by inject()

    @Before
    fun setup() {
        startKoin {
            modules(appModules)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }



    @Test
    fun `should inject GetUsersUseCase`() {
        assertNotNull(useCase)
    }

    @Test
    fun `should inject UserRepository`() {
        assertNotNull(repository)
    }
}