package com.fit2081.bryanzongsheng33523355

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fit2081.bryanzongsheng33523355.ui.theme.BryanZongSheng33523355Theme
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.bryanzongsheng33523355.data.AuthManager
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntakeViewModel
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel
import kotlinx.coroutines.launch
import androidx.core.content.edit


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BryanZongSheng33523355Theme {
                val viewModel: PatientViewModel = ViewModelProvider(
                    this, PatientViewModel.PatientViewModelFactory(this@LoginActivity)
                )[PatientViewModel::class.java]

                val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                    this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@LoginActivity)
                )[FoodIntakeViewModel::class.java]

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        foodIntakeViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val userIds by viewModel.allPatientsId.collectAsState(initial = emptyList())
    val patients by viewModel.allPatients.collectAsState(initial = emptyList())


    var selectedUserId by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // users id using dropdown menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedUserId,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text("My ID (Provided by your Clinician)") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                userIds.forEach { userId ->
                    DropdownMenuItem(
                        text = { Text(userId.toString()) },
                        onClick = {
                            selectedUserId = userId.toString()
                            expanded = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))


        // password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(
                text = "Invalid credentials",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account on your first visit.",
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // login button
        Button(
            onClick = {
                val matched =
                    patients.any { patient -> patient.userId.toString() == selectedUserId && patient.password == password }

                if (matched) {
                    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit {
                        putString("user_id", selectedUserId)
                    }

                    AuthManager.login(selectedUserId)

                    coroutineScope.launch {
                        val userIdInt = selectedUserId.toIntOrNull() ?: 0
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
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = Intent(context, RegisterScreenActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
