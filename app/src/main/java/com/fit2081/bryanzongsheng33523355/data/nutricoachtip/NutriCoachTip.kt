package com.fit2081.bryanzongsheng33523355.data.nutricoachtip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutri_coach_tip")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val message: String?
)
