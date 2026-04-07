package com.adam0006.mobpro1.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adam0006.mobpro1.R
import com.adam0006.mobpro1.navigation.Screen
import com.adam0006.mobpro1.ui.theme.Mobpro1Theme
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi)
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(
            modifier = Modifier.padding(padding),
            context = context
        )
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    context: Context
) {
    var berat by rememberSaveable { mutableStateOf("") }
    var beratError by rememberSaveable { mutableStateOf(false) }

    var tinggi by rememberSaveable { mutableStateOf("") }
    var tinggiError by rememberSaveable { mutableStateOf(false) }

    val radioOptions = listOf(
        stringResource(id = R.string.pria),
        stringResource(id = R.string.wanita)
    )
    var gender by rememberSaveable { mutableStateOf(radioOptions[0]) }

    var bmi by rememberSaveable { mutableFloatStateOf(0f) }
    var kategori by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.bmi_intro),
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it },
            label = { Text(stringResource(id = R.string.berat_badan)) },
            trailingIcon = { IconPicker(beratError, "kg") },
            supportingText = { ErrorHint(beratError) },
            isError = beratError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = tinggi,
            onValueChange = { tinggi = it },
            label = { Text(stringResource(id = R.string.tinggi_badan)) },
            trailingIcon = { IconPicker(tinggiError, "cm") },
            supportingText = { ErrorHint(tinggiError) },
            isError = tinggiError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
        ) {
            radioOptions.forEach { text ->
                GenderOption(
                    label = text,
                    selected = (gender == text),
                    modifier = Modifier
                        .selectable(
                            selected = (gender == text),
                            role = Role.RadioButton,
                            onClick = { gender = text }
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }

        Button(
            onClick = {
                beratError = berat.isEmpty() || berat == "0"
                tinggiError = tinggi.isEmpty() || tinggi == "0"

                if (beratError || tinggiError) return@Button

                bmi = hitungBmi(berat.toFloat(), tinggi.toFloat())
                kategori = getKategori(bmi, gender == radioOptions[0])
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(stringResource(id = R.string.hitung))
        }

        if (bmi != 0f) {

            val message = stringResource(
                id = R.string.bagikan_template,
                berat,
                tinggi,
                gender,
                bmi,
                stringResource(id = kategori)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = stringResource(id = R.string.bmi_x, bmi),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(id = kategori).uppercase(),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    shareData(context, message)
                },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.bagikan))
            }
        }
    }
}

@Composable
fun GenderOption(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    } else {
        Text(unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(
            text = stringResource(id = R.string.input_invalid),
            color = MaterialTheme.colorScheme.error
        )
    }
}

private fun hitungBmi(berat: Float, tinggi: Float): Float {
    return berat / (tinggi / 100).pow(2)
}

private fun getKategori(bmi: Float, isMale: Boolean): Int {
    return if (isMale) {
        when {
            bmi < 20.5 -> R.string.kurus
            bmi < 27.0 -> R.string.ideal
            else -> R.string.gemuk
        }
    } else {
        when {
            bmi < 18.5 -> R.string.kurus
            bmi < 25.0 -> R.string.ideal
            else -> R.string.gemuk
        }
    }
}

private fun shareData(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Mobpro1Theme {
        MainScreen(rememberNavController())
    }
}