package com.example.holloomap.data.local

import androidx.room.*
import com.example.holloomap.data.local.entity.DestinationInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDestination(destinationInfo: DestinationInfo)

    @Query(
        "SELECT * FROM destinationTable"
    )
    fun getAllDestinations(): Flow<List<DestinationInfo>>
}