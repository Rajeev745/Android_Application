package com.example.assignmentapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserDataEntity::class],
    version = 4,
    exportSchema = false
)
abstract class UserDataDatabase : RoomDatabase() {

    abstract fun userDataTableDao(): UserDataTableDao
}