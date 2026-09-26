package com.fit2081.bryanzongsheng33523355

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fit2081.bryanzongsheng33523355.ui.theme.BryanZongSheng33523355Theme
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel
import kotlinx.coroutines.launch

class RegisterScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BryanZongSheng33523355Theme {
                val viewModel: PatientViewModel = ViewModelProvider(
                    this, PatientViewModel.PatientViewModelFactory(this@RegisterScreenActivity)
                )[PatientViewModel::class.java]

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(modifier: Modifier, viewModel: PatientViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var phoneInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var wrongPhoneError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }


    var selectedPatient by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val userIds by viewModel.unregisteredPatientsWithPhone.collectAsState(emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register")

        Spacer(modifier = Modifier.height(16.dp))

        // users id using dropdown menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedPatient?.first?.toString() ?: " ",
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
                userIds.forEach { (userId, phone) ->
                    DropdownMenuItem(
                        text = { Text(userId.toString()) },
                        onClick = {
                            selectedPatient = (userId to phone)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(30.dp))

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = nameError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (nameError) {
                Text(
                    "Name cannot be empty.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        OutlinedTextField(
            value = phoneInput,
            onValueChange = {
                phoneInput = it
                phoneError = it.length != 11
            },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = phoneError || wrongPhoneError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (phoneError) {
                Text(
                    text = "Phone number must be 11 numbers.",
                    color = MaterialTheme.colorScheme.error
                )
            } else if (wrongPhoneError) {
                Text(
                    text = "Phone number does not match the user ID.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }


        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (passwordError) {
                Text(
                    text = "Password cannot be empty.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        OutlinedTextField(
            value = confirmPasswordInput,
            onValueChange = { confirmPasswordInput = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (confirmPasswordError) {
                Text(
                    text = "Passwords does not match.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nameError = false
                phoneError = false
                wrongPhoneError = false
                passwordError = false
                confirmPasswordError = false

                scope.launch {
                    val userId = selectedPatient?.first ?: 0
                    val expectedPhone = selectedPatient?.second ?: ""

                    if (nameInput.isBlank()) {
                        nameError = true
                    }

                    if (phoneInput.isBlank()) {
                        phoneError = true
                    }

                    if (phoneInput != expectedPhone) {
                        wrongPhoneError = true
                    }

                    if (passwordInput.isBlank()) {
                        passwordError = true
                    }

                    if (passwordInput != confirmPasswordInput) {
                        confirmPasswordError = true
                    }

                    if (!nameError && !phoneError && !wrongPhoneError && !passwordError && !confirmPasswordError) {
                        viewModel.updateRegistrationDetails(
                            userId = userId,
                            name = nameInput,
                            password = passwordInput
                        )

                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}


