package ru.takee.android.models

enum class PetCategory(val id: Int){
    GERMAN_SHEPHERD(1),
    GOLDEN_RETRIEVER(2),
    DACHSHUND(3),
    CAT(4),
    DOG(5),
    NONE(6),
    RABBIT(7),
    TURTLE(8);

    override fun toString(): String {
        return when(this){
            GERMAN_SHEPHERD -> "Немецкие овчарки"
            GOLDEN_RETRIEVER -> "Золотистые ретриверы"
            DACHSHUND -> "Таксы"
            CAT -> "Кошки"
            DOG -> "Собаки"
            NONE -> "Не указана"
            RABBIT -> "Кролики"
            TURTLE -> "Черепахи"
        }
    }

    fun isDogCategory(): Boolean = this == DOG || this == GERMAN_SHEPHERD || this == GOLDEN_RETRIEVER || this == DACHSHUND
}