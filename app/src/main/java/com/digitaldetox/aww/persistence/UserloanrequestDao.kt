package com.digitaldetox.aww.persistence


import androidx.room.*
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.util.Constants.Companion.PAGINATION_PAGE_SIZE_USERLOANREQUEST

@Dao
interface UserloanrequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userloanrequestRoom: UserloanrequestRoom): Long

    @Delete
    suspend fun deleteUserloanrequest(userloanrequestRoom: UserloanrequestRoom)

    @Query("delete from userloanrequest_table")
    suspend fun deleteAllUserloanrequest()


    @Query(
        """
        UPDATE userloanrequest_table SET title = :title, body = :body, loanamount = :loanamount
        WHERE pk = :pk
        """
    )

    suspend fun updateUserloanrequest(pk: Int, title: String, body: String, loanamount: Int)

    @Query(
        """
        SELECT * FROM userloanrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || loanamount LIKE '%' || :query || '%') 
        ORDER BY title DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchUserloanrequestsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERLOANREQUEST,
        authorsender: String
    ): List<UserloanrequestRoom>

    @Query(
        """
       SELECT * FROM userloanrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || loanamount LIKE '%' || :query || '%') 
        ORDER BY title  ASC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchUserloanrequestsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERLOANREQUEST,
        authorsender: String
    ): List<UserloanrequestRoom>

    @Query(
        """
        SELECT * FROM userloanrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || loanamount LIKE '%' || :query || '%') 
        ORDER BY title DESC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchUserloanrequestsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERLOANREQUEST,
        authorsender: String
    ): List<UserloanrequestRoom>

    @Query(
        """
       SELECT * FROM userloanrequest_table 
        WHERE authorsender IS :authorsender AND (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || loanamount LIKE '%' || :query || '%') 
        ORDER BY title  ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchUserloanrequestsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_USERLOANREQUEST,
        authorsender: String
    ): List<UserloanrequestRoom>


    /* UPLOAD */

    @Query("SELECT * FROM userloanrequest_table WHERE title = :key LIMIT 1")
    fun findFirstByKeyOnceUp(key: String): UserloanrequestRoom?

    @Query("SELECT * FROM userloanrequest_table WHERE album_id IS :albumId")
    suspend fun findAllByAlbumIdUp(albumId: String): List<UserloanrequestRoom>

    @Query("SELECT * FROM userloanrequest_table WHERE queued_for_upload = 1")
      fun findAllPendingUploads(): List<UserloanrequestRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUp(image: UserloanrequestRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUp(images: List<UserloanrequestRoom>)

    @Update
    fun updateUp(image: UserloanrequestRoom)

    @Delete
    fun deleteUp(image: UserloanrequestRoom)


    @Query("DELETE FROM userloanrequest_table WHERE pk NOT IN (:lstIDUsers)")
    suspend fun deleteOldUserloanrequests(lstIDUsers: Int)


    @Query("delete from userloanrequest_table")
    fun deleteAllUserloanrequests()

}






