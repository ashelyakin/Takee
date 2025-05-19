package ru.takee.android.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow

abstract class BaseDao<T : StatsEntity>(
    private val tableName: String,
    private val roomDatabase: RoomDatabase
) {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun add(element: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addAll(entity: List<T>)

    @Delete
    abstract suspend fun delete(entity: T)

    @Transaction
    @RawQuery
    protected abstract fun getAll(query: SupportSQLiteQuery): Flow<List<T>>


    fun getAll(): Flow<List<T>> = getAll(SimpleSQLiteQuery("select * from $tableName"))

}