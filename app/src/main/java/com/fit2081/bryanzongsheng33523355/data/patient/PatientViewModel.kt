package com.fit2081.bryanzongsheng33523355.data.patient

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PatientViewModel(context: Context) : ViewModel() {

    private val patientRepo = PatientsRepository(context)

    val allPatientsId: Flow<List<Int>> = patientRepo.getAllPatientsId()
    val allPatients: Flow<List<Patient>> = patientRepo.getAllPatients()

    val unregisteredPatientsWithPhone: Flow<List<Pair<Int, String>>> =
        allPatients.map { patients ->
            patients.filter { it.name.isNullOrBlank() }
                .map { it.userId to it.phoneNumber }
        }

    val averageHeifaScoresByGender: Flow<Pair<Float?, Float?>> = allPatients.map { patients ->
        val maleScores = patients.filter { it.sex == "Male" }.map { it.totalScore }
        val femaleScores = patients.filter { it.sex == "Female" }.map { it.totalScore }

        val maleAvg = maleScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
        val femaleAvg = femaleScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()

        maleAvg to femaleAvg
    }

    val averageFruitScore: Flow<Float?> = allPatients.map { patients ->
        val fruitScores = patients.map { it.fruitScore }
        fruitScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageVegetableScore: Flow<Float?> = allPatients.map { patients ->
        val fruitScores = patients.map { it.fruitScore }
        fruitScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageGrainCerealScore: Flow<Float?> = allPatients.map { patients ->
        val grainScores = patients.map { it.grainCerealScore }
        grainScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageMeatAndAlternativesScore: Flow<Float?> = allPatients.map { patients ->
        val meatScores = patients.map { it.meatAndAlternativesScore }
        meatScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageDairyScore: Flow<Float?> = allPatients.map { patients ->
        val dairyScores = patients.map { it.dairyScore }
        dairyScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageWaterScore: Flow<Float?> = allPatients.map { patients ->
        val waterScores = patients.map { it.waterScore }
        waterScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageUnsaturatedFatsScore: Flow<Float?> = allPatients.map { patients ->
        val fatScores = patients.map { it.unsaturatedFatsScore }
        fatScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageSodiumScore: Flow<Float?> = allPatients.map { patients ->
        val sodiumScores = patients.map { it.sodiumScore }
        sodiumScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageSugarScore: Flow<Float?> = allPatients.map { patients ->
        val sugarScores = patients.map { it.sugarScore }
        sugarScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageAlcoholScore: Flow<Float?> = allPatients.map { patients ->
        val alcoholScores = patients.map { it.alcoholScore }
        alcoholScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageDiscretionaryFoodScore: Flow<Float?> = allPatients.map { patients ->
        val discretionaryScores = patients.map { it.discretionaryFoodScore }
        discretionaryScores.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    val averageTotalScore: Flow<Float?> = allPatients.map { patients ->
        val totalScore = patients.map { it.totalScore }
        totalScore.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }


    fun insert(patient: Patient) = viewModelScope.launch {
        patientRepo.insert(patient)
    }


    suspend fun getPatientById(patientId: Int): Patient? {
        return patientRepo.getPatientById(patientId)
    }

    fun getAllPatientsId() = viewModelScope.launch {
        patientRepo.getAllPatientsId()
    }


    suspend fun getAllPatientsOnce(): List<Patient> {
        return patientRepo.getAllPatientsOnce()
    }

    fun updateRegistrationDetails(
        userId: Int,
        name: String,
        password: String
    ) = viewModelScope.launch {
        patientRepo.updateRegistrationDetails(
            userId = userId,
            name = name,
            password = password
        )
    }


    class PatientViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PatientViewModel(context) as T
    }
}