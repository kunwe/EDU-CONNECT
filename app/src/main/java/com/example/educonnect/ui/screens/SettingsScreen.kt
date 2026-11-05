package com.example.educonnect.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.educonnect.R
import com.example.educonnect.ui.viewmodel.ModuleViewModel
import com.example.educonnect.utils.LanguageManager
import com.example.educonnect.utils.ThemeManager

@Composable
fun SettingsScreen(
    moduleViewModel: ModuleViewModel,
    onNavigateToTerms: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onLogout: () -> Unit
) {
    val pushEnabled by moduleViewModel.pushNotificationsEnabled.collectAsState()
    val selectedLang by moduleViewModel.selectedLanguage.collectAsState()
    val selectedTheme by moduleViewModel.selectedTheme.collectAsState()
    val context = LocalContext.current

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.settings_title), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            // General Category
            item {
                SettingsCategory(title = stringResource(R.string.general_category)) {
                    SettingsRow(title = stringResource(R.string.language_label), value = selectedLang) {
                        showLanguageDialog = true
                    }
                    HorizontalDivider()
                    SettingsRow(title = stringResource(R.string.theme_label), value = selectedTheme) {
                        showThemeDialog = true
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.push_notifications), fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Switch(checked = pushEnabled, onCheckedChange = { moduleViewModel.setPushNotificationsEnabled(it) })
                    }
                }
            }

            // Account Category
            item {
                SettingsCategory(title = stringResource(R.string.account_category)) {
                    SettingsRow(title = stringResource(R.string.personal_info)) { onNavigateToPersonalInfo() }
                    HorizontalDivider()
                    SettingsRow(title = stringResource(R.string.change_password)) { onNavigateToChangePassword() }
                    HorizontalDivider()
                    SettingsRow(title = stringResource(R.string.privacy_settings)) { onNavigateToPrivacy() }
                }
            }

            // About Category
            item {
                SettingsCategory(title = stringResource(R.string.about_category)) {
                    SettingsRow(title = stringResource(R.string.terms_of_service)) { onNavigateToTerms() }
                    HorizontalDivider()
                    SettingsRow(title = stringResource(R.string.about_educonnect)) { onNavigateToAbout() }
                }
            }

            item {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = stringResource(R.string.logout), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.language_label)) },
            text = {
                Column {
                    listOf(
                        LanguageManager.LANG_ENGLISH,
                        LanguageManager.LANG_ZULU,
                        LanguageManager.LANG_AFRIKAANS,
                        LanguageManager.LANG_SESOTHO
                    ).forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    moduleViewModel.setSelectedLanguage(lang)
                                    showLanguageDialog = false
                                    (context as? Activity)?.recreate()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedLang == lang),
                                onClick = {
                                    moduleViewModel.setSelectedLanguage(lang)
                                    showLanguageDialog = false
                                    (context as? Activity)?.recreate()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = lang, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(stringResource(R.string.theme_label)) },
            text = {
                Column {
                    listOf(
                        ThemeManager.THEME_LIGHT,
                        ThemeManager.THEME_DARK,
                        ThemeManager.THEME_SYSTEM
                    ).forEach { theme ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    moduleViewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                    (context as? Activity)?.recreate()
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedTheme == theme),
                                onClick = {
                                    moduleViewModel.setSelectedTheme(theme)
                                    showThemeDialog = false
                                    (context as? Activity)?.recreate()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = theme, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SettingsCategory(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp, bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsRow(title: String, value: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(text = value, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
            }
            Text(text = "▶", color = Color.LightGray, fontSize = 12.sp)
        }
    }
}
