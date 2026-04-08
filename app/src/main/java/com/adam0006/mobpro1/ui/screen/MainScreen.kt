package com.adam0006.mobpro1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adam0006.mobpro1.R
import com.adam0006.mobpro1.navigation.Screen
import com.adam0006.mobpro1.ui.theme.Mobpro1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var panjang by rememberSaveable { mutableStateOf("") }
    var lebar by rememberSaveable { mutableStateOf("") }
    var panjangError by rememberSaveable { mutableStateOf(false) }
    var lebarError by rememberSaveable { mutableStateOf(false) }

    var luas by rememberSaveable { mutableFloatStateOf(0f) }
    var keliling by rememberSaveable { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.intro_challenge),
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = panjang,
            onValueChange = { panjang = it; panjangError = false },
            label = { Text(stringResource(R.string.panjang)) },
            isError = panjangError,
            supportingText = { if (panjangError) Text(stringResource(R.string.input_invalid)) },
            trailingIcon = { if (panjangError) Icon(Icons.Filled.Warning, null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = lebar,
            onValueChange = { lebar = it; lebarError = false },
            label = { Text(stringResource(R.string.lebar)) },
            isError = lebarError,
            supportingText = { if (lebarError) Text(stringResource(R.string.input_invalid)) },
            trailingIcon = { if (lebarError) Icon(Icons.Filled.Warning, null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                panjangError = (panjang.isBlank() || panjang == "0")
                lebarError = (lebar.isBlank() || lebar == "0")

                if (panjangError || lebarError) return@Button

                luas = hitungLuas(panjang.toFloat(), lebar.toFloat())
                keliling = hitungKeliling(panjang.toFloat(), lebar.toFloat())
            }) {
                Text(stringResource(R.string.hitung))
            }

            OutlinedButton(onClick = {
                panjang = ""
                lebar = ""
                panjangError = false
                lebarError = false
                luas = 0f
                keliling = 0f
            }) {
                Text(stringResource(R.string.reset))
            }
        }

        if (luas != 0f) {
            val context = LocalContext.current

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )

            Text(
                text = stringResource(R.string.luas_x, luas),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(R.string.keliling_x, keliling),
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = {
                    val message = """
📐 Hasil Perhitungan Persegi Panjang

📏 Panjang: $panjang
📏 Lebar: $lebar

🟦 Luas: $luas
🔲 Keliling: $keliling
                    """.trimIndent()

                    shareData(context, message)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Bagikan")
            }
        }
    }
}

private fun shareData(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(
        Intent.createChooser(intent, "Bagikan via")
    )
}

private fun hitungLuas(p: Float, l: Float): Float = p * l
private fun hitungKeliling(p: Float, l: Float): Float = 2 * (p + l)

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Mobpro1Theme {
        MainScreen(rememberNavController())
    }
}