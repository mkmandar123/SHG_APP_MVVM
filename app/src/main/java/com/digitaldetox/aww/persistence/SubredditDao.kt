package com.digitaldetox.aww.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.digitaldetox.aww.models.SubredditRoom
import com.digitaldetox.aww.util.Constants.Companion.PAGINATION_PAGE_SIZE_SUBREDDIT

@Dao
interface SubredditDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subredditRoom: SubredditRoom): Long

    @Delete
    suspend fun deleteSubredditroom(subredditRoom: SubredditRoom)


    @Query(
        """
        UPDATE subreddit_table SET title = :title, description = :description   
        WHERE pk = :pk
        """
    )

    suspend fun updateSubredditroom(pk: Int, title: String, description: String)

    @Query(
        """
        SELECT * FROM subreddit_table 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
          
        LIMIT (:page * :pageSize)
        """
    )
    suspend fun getAllSubredditrooms(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_SUBREDDIT
    ): List<SubredditRoom>

    @Query(
        """
        SELECT * FROM subreddit_table 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
          
        ORDER BY pk DESC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchSubredditroomsOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_SUBREDDIT
    ): List<SubredditRoom>

    @Query(
        """
        SELECT * FROM subreddit_table 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
          
        ORDER BY pk  ASC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchSubredditroomsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_SUBREDDIT
    ): List<SubredditRoom>

    @Query(
        """
        SELECT * FROM subreddit_table 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
          
        ORDER BY title DESC LIMIT (:page * :pageSize)"""
    )
    suspend fun searchSubredditroomsOrderByAuthorDESC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_SUBREDDIT
    ): List<SubredditRoom>

    @Query(
        """
        SELECT * FROM subreddit_table 
        WHERE title LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
          
        ORDER BY title  ASC LIMIT (:page * :pageSize)
        """
    )
    suspend fun searchSubredditroomsOrderByAuthorASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE_SUBREDDIT
    ): List<SubredditRoom>



    @Query("SELECT * FROM subreddit_table WHERE title = :key LIMIT 1")
    abstract fun findFirstByKeyOnceUp(key: String): SubredditRoom?

    @Query("SELECT * FROM subreddit_table WHERE album_id IS :albumId")
    abstract fun findAllByAlbumIdUp(albumId: String): LiveData<List<SubredditRoom>>



    @Query("SELECT * FROM subreddit_table WHERE queued_for_upload = 1")
    abstract fun findAllPendingUploads(): List<SubredditRoom>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUp(image: SubredditRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllUp(images: List<SubredditRoom>)

    @Update
    abstract fun updateUp(image: SubredditRoom)

    @Delete
    abstract fun deleteUp(image: SubredditRoom)

    @Query("delete from subreddit_table")
    fun deleteAllSubreddits()

}






