package ru.takee.android.color

import android.graphics.Color

enum class ColorsValue(val text: String, val color: Int) {
    WHITE("Белый", Color.rgb(255, 255, 255)),
    BLACK("Черный", Color.rgb(0, 0, 0)),
    DARK_GRAY("Тёмно-серый", Color.rgb(128, 128, 128)),
    GRAY("Тёмно-серый", Color.rgb(169,169,169)),
    LIGHT_GRAY("Светло-серый", Color.rgb(200,200,200)),
    BROWN_1("Коричневый", Color.rgb(165, 42, 42)),
    BROWN_2("Коричневый", Color.rgb(60, 47, 39)),
    BROWN_3("Коричневый", Color.rgb(92, 64, 51)),
    BROWN_4("Коричневый", Color.rgb(184, 115, 51)),
    BROWN_5("Коричневый", Color.rgb(111, 78, 55)),
    BROWN_6("Коричневый", Color.rgb(42, 22, 2)),
    BEIGE_1("Бежевый", Color.rgb(152, 133, 88)),
    BEIGE_2("Бежевый", Color.rgb(194, 178, 128)),
    BEIGE_3("Бежевый", Color.rgb(193, 154, 107)),
    BEIGE_4("Бежевый", Color.rgb(196, 164, 132)),
    YELLOW_1("Золотистый", Color.rgb(255, 255, 0)),
    YELLOW_2("Золотистый", Color.rgb(255, 234, 0)),
    YELLOW_3("Золотистый", Color.rgb(253, 218, 13)),
    YELLOW_4("Золотистый", Color.rgb(255, 255, 143)),
    YELLOW_5("Золотистый", Color.rgb(228, 208, 10)),
    YELLOW_6("Золотистый", Color.rgb(255, 215, 0)),
    YELLOW_7("Золотистый", Color.rgb(255, 192, 0)),
    YELLOW_8("Золотистый", Color.rgb(252, 245, 95)),
}