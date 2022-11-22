package com.greedy.sciflaretest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.greedy.sciflaretest.model.Users

@Database(
    entities = [Users::class],
    version = 1
)


abstract class UsersDatabase: RoomDatabase()  {

    abstract fun getUsersDao(): UsersDao

    companion object {
        @Volatile
        private var instance: UsersDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){

            instance?: createDatabase(context).also{ instance = it }

        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UsersDatabase::class.java,
            "users_db.db"
        ).build()


    }

}