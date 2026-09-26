package com.fit2081.bryanzongsheng33523355.data.network

data class ResponseModel(
    val genus: String,
    val name: String,
    val id: Int,
    val family: String,
    val order: String,
    val nutritions: NutritionData
)

data class NutritionData(
    val calories: Float,
    val fat: Float,
    val sugar: Float,
    val carbohydrates: Float,
    val protein: Float
)
