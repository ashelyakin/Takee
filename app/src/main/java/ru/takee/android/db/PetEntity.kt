package ru.takee.android.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.takee.android.db.TakeeDatabase.Companion.PET_TABLE

@Entity(tableName = PET_TABLE)
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    override val id : Int,
    val name: String,
    val description: String,
    val imgPath: String?,
    val category: Int,
    val color: String,
    override val timestamp: Long
): StatsEntity
