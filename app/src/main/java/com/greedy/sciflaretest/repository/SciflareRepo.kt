package com.greedy.sciflaretest.repository

import com.greedy.sciflaretest.api.RetrofitInstance
import com.greedy.sciflaretest.db.UsersDatabase
import com.greedy.sciflaretest.model.Users

class SciflareRepo(val db: UsersDatabase) {

    suspend fun getUsersfromrepo() =
        RetrofitInstance.api.getUsersfromapi()

    suspend fun upsert(users: List<Users>) = db.getUsersDao().insert(users)

    fun getSavedUsers() = db.getUsersDao().getAllUsers()

    suspend fun deleteuser() = db.getUsersDao().deleteAll()
}