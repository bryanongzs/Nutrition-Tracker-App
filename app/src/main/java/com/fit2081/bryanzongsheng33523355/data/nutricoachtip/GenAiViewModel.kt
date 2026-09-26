package com.fit2081.bryanzongsheng33523355.data.nutricoachtip

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fit2081.bryanzongsheng33523355.UiState
import com.fit2081.bryanzongsheng33523355.data.AuthManager
import com.fit2081.bryanzongsheng33523355.data.patient.Patient
import com.fit2081.bryanzongsheng33523355.data.patient.PatientsRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenAiViewModel(context: Context) : ViewModel() {

    private val repository = NutriCoachRepository(context)
    private val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)

    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    private val _uiTipState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)

    val uiTipState: StateFlow<UiState> =
        _uiTipState.asStateFlow()


    private val patientRepository = PatientsRepository(context)

    /*
    I used Gemini-1.5-flash in NutriCoach and Clinician Dashboard.
    The tool was used to provide NutriCoach tips and 3 data patterns.
    */
    // api key is "AIzaSyBFu-L1YK0_GTIwmuPKwhLeys8yS2xeX8I"
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyBFu-L1YK0_GTIwmuPKwhLeys8yS2xeX8I"
    )

    // get the fruit encourage message
    fun sendPrompt(prompt: String) {
        _uiTipState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )

                response.text?.let { outputContent ->
                    repository.insertTip(userId, outputContent)
                    _uiTipState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiTipState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    // get all user previous ai tips
    fun getUserTips(): Flow<List<NutriCoachTip>> {
        return repository.getUserTips(userId)
    }

    suspend fun getAllPatients(): List<Patient> {
        return patientRepository.getAllPatientsOnce()
    }

    fun formatPatientsForPrompt(patients: List<Patient>): String {
        return patients.joinToString("\n") { patient ->
            """
            ID: ${patient.userId}, Name: ${patient.name ?: "N/A"}, Phone: ${patient.phoneNumber}, Sex: ${patient.sex}
            HEIFA Scores:
            - Vegetable: ${patient.vegetableScore}, Fruit: ${patient.fruitScore}, Grain & Cereal: ${patient.grainCerealScore}
            - Meat & Alternatives: ${patient.meatAndAlternativesScore}, Dairy: ${patient.dairyScore}, Water: ${patient.waterScore}
            - Unsaturated Fats: ${patient.unsaturatedFatsScore}, Sodium: ${patient.sodiumScore}, Sugar: ${patient.sugarScore}
            - Alcohol: ${patient.alcoholScore}, Discretionary Food: ${patient.discretionaryFoodScore}
            - Fruit Serve Size: ${patient.fruitServeSize}, Fruit Variations Score: ${patient.fruitVariationsScore}
            - Total HEIFA Score: ${patient.totalScore}
            """.trimIndent()
        }
    }


    data class DataPattern(
        val title: String,
        val description: String
    )

    fun sendPatientDataForAnalysis() {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val patients = getAllPatients()
                val prompt = """
                You are a nutrition analyst. Analyse this HEIFA nutrition score data and identify EXACTLY 3 SIGNIFICANT PATTERNS.
                
                Each pattern MUST HAVE the following format on a single line:
                Pattern X: [Title that is short] | [Description]
                
                DO NOT include any extra text before or after the 3 patterns.
                Each line should begin with 'Pattern X:', where X is 1 to 3.
                
                Patient Data:
                ${formatPatientsForPrompt(patients)}
                """.trimIndent()


                val response = generativeModel.generateContent(content { text(prompt) })

                response.text?.let { outputContent ->
                    val patterns = parsePatterns(outputContent)
                    if (patterns.size == 3) {
                        _uiState.value = UiState.Success(patterns.joinToString("\n\n") {
                            "${it.title}\n${it.description}"
                        })
                    } else {
                        _uiState.value = UiState.Error("no clear patterns")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Failed")
            }
        }
    }

    private fun parsePatterns(response: String): List<DataPattern> {
        return response
            .lineSequence()
            .filter { it.startsWith("Pattern") && it.contains("|") }
            .mapNotNull { line ->
                val titlePart = line.substringAfter(":").substringBefore("|").trim()
                val descPart = line.substringAfter("|").trim()
                if (titlePart.isNotBlank() && descPart.isNotBlank()) {
                    DataPattern(title = titlePart, description = descPart)
                } else null
            }
            .take(3)
            .toList()
    }



    class GenAiViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GenAiViewModel(context) as T
    }
}