package com.fit2081.bryanzongsheng33523355

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun ClinicianLoginScreen(navController: NavHostController) {

    var clinicianKey by remember { mutableStateOf("") }
    var keyError by remember { mutableStateOf(false) }
    val realKey = "dollar-entry-apples"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(30.dp))


        OutlinedTextField(
            value = clinicianKey,
            onValueChange = { clinicianKey = it },
            label = { Text("Clinician Key") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = keyError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (keyError) {
            Text(
                text = "Incorrect key.",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {

                if (clinicianKey == realKey) {
                    navController.navigate("ClinicianDashboard")
                } else {
                    keyError = true
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clinician Login")
        }


    }
}

