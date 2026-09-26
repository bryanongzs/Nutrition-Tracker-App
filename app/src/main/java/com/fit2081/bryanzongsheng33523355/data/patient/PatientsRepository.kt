package com.fit2081.bryanzongsheng33523355.data.patient

import android.content.Context
import com.fit2081.bryanzongsheng33523355.data.AppDatabase
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntake
import kotlinx.coroutines.flow.Flow

class PatientsRepository {

    var patientDao : PatientDao

    constructor(context: Context) {
        patientDao = AppDatabase.getDatabases(context).patientDao()
    }

    suspend fun insert(patient: Patient) {
        patientDao.insert(patient)
    }


    suspend fun getPatientById(patientId: Int): Patient? {
        return patientDao.getPatientById(patientId)
    }

    fun getAllPatients():
            Flow<List<Patient>> = patientDao.getAllPatients()

    fun getAllPatientsId():
            Flow<List<Int>> = patientDao.getAllPatientsId()

    suspend fun getAllPatientsOnce(): List<Patient> {
        return patientDao.getAllPatientsOnce()
    }

    suspend fun updateRegistrationDetails(
        userId: Int,
        name: String,
        password: String
    ) {
        patientDao.updateRegistrationDetails(userId, name, password)
    }
}