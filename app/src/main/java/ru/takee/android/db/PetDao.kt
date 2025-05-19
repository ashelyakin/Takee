package ru.takee.android.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import ru.takee.android.db.TakeeDatabase.Companion.PET_TABLE

@Dao
abstract class PetDao(roomDatabase: RoomDatabase){

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(element: PetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addAll(entity: List<PetEntity>)

    @Delete
    abstract suspend fun delete(entity: PetEntity)

    @Transaction
    @RawQuery(observedEntities = [PetEntity::class])
    protected abstract fun getAll(query: SupportSQLiteQuery): Flow<List<PetEntity>>


    fun getAll(): Flow<List<PetEntity>> = getAll(SimpleSQLiteQuery("select * from $PET_TABLE"))

}