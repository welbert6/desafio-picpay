package com.picpay.desafio.android.domain.util

/**
 * Created by Welbert on 02/06/2025
 */

sealed class Result<out A, out B> {
    data class Success<A>(val data: A) : Result<A,Nothing>()
    data class Error<B>(val data: B) : Result<Nothing,B>()
}