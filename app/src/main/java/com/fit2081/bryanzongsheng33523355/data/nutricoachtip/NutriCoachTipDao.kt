package com.fit2081.bryanzongsheng33523355.data.nutricoachtip

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipDao {

    @Insert
    suspend fun insertTip(tip: NutriCoachTip)

    @Query("select * from nutri_coach_tip where userId =:userId")
    fun getUserTips(userId: Int):
            Flow<List<NutriCoachTip>>
}