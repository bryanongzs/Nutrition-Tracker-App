package com.fit2081.bryanzongsheng33523355.data

import com.fit2081.bryanzongsheng33523355.data.network.APIService
import com.fit2081.bryanzongsheng33523355.data.network.ResponseModel

class FruitRepository {
    private val apiService = APIService.create()

    suspend fun getFruitData (fruitName: String): ResponseModel? {
        return apiService.getFruitNutrition(fruitName.lowercase().trim())
    }
}