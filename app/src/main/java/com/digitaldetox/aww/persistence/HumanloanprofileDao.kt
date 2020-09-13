package com.digitaldetox.aww.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digitaldetox.aww.models.HumanloanprofileRoom

@Dao
interface HumanloanprofileDao {

    @Query(
        """SELECT * FROM humanloanprofile_table"""
    )
    suspend fun searchHumanloanprofileroomsOrderByDateDESC(
    ): List<HumanloanprofileRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(humanloanprofileRoom: HumanloanprofileRoom): Long

}






