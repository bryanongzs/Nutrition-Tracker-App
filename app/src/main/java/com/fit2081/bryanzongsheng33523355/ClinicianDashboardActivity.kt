package com.fit2081.bryanzongsheng33523355

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fit2081.bryanzongsheng33523355.data.nutricoachtip.GenAiViewModel
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel


@Composable
fun ClinicianDashboardScreen(
    viewModel: PatientViewModel,
    genAiViewModel: GenAiViewModel,
    navController: NavHostController
) {
    val averages by viewModel.averageHeifaScoresByGender.collectAsState(initial = null)
    val uiState by genAiViewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 72.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Clinician Dashboard",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // gender averages
            if (averages != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 3.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Average HEIFA (Male)", modifier = Modifier.weight(0.7f))
                        Text(
                            ": ${"%.1f".format(averages!!.first)}",
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 3.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Average HEIFA (Female)", modifier = Modifier.weight(0.7f))
                        Text(
                            ": ${"%.1f".format(averages!!.second)}",
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // ai 3 pattern
            Button(
                onClick = { genAiViewModel.sendPatientDataForAnalysis() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Find Data Patterns")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is UiState.Error -> {
                    Text(
                        text = (uiState as UiState.Error).errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UiState.Success -> {
                    val patterns = parseAiResponse((uiState as UiState.Success).outputText)
                    Column {
                        if (patterns.isNotEmpty()) {
                            Text(
                                text = "Identified Patterns:",
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            patterns.forEach { pattern ->
                                PatternCard(pattern)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                else -> {}
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Button(
                onClick = { navController.navigate("Setting") }
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
fun PatternCard(pattern: String) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val lines = pattern.lines()

            // title
            if (lines.isNotEmpty()) {
                Text(
                    text = lines[0],
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // description
            if (lines.size > 1) {
                Text(
                    text = lines.drop(1).joinToString("\n"),
                )
            }
        }
    }
}


fun parseAiResponse(response: String): List<String> {
    return response.split("\n\n").filter { it.isNotBlank() }
}