package ru.takee.android.cv

import android.content.Context
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import ru.takee.android.models.PetCategory
import ru.takee.android.models.PetDetectionClassificationResult
import ru.takee.android.utils.toPetCategory
import java.nio.ByteBuffer

class CVManager(private val context: Context) {

    companion object{
        private const val TAG = "CVManager"
        private const val MODEL_PATH: String = "best_float32.tflite"
    }

    private val gpuOption = Interpreter.Options().apply {
        val compatList = CompatibilityList()
        this.setCancellable(true)
        // Проверяем - поддерживает ли устройство GPU
        // Если нет - используем 4 потока
        if (compatList.isDelegateSupportedOnThisDevice) {
            val delegateOptions = compatList.bestOptionsForThisDevice.apply {
                this.setPrecisionLossAllowed(false)
            }
            this.addDelegate(GpuDelegate(delegateOptions))
        } else
            this.setNumThreads(3)
    }

    private val petDetectionClassificationInterpreter = Interpreter(
        FileUtil.loadMappedFile(context, MODEL_PATH),
        gpuOption
    )

    suspend fun imageDetectionAndClassification(input: ByteBuffer): PetDetectionClassificationResult? {
        try {
            Log.i(TAG, "start image detection and classification")
            val modelTimeStart = System.currentTimeMillis()
            val inputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 640, 640, 3), DataType.FLOAT32)
            inputBuffer.loadBuffer(input)
            val output = Array(1) { Array(9) { FloatArray(8400) } }
            petDetectionClassificationInterpreter.run(inputBuffer.buffer, output)
            Log.i(TAG, "end image detection and classification, time: ${System.currentTimeMillis() - modelTimeStart}")
            val processTimeStart = System.currentTimeMillis()
            val res = processImageDetectionAndClassificationOutput(output)
            Log.i(TAG, "model output precessed, time: ${System.currentTimeMillis() - processTimeStart}, result class: ${res?.category}")
            return res
        } catch (e: Exception){
            Log.e(TAG, e.stackTraceToString())
            return null
        }
    }

    /**
     * Обрабатывает выход модели размерностью (1,9,8400)
     */
    private suspend fun processImageDetectionAndClassificationOutput(modelOutput: Array<Array<FloatArray>>): PetDetectionClassificationResult?{
        // Убираем размерность батча (1,9,8400) -> (9,8400)
        val outputWithoutBatch = modelOutput[0]

        // Транспонируем в (8400,9) - каждая строка = один bounding box
        val transposedOutput = CVUtils.transposeOutput(outputWithoutBatch)

        var maxConfidence = 0f
        var res: PetDetectionClassificationResult? = null

        // Извлекаем компоненты для каждого bounding box'а
        transposedOutput.forEach {
            // Извлекаем координаты бокса
            val boxes = it.copyOfRange(0, 4)

            // Извлекаем уверенности по классам
            val classScores = it.copyOfRange(4, 9)

            // Находим класс с максимальной уверенностью
            val (classId, maxClassScore) = classScores.withIndex().maxByOrNull { it.value } ?: return@forEach

            // Общая уверенность
            val confidence = maxClassScore

            // Сопоставляем classId с реальными классами
            val petCategory = classId.toPetCategory()

            if (confidence > maxConfidence){
                maxConfidence = confidence
                res = PetDetectionClassificationResult(boxes.toList(), petCategory)
            }
        }

        if (maxConfidence < 0.1 && res != null)
            return PetDetectionClassificationResult(res!!.boxes, PetCategory.NONE)
        return res
    }

}