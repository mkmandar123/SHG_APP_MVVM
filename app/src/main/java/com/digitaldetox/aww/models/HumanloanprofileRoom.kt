package com.digitaldetox.aww.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "humanloanprofile_table")
data class HumanloanprofileRoom(

    @ColumnInfo(name = "pk")
    @PrimaryKey(autoGenerate = false)
    var pk: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "album_id") var albumId: String?,
    @ColumnInfo(name = "queued_for_upload") var queuedForUpload: Boolean = false,
    @ColumnInfo(name = "uploaded_percentage") var uploadedPercentage: Int? = null,
    @ColumnInfo(name = "upload_id") var uploadId: String? = null,
    @ColumnInfo(name = "upload_error") var uploadError: String? = null

) : Parcelable {
     override fun toString(): String {
        return "HumanloanprofileRoom(pk=$pk, title='$title', albumId=$albumId, queuedForUpload=$queuedForUpload, uploadedPercentage=$uploadedPercentage, uploadId=$uploadId, uploadError=$uploadError)"
    }
}