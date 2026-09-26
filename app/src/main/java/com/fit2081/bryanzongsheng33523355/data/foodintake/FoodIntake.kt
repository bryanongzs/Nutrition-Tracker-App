package com.fit2081.bryanzongsheng33523355.data.foodintake

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.fit2081.bryanzongsheng33523355.data.patient.Patient

// FoodIntake.kt
@Entity(tableName = "food_intake")
data class FoodIntake(
    @PrimaryKey
    val userId: Int,
    val fruitsChecked: Boolean,
    val vegetablesChecked: Boolean,
    val grainsChecked: Boolean,
    val redMeatChecked: Boolean,
    val seafoodChecked: Boolean,
    val poultryChecked: Boolean,
    val fishChecked: Boolean,
    val eggChecked: Boolean,
    val nutsSeedsChecked: Boolean,
    val selectedPersona: String,
    val biggestMealTime: String,
    val sleepTime: String,
    val wakeUpTime: String
)