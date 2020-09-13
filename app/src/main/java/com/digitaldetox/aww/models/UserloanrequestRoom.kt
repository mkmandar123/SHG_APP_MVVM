package com.digitaldetox.aww.models


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "userloanrequest_table")
data class UserloanrequestRoom(

    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    val pk: Int,

    @ColumnInfo(name = "loanamount")
    val loanamount: Int,

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
        return "UserloanrequestRoom(pk=$pk, loanamount=$loanamount, title='$title', body='$body', authorsender='$authorsender', subreddit='$subreddit', albumId=$albumId, queuedForUpload=$queuedForUpload, uploadedPercentage=$uploadedPercentage, uploadId=$uploadId, uploadError=$uploadError)"
    }
}

