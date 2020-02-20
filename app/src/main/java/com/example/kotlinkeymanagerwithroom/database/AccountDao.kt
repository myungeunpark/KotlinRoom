package com.example.kotlinkeymanagerwithroom.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AccountDao {

    @Query("SELECT * FROM Account ORDER BY timestamp ASC")
    fun getAll(): LiveData<List<AccountEntity>>

    @Insert
    suspend fun insert(account: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)
}