package com.example.kotlinkeymanagerwithroom.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import androidx.room.Entity as Entity

@Parcelize
@Entity(tableName = "Account")
data class AccountEntity (
    @PrimaryKey(autoGenerate = true) var accountId: Int?,

    @ColumnInfo(name = "title") var name: String,
    @ColumnInfo(name = "userId") var userId: String,
    @ColumnInfo(name = "password") var pwd: String,
    @ColumnInfo(name = "link") var link: String,
    @ColumnInfo(name = "comment") var comment:String,
    @ColumnInfo(name = "image") var image:String,
    @ColumnInfo(name = "timestamp") var timestamp: Long = System.currentTimeMillis()

) : Parcelable
