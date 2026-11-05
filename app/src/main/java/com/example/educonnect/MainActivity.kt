package com.example.educonnect

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.educonnect.ui.screens.*
import com.example.educonnect.ui.theme.EduConnectTheme
import com.example.educonnect.ui.viewmodel.AuthViewModel
import com.example.educonnect.ui.viewmodel.ModuleViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("educonnect_lang_prefs", Context.MODE_PRIVATE)
        val savedTag = prefs.getString("lang_tag", null)

        val appLocales = if (!savedTag.isNullOrBlank()) {
            LocaleListCompat.forLanguageTags(savedTag)
        } else {
            AppCompatDelegate.getApplicationLocales()
        }

        val context = if (!appLocales.isEmpty) {
            val locale = appLocales.get(0) ?: java.util.Locale.getDefault()
            java.util.Locale.setDefault(locale)
            val configuration = newBase.resources.configuration
            val localeList = android.os.LocaleList.forLanguageTags(appLocales.toLanguageTags())
            configuration.setLocales(localeList)
            newBase.createConfigurationContext(configuration)
        } else {
            newBase
        }
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val moduleViewModel: ModuleViewModel = hiltViewModel()
            val selectedLang by moduleViewModel.selectedLanguage.collectAsState()
            val selectedTheme by moduleViewModel.selectedTheme.collectAsState()
            val context = LocalContext.current

            var initialLang by remember { mutableStateOf(selectedLang) }
            var initialTheme by remember { mutableStateOf(selectedTheme) }

            LaunchedEffect(selectedLang, selectedTheme) {
                if (initialLang != selectedLang || initialTheme != selectedTheme) {
                    initialLang = selectedLang
                    initialTheme = selectedTheme
                    (context as? android.app.Activity)?.recreate()
                }
            }

            val isDark = when (selectedTheme) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemInDarkTheme()
            }

            EduConnectTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val firebaseAuth = FirebaseAuth.getInstance()
                val startDestination = if (firebaseAuth.currentUser != null) "home" else "login"

                Scaffold(
                    bottomBar = {
                        val mainTabs = listOf("home", "courses", "notifications", "study", "profile")
                        if (currentRoute in mainTabs) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = { if (currentRoute != "home") navController.navigate("home") },
                                    icon = { Text("🏠", fontSize = 20.sp) },
                                    label = { Text(stringResource(R.string.nav_home)) }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "courses",
                                    onClick = { if (currentRoute != "courses") navController.navigate("courses") },
                                    icon = { Text("📚", fontSize = 20.sp) },
                                    label = { Text(stringResource(R.string.nav_courses)) }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "notifications",
                                    onClick = { if (currentRoute != "notifications") navController.navigate("notifications") },
                                    icon = { Text("🔔", fontSize = 20.sp) },
                                    label = { Text(stringResource(R.string.nav_notifications)) }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "study",
                                    onClick = { if (currentRoute != "study") navController.navigate("study") },
                                    icon = { Text("⏱️", fontSize = 20.sp) },
                                    label = { Text("Study") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "profile",
                                    onClick = { if (currentRoute != "profile") navController.navigate("profile") },
                                    icon = { Text("⚙️", fontSize = 20.sp) },
                                    label = { Text(stringResource(R.string.nav_settings)) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController, 
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                viewModel = authViewModel,
                                onNavigateToSettings = { navController.navigate("profile") },
                                onNavigateToCourseDetail = { subjectId -> navController.navigate("course_detail/$subjectId") }
                            )
                        }
                        composable("courses") {
                            CoursesScreen(
                                moduleViewModel = moduleViewModel,
                                onNavigateToDetail = { subjectId -> navController.navigate("course_detail/$subjectId") }
                            )
                        }
                        composable("course_detail/{subjectId}") { backStackEntry ->
                            val subjectId = backStackEntry.arguments?.getString("subjectId") ?: ""
                            CourseDetailScreen(
                                subjectId = subjectId,
                                moduleViewModel = moduleViewModel,
                                onBack = { navController.popBackStack() },
                                onTakeQuiz = {}
                            )
                        }
                        composable("notifications") {
                            NotificationsScreen(moduleViewModel = moduleViewModel)
                        }
                        composable("study") {
                            StudyTrackerScreen()
                        }
                        composable("profile") {
                            SettingsScreen(
                                moduleViewModel = moduleViewModel,
                                onNavigateToTerms = { navController.navigate("terms") },
                                onNavigateToAbout = { navController.navigate("about") },
                                onNavigateToPersonalInfo = { navController.navigate("personal_info") },
                                onNavigateToChangePassword = { navController.navigate("change_password") },
                                onNavigateToPrivacy = { navController.navigate("privacy") },
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("personal_info") {
                            PersonalInfoScreen(
                                authViewModel = authViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("change_password") {
                            ChangePasswordScreen(
                                authViewModel = authViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("privacy") {
                            PrivacySettingsScreen(
                                authViewModel = authViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("terms") {
                            TermsOfServiceScreen(onBack = { navController.popBackStack() })
                        }
                        composable("about") {
                            AboutEduConnectScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
