package com.greedy.sciflaretest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.greedy.sciflaretest.model.Users
import java.util.concurrent.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<Users>)

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<Users>>

    @Query("DELETE FROM users")
    suspend fun deleteAll(): Int

}