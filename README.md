# Room

https://developer.android.com/training/data-storage/room/index.html

Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.

There are 3 major components in Room:

Database: Contains the database holder and serves as the main access point for the underlying connection to your app's persisted, relational data.

The class that's annotated with @Database should satisfy the following conditions:

Be an abstract class that extends RoomDatabase.
Include the list of entities associated with the database within the annotation.
Contain an abstract method that has 0 arguments and returns the class that is annotated with @Dao.
At runtime, you can acquire an instance of Database by calling Room.databaseBuilder() or Room.inMemoryDatabaseBuilder().

Entity: Represents a table within the database.

DAO: Contains the methods used for accessing the database.

The app uses the Room database to get the data access objects, or DAOs, associated with that database. The app then uses each DAO to get entities from the database and save any changes to those entities back to the database. Finally, the app uses an entity to get and set values that correspond to table columns within the database.


![image](https://user-images.githubusercontent.com/53125879/74974220-af2a1200-53d9-11ea-9503-9cf4546cea3a.png)




# Project  

source : myungeunpark/KotlinRoom/app

### Source Architecture

![image](https://user-images.githubusercontent.com/53125879/74973636-9f5dfe00-53d8-11ea-938f-eed9508bf883.png)


### application screens
  
  
![image](https://user-images.githubusercontent.com/53125879/74973429-41311b00-53d8-11ea-8296-282d539fd241.png)



### How to implement

1. dependency update (gradle files)

build gradle of module
```
// Room components
    implementation "androidx.room:room-runtime:$rootProject.roomVersion"
    implementation "androidx.room:room-ktx:$rootProject.roomVersion"
    kapt "androidx.room:room-compiler:$rootProject.roomVersion"
    androidTestImplementation "androidx.room:room-testing:$rootProject.roomVersion"
 

    // Lifecycle components
    implementation "androidx.lifecycle:lifecycle-extensions:$rootProject.archLifecycleVersion"
    kapt "androidx.lifecycle:lifecycle-compiler:$rootProject.archLifecycleVersion"
    androidTestImplementation "androidx.arch.core:core-testing:$rootProject.androidxArchVersion"

    // ViewModel Kotlin support
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$rootProject.archLifecycleVersion"

    // Coroutines
    api "org.jetbrains.kotlinx:kotlinx-coroutines-android:$rootProject.coroutines"
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    
 ```

build gradle of project

```

ext {
    roomVersion = '2.2.1'
    archLifecycleVersion = '2.2.0-rc02'
    androidxArchVersion = '2.1.0'
    coreTestingVersion = "2.1.0"
    coroutines = '1.3.2'
    materialVersion = "1.0.0"
}

```

2. Create the data entity 

```
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
```

3. Create DAO with LiveData

```
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
```

4. Create Database
```
@Database(entities = [AccountEntity::class], version = 1, exportSchema = false)
abstract class AccountDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    ...
    
    companion object{

        @Volatile
        private var INSTANCE: AccountDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AccountDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountDatabase::class.java,
                    "account_database"
                )
                    .addCallback(AccountDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

```
5. Create Repository

```
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
```

6. Create ViewModel 

```
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
```

7. Call the ViewModel in the activity

```
    private lateinit var viewModel: AccountViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          ...
          
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.allAccounts.observe(this, Observer { accounts ->

            setRecyclerViewData(accounts.toList())
        })

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = AccountListAdapter(this)
      
         ...        

```
