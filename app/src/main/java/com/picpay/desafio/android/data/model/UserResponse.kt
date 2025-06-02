package com.picpay.desafio.android.data.model

import com.google.gson.annotations.SerializedName
import com.picpay.desafio.android.domain.model.User

/**
 * Created by Welbert on 31/05/2025
 */

data class UserResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("username")
    val username: String = "",
    @SerializedName("img")
    val img: String = ""
) {
    fun toDomain() =  User(
        id = this.id,
        name = this.name,
        username = this.username,
        img = this.img
    )

}