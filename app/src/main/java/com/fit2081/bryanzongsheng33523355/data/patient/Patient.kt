package com.fit2081.bryanzongsheng33523355.data.patient

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient (
    @PrimaryKey
    val userId: Int,
    val phoneNumber: String,
    val name: String?,
    val sex: String,
    val vegetableScore: Float,
    val fruitScore: Float,
    val grainCerealScore: Float,
    val meatAndAlternativesScore: Float,
    val dairyScore: Float,
    val waterScore: Float,
    val unsaturatedFatsScore: Float,
    val sodiumScore: Float,
    val sugarScore: Float,
    val alcoholScore: Float,
    val discretionaryFoodScore: Float,
    val totalScore: Float,
    val password: String?,
    val fruitServeSize: Float,
    val fruitVariationsScore: Float
)
