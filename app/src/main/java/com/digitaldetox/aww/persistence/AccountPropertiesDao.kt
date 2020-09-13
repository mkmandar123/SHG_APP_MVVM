package com.digitaldetox.aww.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digitaldetox.aww.models.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE username = :username")
    suspend fun searchByUsername(username: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    suspend fun searchByPk(pk: Int): AccountProperties

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, username = :username, first_name = :first_name, last_name = :last_name, location = :location, age = :age, savingtarget = :savingtarget WHERE pk = :pk")
    suspend fun updateAccountProperties(pk: Int, email: String, username: String, first_name: String, last_name: String, location: String, age: Int, savingtarget: Int)
}












