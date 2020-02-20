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
