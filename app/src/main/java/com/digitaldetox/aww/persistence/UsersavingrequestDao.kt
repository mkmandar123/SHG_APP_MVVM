package com.digitaldetox.aww.persistence


import androidx.room.*
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.util.Constants.Companion.PAGINATION_PAGE_SIZE_USERSAVINGREQUEST

@Dao
interface UsersavingrequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usersavingrequestRoom: UsersavingrequestRoom): Long

    @Delete
    suspend fun deleteUsersavingrequest(usersavingrequestRoom: UsersavingrequestRoom)

    @Query("delete from usersavingrequest_table")
    suspend fun deleteAllUsersavingrequest()


    @Query(
        """
        UPDATE usersavingrequest_table SET title = :title, body = :body, savingamount = :savingamount
        WHERE pk = :pk
        """
    )

    suspend fun updateUsersavingrequest(pk: Int, title: String, body: String, savingamount: Int)

    @Query(
        """
        SELECT * FROM usersavingrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || savingamount LIKE '%' || :query || '%') 
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchUsersavingrequestsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERSAVINGREQUEST,
        authorsender: String
    ): List<UsersavingrequestRoom>

    @Query(
        """
       SELECT * FROM usersavingrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || savingamount LIKE '%' || :query || '%') 
        ORDER BY title  ASC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchUsersavingrequestsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERSAVINGREQUEST,
        authorsender: String
    ): List<UsersavingrequestRoom>

    @Query(
        """
         SELECT * FROM usersavingrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || savingamount LIKE '%' || :query || '%') 
        ORDER BY title DESC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchUsersavingrequestsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERSAVINGREQUEST,
        authorsender: String
    ): List<UsersavingrequestRoom>

    @Query(
        """
        SELECT * FROM usersavingrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || savingamount LIKE '%' || :query || '%')  
        ORDER BY title  ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchUsersavingrequestsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERSAVINGREQUEST,
        authorsender: String
    ): List<UsersavingrequestRoom>


    /* UPLOAD */

    @Query("SELECT * FROM usersavingrequest_table WHERE title = :key LIMIT 1")
    fun findFirstByKeyOnceUp(key: String): UsersavingrequestRoom?

    @Query("SELECT * FROM usersavingrequest_table WHERE album_id IS :albumId")
    suspend fun findAllByAlbumIdUp(albumId: String): List<UsersavingrequestRoom>

    @Query("SELECT * FROM usersavingrequest_table WHERE queued_for_upload = 1")
      fun findAllPendingUploads(): List<UsersavingrequestRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUp(image: UsersavingrequestRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUp(images: List<UsersavingrequestRoom>)

    @Update
    fun updateUp(image: UsersavingrequestRoom)

    @Delete
    fun deleteUp(image: UsersavingrequestRoom)


    @Query("DELETE FROM usersavingrequest_table WHERE pk NOT IN (:lstIDUsers)")
    suspend fun deleteOldUsersavingrequests(lstIDUsers: Int)


    @Query("delete from usersavingrequest_table")
    fun deleteAllUsersavingrequests()

}






