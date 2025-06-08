package ru.takee.android.models

enum class PetCategory(val id: Int){
    DOG(0),
    GERMAN_SHEPHERD(1),
    GOLDEN_RETRIEVER(2),
    DACHSHUND(3),
    CAT(4),
    NONE(5),
    RABBIT(6),
    TURTLE(7);

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