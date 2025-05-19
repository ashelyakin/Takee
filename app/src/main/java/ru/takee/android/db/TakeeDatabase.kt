package ru.takee.android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    PetEntity::class
], version = 1)
abstract class TakeeDatabase: RoomDatabase()  {

    abstract val petDao: PetDao

    companion object{
        @Volatile
        private var INSTANCE: TakeeDatabase? = null

        fun getInstance(context: Context): TakeeDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TakeeDatabase::class.java,
                        "takee_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        const val PET_TABLE = "PET_TABLE"
    }
}