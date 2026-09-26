package com.fit2081.bryanzongsheng33523355

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.fit2081.bryanzongsheng33523355.data.AuthManager
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntake
import com.fit2081.bryanzongsheng33523355.data.FruitRepository
import com.fit2081.bryanzongsheng33523355.data.foodintake.FoodIntakeViewModel
import com.fit2081.bryanzongsheng33523355.data.nutricoachtip.GenAiViewModel
import com.fit2081.bryanzongsheng33523355.data.patient.Patient
import com.fit2081.bryanzongsheng33523355.data.patient.PatientViewModel
import com.fit2081.bryanzongsheng33523355.data.network.NutritionData
import com.fit2081.bryanzongsheng33523355.data.network.ResponseModel
import com.fit2081.bryanzongsheng33523355.ui.theme.BryanZongSheng33523355Theme
import kotlinx.coroutines.launch
import androidx.core.content.edit


class HomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BryanZongSheng33523355Theme {
                val viewModel: PatientViewModel = ViewModelProvider(
                    this, PatientViewModel.PatientViewModelFactory(this@HomeScreenActivity)
                )[PatientViewModel::class.java]

                val foodIntakeViewModel: FoodIntakeViewModel = ViewModelProvider(
                    this, FoodIntakeViewModel.FoodIntakeViewModelFactory(this@HomeScreenActivity)
                )[FoodIntakeViewModel::class.java]


                val genAiViewModel: GenAiViewModel = ViewModelProvider(
                    this, GenAiViewModel.GenAiViewModelFactory(this@HomeScreenActivity)
                )[GenAiViewModel::class.java]

                val navController: NavHostController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        MyBottomBar(navController)
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MyNavHost(
                            innerPadding,
                            navController,
                            viewModel,
                            genAiViewModel,
                            foodIntakeViewModel
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun MyNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    viewModel: PatientViewModel,
    genAiViewModel: GenAiViewModel,
    foodIntakeViewModel: FoodIntakeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable("Home") {
            HomeScreen(innerPadding, viewModel, navController)
        }

        composable("Insight") {
            InsightScreen(innerPadding, viewModel, navController)
        }

        composable("NutriCoach") {
            NutriCoachScreen(innerPadding, viewModel, genAiViewModel, foodIntakeViewModel)
        }

        composable("Setting") {
            SettingScreen(innerPadding, viewModel, navController)
        }

        composable("ClinicianLogin") {
            ClinicianLoginScreen(navController)
        }

        composable("ClinicianDashboard") {
            ClinicianDashboardScreen(viewModel, genAiViewModel, navController)
        }

        composable("Stats") {
            ComparisonScreen(viewModel)
        }
    }
}

@Composable
fun MyBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        "Home",
        "Insight",
        "NutriCoach",
        "Stats",
        "Setting"
    )

    val selectedItem = items.indexOfFirst { it == currentRoute }.takeIf { it >= 0 } ?: 0

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insight" -> Icon(Icons.Default.BarChart, contentDescription = "Insight")
                        "NutriCoach" -> Icon(Icons.Filled.Person, contentDescription = "NutriCoach")
                        "Stats" -> Icon(Icons.Filled.Analytics, contentDescription = "Stats")
                        "Setting" -> Icon(Icons.Filled.Settings, contentDescription = "Setting")

                    }
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item)
                    {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }

            )
        }
    }
}

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: PatientViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current

    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0

    // State variables
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        patient = viewModel.getPatientById(userId)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Hello, ${patient?.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "You've already filled in your Food Intake Questionnaire, but you can change the details here:",
                    modifier = Modifier.weight(0.7f),
                    fontSize = 12.sp
                )
                Button(
                    onClick = {
                        val intent = Intent(context, QuestionnaireActivity::class.java)
                        context.startActivity(intent)
                    }, modifier = Modifier.weight(0.2f)
                ) {
                    Text("Edit")
                }
            }

            Image(
                painter = painterResource(R.drawable.home_screen_img),
                contentDescription = "Home Screen logo",
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(text = "My Score", fontSize = 25.sp, modifier = Modifier.weight(1f))

                TextButton(
                    onClick = { navController.navigate("Insight") }
                ) {
                    Text("See all scores >")
                }
            }


            Row {
                Text(
                    text = "Your Food Quality Score: ",
                    fontSize = 20.sp
                )
                Spacer(Modifier.width(24.dp))
                Text(
                    text = "${patient?.totalScore ?: 0f}/100",
                    fontSize = 16.sp,
                    color = Color(0xFF006400),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(30.dp))

            Text(text = "What is Food Quality Score?", fontSize = 25.sp)
            Text(
                text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines," +
                        " helping you identify both strengths and opportunities for improvement in your diet.",
                fontSize = 12.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "This personalized measurement considers various food groups including vegetables, " +
                        "fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
                fontSize = 12.sp
            )

        }
    }
}

@Composable
fun InsightScreen(
    innerPadding: PaddingValues,
    viewModel: PatientViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current

    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0

    var shareText by remember { mutableStateOf("") }

    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        patient = viewModel.getPatientById(userId)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Insights: Food Score",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // vegetables
            NutritionScoreRow(
                label = "Vegetables",
                score = patient?.vegetableScore ?: 0f,
                maxScore = 10f
            )

            // fruits
            NutritionScoreRow(
                label = "Fruits",
                score = patient?.fruitScore ?: 0f,
                maxScore = 10f
            )

            // grains and cereals
            NutritionScoreRow(
                label = "Grains and Cereal",
                score = patient?.grainCerealScore ?: 0f,
                maxScore = 10f
            )

            // meat and alternatives
            NutritionScoreRow(
                label = "Meat and Alternatives",
                score = patient?.meatAndAlternativesScore ?: 0f,
                maxScore = 10f
            )

            // dairy
            NutritionScoreRow(
                label = "Dairy and Alternatives",
                score = patient?.dairyScore ?: 0f,
                maxScore = 10f
            )

            // water
            NutritionScoreRow(
                label = "Water",
                score = patient?.waterScore ?: 0f,
                maxScore = 5f
            )

            // fats
            NutritionScoreRow(
                label = "Fats",
                score = patient?.unsaturatedFatsScore ?: 0f,
                maxScore = 10f
            )

            // sodium
            NutritionScoreRow(
                label = "Sodium",
                score = patient?.sodiumScore ?: 0f,
                maxScore = 10f
            )

            // sugar
            NutritionScoreRow(
                label = "Added Sugars",
                score = patient?.sugarScore ?: 0f,
                maxScore = 10f
            )

            // alcohol
            NutritionScoreRow(
                label = "Alcohol",
                score = patient?.alcoholScore ?: 0f,
                maxScore = 5f
            )

            // discretionary food
            NutritionScoreRow(
                label = "Discretionary Foods",
                score = patient?.discretionaryFoodScore ?: 0f,
                maxScore = 10f
            )

            Spacer(modifier = Modifier.height(30.dp))

            val totalScore = patient?.totalScore ?: 0f
            // total score
            Text(text = "Total Food Quality Score", fontSize = 20.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { totalScore / 100f },
                    modifier = Modifier.width(250.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text("${totalScore}/100", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            shareText = "Vegetable Score: ${patient?.vegetableScore} / 10\n" +
                    "Fruit Score: ${patient?.fruitScore} / 10\n" +
                    "Grain and Cereal Score: ${patient?.grainCerealScore} / 10\n" +
                    "Meat and Alternative Score: ${patient?.meatAndAlternativesScore} / 10\n" +
                    "Dairy and Alternative Score: ${patient?.dairyScore} / 10\n" +
                    "Water Score: ${patient?.waterScore} / 5\n" +
                    "Fat Score: ${patient?.unsaturatedFatsScore} / 10\n" +
                    "Sodium Score: ${patient?.sodiumScore} / 10\n" +
                    "Sugar Score: ${patient?.sugarScore} / 10\n" +
                    "Alcohol Score: ${patient?.alcoholScore} / 5\n" +
                    "Discretionary food Score: ${patient?.discretionaryFoodScore} / 10\n" +
                    "Total Score: ${patient?.totalScore} / 100"

            Button(
                onClick = {
                    val shareIntent = Intent(ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                    context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text("Share with someone")
            }

            Button(
                onClick = {
                    navController.navigate("NutriCoach")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text("Improve my diet!")
            }
        }
    }
}

@Composable
fun NutriCoachScreen(
    innerPadding: PaddingValues,
    viewModel: PatientViewModel,
    genAiViewModel: GenAiViewModel,
    foodIntakeViewModel: FoodIntakeViewModel
) {

    val repository: FruitRepository = FruitRepository()
    val coroutineScope = rememberCoroutineScope()

    val emptyFruit = ResponseModel(
        id = 0,
        genus = "",
        name = "",
        family = "",
        order = "",
        nutritions = NutritionData(
            calories = 0.0f,
            protein = 0.0f,
            fat = 0.0f,
            sugar = 0.0f,
            carbohydrates = 0.0f
        )
    )

    var fruitName by remember { mutableStateOf("") }
    var fruitData by remember { mutableStateOf(emptyFruit) }
    var isLoading by remember { mutableStateOf(false) }
    var fruitError by remember { mutableStateOf(false) }
    var isLoadingImage by remember { mutableStateOf(false) }
    val imageUrl by remember { mutableStateOf("https://picsum.photos/400/400?random=${System.currentTimeMillis()}") }
    var isLoadingPatient by remember { mutableStateOf(true) }

    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0
    var patient by remember { mutableStateOf<Patient?>(null) }
    var foodIntakes by remember { mutableStateOf<FoodIntake?>(null) }

    LaunchedEffect(userId) {
        patient = viewModel.getPatientById(userId)
        foodIntakes = foodIntakeViewModel.getFoodIntakeByPatient(userId)
        isLoadingPatient = false
    }

    val fruitServeSize = patient?.fruitServeSize ?: 0f
    val fruitVariationsScore = patient?.fruitVariationsScore ?: 0f


    // ai
    val tips by genAiViewModel.getUserTips().collectAsState(initial = emptyList())

    var result by remember { mutableStateOf("") }
    val uiState by genAiViewModel.uiTipState.collectAsState()
    val prompt =
        """Generate a short motivational message that encourages someone to improve their food intake. 
        Add THREE emojis too. Generate a different message everytime I prompt this. Within ONE paragraph.
        Patient data:
        - ID: ${patient?.userId ?: "None"}
        - Name: ${patient?.name ?: "None"}
        - Sex: ${patient?.sex ?: "None"}
        - Fruit Serve Size: ${patient?.fruitServeSize ?: 0}/5
        - Fruit Variations Score: ${patient?.fruitVariationsScore ?: 0}/5
        - Vegetable Score: ${patient?.vegetableScore ?: 0}/10
        - Grain and Cereal Score: ${patient?.grainCerealScore ?: 0}/10
        - Meat and Alternatives Score: ${patient?.meatAndAlternativesScore ?: 0}/10
        - Dairy Score: ${patient?.dairyScore ?: 0}/10
        - Water Score: ${patient?.waterScore ?: 0}/5
        - Fats Score: ${patient?.unsaturatedFatsScore ?: 0}/10
        - Sodium Score: ${patient?.sodiumScore ?: 0}/10
        - Sugar Score: ${patient?.sugarScore ?: 0}/10
        - Alcohol Score: ${patient?.alcoholScore ?: 0}/5
        - Discretionary foods Score: ${patient?.discretionaryFoodScore ?: 0}/10
        - Total HEIFA Score: ${patient?.totalScore ?: 0}/100
        Food Intake data:
        - Fruits Eaten: ${foodIntakes?.fruitsChecked ?: false}
        - Vegetables Eaten: ${foodIntakes?.vegetablesChecked ?: false}
        - Grains Eaten: ${foodIntakes?.grainsChecked ?: false}
        - Proteins Eaten:
          - Red Meat: ${foodIntakes?.redMeatChecked ?: false}
          - Seafood: ${foodIntakes?.seafoodChecked ?: false}
          - Poultry: ${foodIntakes?.poultryChecked ?: false}
          - Fish: ${foodIntakes?.fishChecked ?: false}
          - Eggs: ${foodIntakes?.eggChecked ?: false}
          - Nuts/Seeds: ${foodIntakes?.nutsSeedsChecked ?: false}
        - Persona: ${foodIntakes?.selectedPersona ?: "None"}
        - Biggest Meal Time: ${foodIntakes?.biggestMealTime ?: "None"}
        - Sleep Time: ${foodIntakes?.sleepTime ?: "None"}
        - Wake Time: ${foodIntakes?.wakeUpTime ?: "None"}
        """.trimMargin()

    var showHistoryDialog by remember { mutableStateOf(false) }

    if (isLoadingPatient) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("NutriCoach", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                if (fruitServeSize < 2 || fruitVariationsScore < 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // search bar
                        OutlinedTextField(
                            value = fruitName,
                            onValueChange = { fruitName = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            label = { Text("Enter a fruit") },
                            modifier = Modifier.weight(0.6f),
                            singleLine = true
                        )


                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    fruitError = false
                                    try {
                                        fruitData = repository.getFruitData(fruitName) ?: emptyFruit
                                    } catch (e: Exception) {
                                        fruitError = true
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(0.4f),
                            enabled = !isLoading
                        ) {
                            Text("Details")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (fruitError) {
                            Text(
                                "Fruit not found.",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }


                    // nutrition values
                    fruitData.let { fruit ->
                        NutritionRow("family", fruit.family)
                        NutritionRow("calories", "${fruit.nutritions.calories}")
                        NutritionRow("fat", "${fruit.nutritions.fat}")
                        NutritionRow("sugar", "${fruit.nutritions.sugar}")
                        NutritionRow("carbohydrates", "${fruit.nutritions.carbohydrates}")
                        NutritionRow("protein", "${fruit.nutritions.protein}")


                    }
                } else {
                    if (imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Random Image",
                            modifier = Modifier.size(300.dp),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                isLoadingImage = true
                            },
                            onSuccess = {
                                isLoadingImage = false
                            },
                            onError = {
                                isLoadingImage = false
                            }
                        )
                        if (isLoadingImage) {
                            CircularProgressIndicator()
                        }
                    }

                }
                Spacer(modifier = Modifier.height(30.dp))

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // AI tips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(
                        onClick = {
                            genAiViewModel.sendPrompt(
                                if (tips.isNotEmpty()) {
                                    val previousTips =
                                        tips.joinToString("\n") { it.message.toString() }
                                    "$prompt\n DO NOT repeat these tips: \n $previousTips"
                                } else {
                                    prompt
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(0.6f),
                        enabled = uiState !is UiState.Loading
                    ) {
                        Text("Motivational Message (AI)")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // pop up dialog
                if (showHistoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showHistoryDialog = false },
                        title = {
                            Text("AI Tips", fontSize = 18.sp, textAlign = TextAlign.Center)
                        },
                        text = {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (tips.isEmpty()) {
                                    item {
                                        Text("No messages yet.")
                                    }
                                } else {
                                    items(tips.size) { index ->
                                        val aiMessage = tips[index]
                                        Card(
                                            elevation = CardDefaults.cardElevation(4.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                        ) {
                                            Text("${aiMessage.message}", fontSize = 14.sp, modifier = Modifier.padding(12.dp))
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showHistoryDialog = false }) {
                                Text("Close")
                            }
                        }
                    )
                }

                // ai tip message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    if (uiState is UiState.Loading) {
                        CircularProgressIndicator()
                    } else {
                        var textColor = MaterialTheme.colorScheme.onSurface
                        if (uiState is UiState.Error) {
                            textColor = MaterialTheme.colorScheme.error
                            result = (uiState as UiState.Error).errorMessage
                        } else if (uiState is UiState.Success) {
                            textColor = MaterialTheme.colorScheme.onSurface
                            result = (uiState as UiState.Success).outputText

                        }
                        Text(
                            text = result,
                            color = textColor,
                            modifier = Modifier.align(Alignment.TopStart)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            ) {
                Button(
                    onClick = { showHistoryDialog = true }
                ) {
                    Text("Show All Tips")
                }
            }
        }
    }
}

@Composable
fun SettingScreen(
    innerPadding: PaddingValues,
    viewModel: PatientViewModel,
    navController: NavHostController
) {

    val context = LocalContext.current
    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        patient = viewModel.getPatientById(userId)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ACCOUNT",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingOption(
                title = "Name",
                desc = patient?.name ?: "None",
                icon = Icons.Default.Person
            ) {}

            SettingOption(
                title = "Phone Number",
                desc = patient?.phoneNumber ?: "None",
                icon = Icons.Default.Phone
            ) {}

            SettingOption(
                title = "User ID",
                desc = "${patient?.userId}",
                icon = Icons.Default.AccountBox
            ) {}

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "OTHER SETTINGS",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingOption(
                title = "Logout",
                desc = "",
                icon = Icons.AutoMirrored.Filled.Logout,
                onClick = {
                    AuthManager.logout()
                    val sharedPref =
                        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit { remove("user_id") }
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Logout"
                )
            }

            SettingOption(
                title = "Clinician Login",
                desc = "",
                icon = Icons.Default.Person,
                onClick = {
                    navController.navigate("ClinicianLogin")
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Clinician Login"
                )
            }

        }
    }
}

@Composable
fun ComparisonScreen(
    viewModel: PatientViewModel
) {

    val userId = AuthManager.getPatientId()?.toIntOrNull() ?: 0
    var patient by remember { mutableStateOf<Patient?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    val averageFruitScore by viewModel.averageFruitScore.collectAsState(initial = null)
    val averageVegetableScore by viewModel.averageVegetableScore.collectAsState(initial = null)
    val averageGrainCerealScore by viewModel.averageGrainCerealScore.collectAsState(initial = null)
    val averageMeatAndAlternativesScore by viewModel.averageMeatAndAlternativesScore.collectAsState(
        initial = null
    )
    val averageDairyScore by viewModel.averageDairyScore.collectAsState(initial = null)
    val averageWaterScore by viewModel.averageWaterScore.collectAsState(initial = null)
    val averageUnsaturatedFatsScore by viewModel.averageUnsaturatedFatsScore.collectAsState(initial = null)
    val averageSodiumScore by viewModel.averageSodiumScore.collectAsState(initial = null)
    val averageSugarScore by viewModel.averageSugarScore.collectAsState(initial = null)
    val averageAlcoholScore by viewModel.averageAlcoholScore.collectAsState(initial = null)
    val averageDiscretionaryFoodScore by viewModel.averageDiscretionaryFoodScore.collectAsState(
        initial = null
    )
    val averageTotalScore by viewModel.averageTotalScore.collectAsState(initial = null)

    var allPatients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var displayMessage by remember { mutableStateOf("") }

    val totalPercentage = patient?.let { currentPatient ->
        val totalPatients = allPatients.size
        val patientsBeaten = allPatients.count { it.totalScore <= currentPatient.totalScore }
        patientsBeaten.toFloat() / totalPatients.coerceAtLeast(1) * 100
    } ?: 0f

    displayMessage = if (totalPercentage >= 90) {
        "Congratulations, you are in the Top 10%!"
    } else if (totalPercentage >= 70) {
        "Well done, you have a higher food score than most users!"
    } else if (totalPercentage >= 50) {
        "Wow, your food score is above average!"
    } else {
        "Your food score may need some improvements."
    }


    LaunchedEffect(userId) {
        patient = viewModel.getPatientById(userId)
        allPatients = viewModel.getAllPatientsOnce()
        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState), verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Statistics", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text(displayMessage)
            Text("You have a higher food score than")
            Text(
                "%.2f %% users".format(totalPercentage),
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD81B60)
            )

            ScoreComparisonChart(
                label = "Fruit",
                userScore = patient?.fruitScore ?: 0f,
                averageScore = averageFruitScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Vegetable",
                userScore = patient?.vegetableScore ?: 0f,
                averageScore = averageVegetableScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Grain and Cereal",
                userScore = patient?.grainCerealScore ?: 0f,
                averageScore = averageGrainCerealScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Meat and Alternatives",
                userScore = patient?.meatAndAlternativesScore ?: 0f,
                averageScore = averageMeatAndAlternativesScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Dairy",
                userScore = patient?.dairyScore ?: 0f,
                averageScore = averageDairyScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Water",
                userScore = patient?.waterScore ?: 0f,
                averageScore = averageWaterScore ?: 0f,
                maxScore = 5
            )

            ScoreComparisonChart(
                label = "Healthy Fats",
                userScore = patient?.unsaturatedFatsScore ?: 0f,
                averageScore = averageUnsaturatedFatsScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Sodium",
                userScore = patient?.sodiumScore ?: 0f,
                averageScore = averageSodiumScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Sugar",
                userScore = patient?.sugarScore ?: 0f,
                averageScore = averageSugarScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Alcohol",
                userScore = patient?.alcoholScore ?: 0f,
                averageScore = averageAlcoholScore ?: 0f,
                maxScore = 5
            )

            ScoreComparisonChart(
                label = "Discretionary Food",
                userScore = patient?.discretionaryFoodScore ?: 0f,
                averageScore = averageDiscretionaryFoodScore ?: 0f,
                maxScore = 10
            )

            ScoreComparisonChart(
                label = "Total",
                userScore = patient?.totalScore ?: 0f,
                averageScore = averageTotalScore ?: 0f,
                maxScore = 100
            )
        }
    }
}

@Composable
fun ScoreComparisonChart(
    label: String,
    userScore: Float,
    averageScore: Float,
    maxScore: Int,
    modifier: Modifier = Modifier
) {
    val barWidth = 80.dp
    val chartHeight = 200.dp
    val barSpacing = 32.dp

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with all values
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Score")
                    Text(
                        "%.1f".format(userScore),
                        color = Color(0xFFD81B60),
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Average")
                    Text(
                        "%.1f".format(averageScore),
                        color = Color(0xFF800000),
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Max")
                    Text(maxScore.toString(), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(barSpacing),
                modifier = Modifier.height(chartHeight)
            ) {
                // my score bar
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(
                        modifier = Modifier
                            .width(barWidth)
                            .fillMaxHeight()
                    ) {
                        val barHeight = size.height * (userScore / maxScore)
                        drawRoundRect(
                            color = Color(0xFFD81B60),
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(size.width, barHeight)
                        )
                    }
                    Text("You", modifier = Modifier.padding(top = 8.dp))
                }

                // Average score bar
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Canvas(
                        modifier = Modifier
                            .width(barWidth)
                            .fillMaxHeight()
                    ) {
                        val barHeight = size.height * (averageScore / maxScore)
                        drawRoundRect(
                            color = Color(0xFF800000),
                            topLeft = Offset(0f, size.height - barHeight),
                            size = Size(size.width, barHeight)
                        )
                    }
                    Text("Average", modifier = Modifier.padding(top = 8.dp))
                }
            }

            // legend
            Text(
                text = "$label Score Comparison",
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun NutritionScoreRow(
    label: String,
    score: Float,
    maxScore: Float,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(0.4f), fontSize = 14.sp)
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = { score / maxScore },
            modifier = Modifier.width(180.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$score / $maxScore",
            modifier = Modifier.weight(0.3f),
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun NutritionRow(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.4f))
            Text(": $value", modifier = Modifier.weight(0.6f))
        }
    }
}


@Composable
fun SettingOption(
    title: String,
    desc: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,  // Add onClick parameter
    trailingContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() },  // Make whole row clickable if onClick provided
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = desc,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
        trailingContent()
    }
}