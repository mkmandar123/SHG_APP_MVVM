package com.digitaldetox.aww.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.digitaldetox.aww.models.*
import com.digitaldetox.aww.persistence.converters.Converters

@Database(
    entities = [AuthToken::class, AccountProperties::class, SubredditRoom::class, UserloanrequestRoom::class, UsersavingrequestRoom::class, HumanloanprofileRoom::class, HumansavingprofileRoom::class ],
    version = 1
)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getSubredditroomDao(): SubredditDao
    abstract fun getHumanloanprofileroomDao(): HumanloanprofileDao
    abstract fun getHumansavingprofileroomDao(): HumansavingprofileDao



    abstract fun getUserloanrequestroomDao(): UserloanrequestDao
    abstract fun getUsersavingrequestroomDao(): UsersavingrequestDao



    companion object {
        val DATABASE_NAME: String = "app_db"
    }


}








