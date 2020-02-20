package com.example.kotlinkeymanagerwithroom

import androidx.lifecycle.LiveData
import com.example.kotlinkeymanagerwithroom.database.AccountDao
import com.example.kotlinkeymanagerwithroom.database.AccountEntity

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AccountRepository(private val accountDao: AccountDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allAccounts: LiveData<List<AccountEntity>> = accountDao.getAll()

    suspend fun insert(account: AccountEntity) {
        accountDao.insert(account)
    }

    suspend fun update(account: AccountEntity) {
        accountDao.update(account)
    }

    suspend fun delete(account: AccountEntity) {
        accountDao.delete(account)
    }
}