package com.digitaldetox.aww.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digitaldetox.aww.models.HumansavingprofileRoom

@Dao
interface HumansavingprofileDao {

    @Query(
        """SELECT * FROM humansavingprofile_table"""
    )
    suspend fun searchHumansavingprofileroomsOrderByDateDESC(
    ): List<HumansavingprofileRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(humansavingprofileRoom: HumansavingprofileRoom): Long

}






