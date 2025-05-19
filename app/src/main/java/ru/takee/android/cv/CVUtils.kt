package ru.takee.android.cv

object CVUtils {

    // Вспомогательная функция для транспонирования [9][8400] -> [8400][9]
    fun transposeOutput(output: Array<FloatArray>): Array<FloatArray> {
        val rows = output.size       // 9
        val cols = output[0].size    // 8400

        return Array(cols) { j ->
            FloatArray(rows) { i ->
                output[i][j]
            }
        }
    }

}