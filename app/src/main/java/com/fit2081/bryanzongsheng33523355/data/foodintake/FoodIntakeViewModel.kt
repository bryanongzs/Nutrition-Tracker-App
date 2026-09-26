package com.fit2081.bryanzongsheng33523355.data.foodintake

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FoodIntakeViewModel(context: Context) : ViewModel() {

    private val foodIntakeRepo = FoodIntakeRepository(context)

    suspend fun getFoodIntakeByPatient(patientId: Int): FoodIntake? {
        return foodIntakeRepo.getFoodIntakeByPatient(patientId)
    }

    fun insertFoodIntake(foodIntake: FoodIntake) = viewModelScope.launch {
        foodIntakeRepo.insertFoodIntake(foodIntake)
    }

    class FoodIntakeViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FoodIntakeViewModel(context) as T
    }
}