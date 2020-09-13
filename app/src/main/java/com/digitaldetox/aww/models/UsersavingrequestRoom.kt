package com.digitaldetox.aww.models


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "usersavingrequest_table")
data class UsersavingrequestRoom(

    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val pk: Int,

    @ColumnInfo(name = "savingamount")
    val savingamount: Int,

    @ColumnInfo(name = "title")
    val title: String,


    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "authorsender")
    val authorsender: String,

    @ColumnInfo(name = "subreddit")
    val subreddit: String,


    @ColumnInfo(name = "album_id") var albumId: String?,
    @ColumnInfo(name = "queued_for_upload") var queuedForUpload: Boolean = false,
    @ColumnInfo(name = "uploaded_percentage") var uploadedPercentage: Int? = null,
    @ColumnInfo(name = "upload_id") var uploadId: String? = null,
    @ColumnInfo(name = "upload_error") var uploadError: String? = null


) : Parcelable {
    override fun toString(): String {
        return "UsersavingrequestRoom(pk=$pk, savingamount=$savingamount, title='$title', body='$body', authorsender='$authorsender', subreddit='$subreddit', albumId=$albumId, queuedForUpload=$queuedForUpload, uploadedPercentage=$uploadedPercentage, uploadId=$uploadId, uploadError=$uploadError)"
    }
}

