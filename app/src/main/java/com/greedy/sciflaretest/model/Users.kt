package com.greedy.sciflaretest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "users"
)
data class Users(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = 0,
    val name: String?,
    val age: String?,
    val color: String?
) : Serializable