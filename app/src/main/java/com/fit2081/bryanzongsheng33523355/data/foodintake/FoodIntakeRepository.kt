package com.fit2081.bryanzongsheng33523355.data.foodintake

import android.content.Context
import com.fit2081.bryanzongsheng33523355.data.AppDatabase

class FoodIntakeRepository {

    var foodIntakeDao : FoodIntakeDao

    constructor(context: Context) {
        foodIntakeDao = AppDatabase.getDatabases(context).foodIntakeDao()
    }


    suspend fun getFoodIntakeByPatient(patientId: Int): FoodIntake? {
        return foodIntakeDao.getFoodIntakeByPatient(patientId)
    }


    suspend fun insertFoodIntake(foodIntake: FoodIntake) {
        foodIntakeDao.insertFoodIntake(foodIntake)
    }
}