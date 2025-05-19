package ru.takee.android.utils

import com.google.gson.Gson
import ru.takee.android.models.PetCategory

fun Int.toPetCategory(): PetCategory = PetCategory.entries.find { it.id == this } ?: PetCategory.NONE

fun Any?.toJson(): String{
    return try {
        Gson().toJson(this)
    } catch (e: Exception){
        ""
    }
}

inline fun <reified T> String?.fromJson():T?{
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception){
        null
    }
}