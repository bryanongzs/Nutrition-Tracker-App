package com.fit2081.bryanzongsheng33523355

import android.app.TimePickerDialog
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.fit2081.bryanzongsheng33523355.data.AuthManager
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntake
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntakeViewModel
import com.fit2081.bryanzongsheng33523355.ui.theme.BryanZongSheng33523355Theme
import java.util.Calendar

class QuestionnaireActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BryanZongSheng33523355Theme {
                val viewModel: PatientViewModel = ViewModelProvider(
                    this, PatientViewModel.PatientViewModelFactory(this@QuestionnaireActivity)
                )[PatientViewModel::class.java]

                val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                    this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@QuestionnaireActivity)
                )[FoodIntakeViewModel::class.java]

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodIntakeQuestionnaire(
                        this@QuestionnaireActivity,
                        modifier = Modifier.padding(innerPadding), viewModel, foodIntakeViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodIntakeQuestionnaire(
    context: Context,
    modifier: Modifier = Modifier,
    viewModel: PatientViewModel,
    foodIntakeViewModel: FoodIntakeViewModel
) {
    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0

    var foodIntake by remember { mutableStateOf<FoodIntake?>(null) }
    var isLoading by remember { mutableStateOf(true) }


    // checkbox foods
    var fruitsChecked by remember { mutableStateOf(false) }
    var vegetablesChecked by remember { mutableStateOf(false) }
    var grainsChecked by remember { mutableStateOf(false) }
    var redMeatChecked by remember { mutableStateOf(false) }
    var seafoodChecked by remember { mutableStateOf(false) }
    var poultryChecked by remember { mutableStateOf(false) }
    var fishChecked by remember { mutableStateOf(false) }
    var eggChecked by remember { mutableStateOf(false) }
    var nutsSeedsChecked by remember { mutableStateOf(false) }

    var finalPersona by remember { mutableStateOf("") }

    val personas = listOf(
        "Health Devotee",
        "Mindful Eater",
        "Wellness Striver",
        "Balance Seeker",
        "Health Procrastinator",
        "Food Carefree"
    )

    // Time pickers
    val mMealTime = remember { mutableStateOf("00:00") }
    val biggestMealTime = timePickerFun(mMealTime)
    val mSleepTime = remember { mutableStateOf("00:00") }
    val sleepTime = timePickerFun(mSleepTime)
    val mWakeUpTime = remember { mutableStateOf("00:00") }
    val wakeUpTime = timePickerFun(mWakeUpTime)
    var timeError by remember { mutableStateOf(false) }
    var duplicateError by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedPersona by remember { mutableStateOf("") }
    var selectedPersonaDescription by remember { mutableStateOf("") }
    var selectedPersonaImage by remember { mutableIntStateOf(0) }
    var personaExpanded by remember { mutableStateOf(false) }
    var personaError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // get the values from the database
    LaunchedEffect(userId) {
        foodIntake = foodIntakeViewModel.getFoodIntakeByPatient(userId)
        isLoading = false

        foodIntake?.let {
            fruitsChecked = it.fruitsChecked
            vegetablesChecked = it.vegetablesChecked
            grainsChecked = it.grainsChecked
            redMeatChecked = it.redMeatChecked
            seafoodChecked = it.seafoodChecked
            poultryChecked = it.poultryChecked
            fishChecked = it.fishChecked
            eggChecked = it.eggChecked
            nutsSeedsChecked = it.nutsSeedsChecked

            mMealTime.value = it.biggestMealTime
            mSleepTime.value = it.sleepTime
            mWakeUpTime.value = it.wakeUpTime
            finalPersona = it.selectedPersona
        }
    }

    // show loading screen when fetching data from database
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
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Food Intake Questionnaire", fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(8.dp))

            Text(text = "Tick all the food categories you can eat", fontSize = 16.sp)
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = fruitsChecked, onCheckedChange = { fruitsChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Fruits", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = vegetablesChecked,
                        onCheckedChange = { vegetablesChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Vegetables", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = grainsChecked, onCheckedChange = { grainsChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Grains", fontSize = 12.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = redMeatChecked, onCheckedChange = { redMeatChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Red Meat", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = seafoodChecked, onCheckedChange = { seafoodChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Seafood", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = poultryChecked, onCheckedChange = { poultryChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Poultry", fontSize = 12.sp)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = fishChecked, onCheckedChange = { fishChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Fish", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(checked = eggChecked, onCheckedChange = { eggChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Eggs", fontSize = 12.sp)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = nutsSeedsChecked,
                        onCheckedChange = { nutsSeedsChecked = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Nuts/Seeds", fontSize = 12.sp)
                }
            }


            Spacer(Modifier.height(8.dp))


            // Persona Selection Buttons
            Text(text = "Your Persona", fontSize = 20.sp)
            Text(
                text = "People can be broadly classified into 6 different types based on their eating preferences." +
                        " Click on each button below to find out the different types, and select the type that best fits you!",
                textAlign = TextAlign.Left,
                fontSize = 12.sp
            )

            Spacer(Modifier.height(4.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    selectedPersona = "Health Devotee"
                    selectedPersonaDescription =
                        "I’m passionate about healthy eating & health plays a big part in my life." +
                                " I use social media to follow active lifestyle personalities or get new recipes/exercise ideas." +
                                " I may even buy superfoods or follow a particular type of diet. I like to think I am super healthy."
                    selectedPersonaImage = R.drawable.persona_1
                    showDialog = true
                }) {
                    Text(text = "Health Devotee", fontSize = 10.sp)
                }

                Button(onClick = {
                    selectedPersona = "Mindful Eater"
                    selectedPersonaDescription =
                        "I’m health-conscious and being healthy and eating healthy is important to me." +
                                " Although health means different things to different people, I make conscious lifestyle decisions about eating" +
                                " based on what I believe healthy means. I look for new recipes and healthy eating information on social media."
                    selectedPersonaImage = R.drawable.persona_2
                    showDialog = true
                }) {
                    Text(text = "Mindful Eater", fontSize = 10.sp)
                }

                Button(onClick = {
                    selectedPersona = "Wellness Striver"
                    selectedPersonaDescription =
                        "I aspire to be healthy (but struggle sometimes). Healthy eating is hard work!" +
                                " I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes." +
                                " Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go."
                    selectedPersonaImage = R.drawable.persona_3
                    showDialog = true
                }) {
                    Text(text = "Wellness Striver", fontSize = 10.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    selectedPersona = "Balance Seeker"
                    selectedPersonaDescription =
                        "I try and live a balanced lifestyle, and I think that all foods are okay in moderation." +
                                " I shouldn’t have to feel guilty about eating a piece of cake now and again. I get all sorts of inspiration from social media" +
                                " like finding out about new restaurants, fun recipes and sometimes healthy eating tips."
                    selectedPersonaImage = R.drawable.persona_4
                    showDialog = true
                }) {
                    Text(text = "Balance Seeker", fontSize = 10.sp)
                }
                Button(onClick = {
                    selectedPersona = "Health Procrastinator"
                    selectedPersonaDescription =
                        "I’m contemplating healthy eating but it’s not a priority for me right now. I know the basics about what" +
                                " it means to be healthy, but it doesn’t seem relevant to me right now. I have taken a few steps to be healthier but I am not motivated" +
                                " to make it a high priority because I have too many other things going on in my life."
                    selectedPersonaImage = R.drawable.persona_5
                    showDialog = true
                }) {
                    Text(text = "Health Procrastinator", fontSize = 10.sp)
                }

                Button(onClick = {
                    selectedPersona = "Food Carefree"
                    selectedPersonaDescription =
                        "I’m not bothered about healthy eating. I don’t really see the point and I don’t think about it." +
                                " I don’t really notice healthy eating tips or recipes and I don’t care what I eat."
                    selectedPersonaImage = R.drawable.persona_6
                    showDialog = true
                }) {
                    Text(text = "Food Carefree", fontSize = 10.sp)
                }
            }


            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Image(
                            painter = painterResource(selectedPersonaImage),
                            contentDescription = "Persona Image"
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp) // Adds space between text elements
                        ) {
                            Text(
                                text = selectedPersona,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp
                            )
                            Text(text = selectedPersonaDescription)
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showDialog = false }) {
                            Text(text = "Dismiss")
                        }
                    }
                )
            }

            Spacer(Modifier.height(8.dp))


            // Persona Dropdown
            Text(
                text = "Which persona fits you?", fontSize = 20.sp
            )

            ExposedDropdownMenuBox(
                expanded = personaExpanded,
                onExpandedChange = { personaExpanded = !personaExpanded }
            ) {
                TextField(
                    value = finalPersona,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Select Option") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = personaExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = personaExpanded,
                    onDismissRequest = { personaExpanded = false }
                ) {
                    personas.forEach { persona ->
                        DropdownMenuItem(
                            text = { Text(persona) },
                            onClick = {
                                finalPersona = persona
                                personaExpanded = false
                            }
                        )
                    }
                }
            }

            // error handling
            if (personaError) {
                Text(
                    text = "Please choose a persona.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(10.dp))


            Text(text = "Timings", fontSize = 20.sp)
            // BIGGEST MEAL PICK TIME
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "What time of day approx. do you normally eat your biggest meal?",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )
                Button(onClick = { biggestMealTime.show() }) {
                    Text(mMealTime.value)
                }
            }
            Spacer(Modifier.height(4.dp))


            // SLEEP TIME PICK TIME
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "What time of day approx. do you go to sleep at night?",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )
                Button(onClick = { sleepTime.show() }) {
                    Text(mSleepTime.value)
                }
            }
            Spacer(Modifier.height(4.dp))

            //WAKE UP PICK TIME
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "What time of day approx. do you wake up in the morning?",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp
                )
                Button(onClick = { wakeUpTime.show() }) {
                    Text(mWakeUpTime.value)
                }
            }

            // error handling
            if (timeError) {
                Text(
                    text = "Please fill up the timings.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            } else if (duplicateError) {
                Text(
                    text = "All timings must be different.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {

                    personaError = false
                    timeError = false
                    duplicateError = false

                    if (finalPersona.isEmpty()) {
                        personaError = true
                    }

                    if (mMealTime.value == "00:00" || mSleepTime.value == "00:00" || mWakeUpTime.value == "00:00") {
                        timeError = true
                    }

                    val times = listOf(mMealTime.value, mSleepTime.value, mWakeUpTime.value)
                    if (times.distinct().size < times.size) {
                        duplicateError = true
                    }


                    if (!personaError && !timeError && !duplicateError) {

                        val newFoodIntake = FoodIntake(
                            userId = userId,
                            fruitsChecked = fruitsChecked,
                            vegetablesChecked = vegetablesChecked,
                            grainsChecked = grainsChecked,
                            redMeatChecked = redMeatChecked,
                            seafoodChecked = seafoodChecked,
                            poultryChecked = poultryChecked,
                            fishChecked = fishChecked,
                            eggChecked = eggChecked,
                            nutsSeedsChecked = nutsSeedsChecked,
                            selectedPersona = finalPersona,
                            biggestMealTime = mMealTime.value,
                            sleepTime = mSleepTime.value,
                            wakeUpTime = mWakeUpTime.value
                        )

                        foodIntakeViewModel.insertFoodIntake(newFoodIntake)


                        val intent = Intent(context, HomeScreenActivity::class.java)
                        context.startActivity(intent)
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(text = "Save", textAlign = TextAlign.Center)

            }
        }
    }
}


@Composable
fun timePickerFun(mTime: MutableState<String>): TimePickerDialog {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()

    val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
    val mMinute = mCalendar.get(Calendar.MINUTE)

    return TimePickerDialog(
        mContext,
        { _, hourOfDay: Int, minute: Int ->
            mTime.value = "$hourOfDay:$minute"
        },
        mHour,
        mMinute,
        false // Use 24-hour format
    )
}
