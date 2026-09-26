package com.fit2081.bryanzongsheng33523355

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.bryanzongsheng33523355.data.AuthManager
import com.fit2081.bryanzongsheng33523355.data.patient.Patient
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel
import com.fit2081.bryanzongsheng33523355.ui.theme.BryanZongSheng33523355Theme
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.core.content.edit
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntakeViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BryanZongSheng33523355Theme {
                val viewModel: PatientViewModel = ViewModelProvider(
                    this, PatientViewModel.PatientViewModelFactory(this@MainActivity)
                )[PatientViewModel::class.java]

                val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                    this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@MainActivity)
                )[FoodIntakeViewModel::class.java]


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        this@MainActivity,
                        Modifier.padding(innerPadding),
                        viewModel,
                        foodIntakeViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    context: Context,
    modifier: Modifier = Modifier,
    viewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val patients = viewModel.getAllPatientsOnce()
        if (patients.isEmpty()) {
            parseCsv(context, viewModel)
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit { clear() }
        }
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(R.drawable.nutritrack), // NutriTrack logo
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(400.dp)
            )


            // Disclaimer text
            Text(
                text = "This app provides general health and nutrition information for educational purposes only. " +
                        "It is not intended as medical advice diagnosis, or treatment. " +
                        "Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen." +
                        "Use this app at your own risk." +
                        "If you’d like to an Accredited Practicing Dietitian (APD), please visit the Monash Nutrition/Dietetics Clinic \n " +
                        "(discounted rates for students): \n" +
                        "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition ",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Login button
            Button(onClick = {
                val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val savedUserId = prefs.getString("user_id", null)
                if (savedUserId != null) {
                    AuthManager.login(savedUserId)
                    coroutineScope.launch {
                        val userIdInt = savedUserId.toIntOrNull() ?: 0
                        val hasCompleted =
                            foodIntakeViewModel.getFoodIntakeByPatient(userIdInt) != null

                        if (hasCompleted) {
                            val intent = Intent(context, HomeScreenActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            val intent = Intent(context, QuestionnaireActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                } else {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                }
            }) {
                Text(
                    text = "Login", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Student Name and ID
            Text(text = "Bryan Ong Zong Sheng (33523355)", fontSize = 14.sp)
        }
    }
}


fun parseCsv(context: Context, viewModel: PatientViewModel) {
    try {
        val inputStream = context.assets.open("assignment1.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))

        reader.useLines { sequence ->
            val lines = sequence.toList()
            val header = lines.first().split(",")
            val columnIndexMap = header.mapIndexed { index, name -> name.trim() to index }.toMap()

            lines.drop(1).forEach { line ->
                val columns = line.split(",")

                val sexValue = columns.getOrNull(columnIndexMap["Sex"] ?: -1) ?: "None"
                val isMale = sexValue == "Male"

                val phoneNumber = columns.getOrNull(columnIndexMap["PhoneNumber"] ?: -1) ?: ""
                val userId = columns.getOrNull(columnIndexMap["User_ID"] ?: -1) ?: "0"

                fun getScore(key: String) = columns.getOrNull(columnIndexMap[key] ?: -1) ?: "0"

                val vegetableScore =
                    getScore(if (isMale) "VegetablesHEIFAscoreMale" else "VegetablesHEIFAscoreFemale")
                val fruitScore =
                    getScore(if (isMale) "FruitHEIFAscoreMale" else "FruitHEIFAscoreFemale")
                val grainCerealScore =
                    getScore(if (isMale) "GrainsandcerealsHEIFAscoreMale" else "GrainsandcerealsHEIFAscoreFemale")
                val wholeGrainScore =
                    getScore(if (isMale) "WholegrainsHEIFAscoreMale" else "WholegrainsHEIFAscoreFemale")
                val meatAlternativesScore =
                    getScore(if (isMale) "MeatandalternativesHEIFAscoreMale" else "MeatandalternativesHEIFAscoreFemale")
                val dairyScore =
                    getScore(if (isMale) "DairyandalternativesHEIFAscoreMale" else "DairyandalternativesHEIFAscoreFemale")
                val waterScore =
                    getScore(if (isMale) "WaterHEIFAscoreMale" else "WaterHEIFAscoreFemale")
                val unsaturatedFatsScore =
                    getScore(if (isMale) "UnsaturatedFatHEIFAscoreMale" else "UnsaturatedFatHEIFAscoreFemale")
                val saturatedFatsScore =
                    getScore(if (isMale) "SaturatedFatHEIFAscoreMale" else "SaturatedFatHEIFAscoreFemale")
                val sodiumScore =
                    getScore(if (isMale) "SodiumHEIFAscoreMale" else "SodiumHEIFAscoreFemale")
                val sugarScore =
                    getScore(if (isMale) "SugarHEIFAscoreMale" else "SugarHEIFAscoreFemale")
                val alcoholScore =
                    getScore(if (isMale) "AlcoholHEIFAscoreMale" else "AlcoholHEIFAscoreFemale")
                val discretionaryFoodScore =
                    getScore(if (isMale) "DiscretionaryHEIFAscoreMale" else "DiscretionaryHEIFAscoreFemale")
                val totalScore =
                    getScore(if (isMale) "HEIFAtotalscoreMale" else "HEIFAtotalscoreFemale")

                val fruitServeSize = getScore("Fruitservesize")
                val fruitVariationsScore = getScore("Fruitvariationsscore")

                viewModel.insert(
                    Patient(
                        userId.toIntOrNull() ?: 0,
                        phoneNumber,
                        null,
                        sexValue,
                        vegetableScore.toFloatOrNull() ?: 0f,
                        fruitScore.toFloatOrNull() ?: 0f,
                        grainCerealScore.toFloatOrNull()
                            ?.plus(wholeGrainScore.toFloatOrNull() ?: 0f) ?: 0f,
                        meatAlternativesScore.toFloatOrNull() ?: 0f,
                        dairyScore.toFloatOrNull() ?: 0f,
                        waterScore.toFloatOrNull() ?: 0f,
                        unsaturatedFatsScore.toFloatOrNull()
                            ?.plus(saturatedFatsScore.toFloatOrNull() ?: 0f) ?: 0f,
                        sodiumScore.toFloatOrNull() ?: 0f,
                        sugarScore.toFloatOrNull() ?: 0f,
                        alcoholScore.toFloatOrNull() ?: 0f,
                        discretionaryFoodScore.toFloatOrNull() ?: 0f,
                        totalScore.toFloatOrNull() ?: 0f,
                        null,
                        fruitServeSize.toFloatOrNull() ?: 0f,
                        fruitVariationsScore.toFloatOrNull() ?: 0f
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
