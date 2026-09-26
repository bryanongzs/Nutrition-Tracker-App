package com.fit2081.bryanzongsheng33523355.data.foodintake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intake WHERE userId = :userId")
    suspend fun getFoodIntakeByPatient(userId: Int): FoodIntake?

}