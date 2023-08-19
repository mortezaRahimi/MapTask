package com.example.holloomap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.holloomap.data.local.entity.DestinationInfo

@Database(
    entities = [DestinationInfo::class],
    version = 1
)
abstract class MapDatabase : RoomDatabase() {

    abstract fun dao(): MapDao
}