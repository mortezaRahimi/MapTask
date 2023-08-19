package com.example.holloomap.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random


@Entity(tableName = "destinationTable")
data class DestinationInfo(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "lat") val lat: String,
    @ColumnInfo(name = "lon") val lon: String,
)