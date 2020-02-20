package com.example.kotlinkeymanagerwithroom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinkeymanagerwithroom.database.AccountDatabase
import com.example.kotlinkeymanagerwithroom.database.AccountEntity
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application){

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: AccountRepository
    // LiveData gives us updated words when they change.
    val allAccounts: LiveData<List<AccountEntity>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val accountDao = AccountDatabase.getDatabase(application, viewModelScope).accountDao()
        repository = AccountRepository(accountDao)
        allAccounts = repository.allAccounts
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(account: AccountEntity) = viewModelScope.launch {
        repository.insert(account)
    }

    fun update(account: AccountEntity) = viewModelScope.launch {
        repository.update(account)
    }

    fun delete(account: AccountEntity) = viewModelScope.launch {
        repository.delete(account)
    }
}
