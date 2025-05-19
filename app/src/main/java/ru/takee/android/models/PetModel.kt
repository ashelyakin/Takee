package ru.takee.android.models

import ru.takee.android.db.PetEntity

data class PetModel(
    val id: Int = 0,
    val name: String = "Name",
    val description: String = "",
    val category: Int = PetCategory.NONE.id,
    val imgPath: String? = null,
    val color: String = "",
    val timestamp: Long = 0
)

fun PetModel.toPetEntity() = PetEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    imgPath = this.imgPath,
    category = this.category,
    color = color,
    timestamp = System.currentTimeMillis()
)

fun PetEntity.toModel() = PetModel(
    id = id,
    name = this.name,
    description = this.description,
    category = this.category,
    imgPath = this.imgPath,
    color = color,
    timestamp = timestamp
)