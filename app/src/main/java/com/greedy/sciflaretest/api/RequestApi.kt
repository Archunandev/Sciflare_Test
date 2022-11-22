package com.greedy.sciflaretest.api


import com.greedy.sciflaretest.model.Users
import retrofit2.Response
import retrofit2.http.GET

interface RequestApi {

    @GET("1e7fb2100a314b2c96a4950114efa16b/unicorns")
    suspend fun getUsersfromapi(): Response<List<Users>>


}