package com.example.assignmentapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
class UserDataEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String
)