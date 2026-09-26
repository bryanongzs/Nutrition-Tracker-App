package com.fit2081.bryanzongsheng33523355.data.patient

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Query("SELECT * FROM patients WHERE userId = :patientId")
    suspend fun getPatientById(patientId: Int): Patient?

    @Query("SELECT * FROM patients ORDER BY name ASC")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT userId FROM patients ORDER BY userId ASC")
    fun getAllPatientsId(): Flow<List<Int>>

    @Query("SELECT * FROM patients")
    suspend fun getAllPatientsOnce(): List<Patient>

    @Query("""
        UPDATE patients 
        SET name = :name,
            password = :password 
        WHERE userId = :userId
    """)
    suspend fun updateRegistrationDetails(
        userId: Int,
        name: String,
        password: String
    )

}