package com.fit2081.bryanzongsheng33523355.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntake
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntakeDao
import com.fit2081.bryanzongsheng33523355.data.nutricoachtip.NutriCoachTip
import com.fit2081.bryanzongsheng33523355.data.nutricoachtip.NutriCoachTipDao
import com.fit2081.bryanzongsheng33523355.data.patient.Patient
import com.fit2081.bryanzongsheng33523355.data.patient.PatientDao

@Database(entities = [Patient::class, FoodIntake::class, NutriCoachTip::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao
    abstract fun foodIntakeDao(): FoodIntakeDao


    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabases(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .build().also {Instance = it
                    }
            }
        }
    }
}