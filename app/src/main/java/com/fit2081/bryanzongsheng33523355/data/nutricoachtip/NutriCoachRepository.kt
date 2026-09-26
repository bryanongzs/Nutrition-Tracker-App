package com.fit2081.bryanzongsheng33523355.data.nutricoachtip

import android.content.Context
import com.fit2081.bryanzongsheng33523355.data.AppDatabase
import kotlinx.coroutines.flow.Flow

class NutriCoachRepository {

    var tipDao : NutriCoachTipDao

    constructor(context: Context) {
        tipDao = AppDatabase.getDatabases(context).nutriCoachTipDao()
    }


    suspend fun insertTip(userId: Int, message: String) {
        tipDao.insertTip(NutriCoachTip(userId = userId, message = message))
    }

    fun getUserTips(userId: Int): Flow<List<NutriCoachTip>> {
        return tipDao.getUserTips(userId)
    }
}