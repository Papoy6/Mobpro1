package com.adam0006.mobpro1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.adam0006.mobpro1.R
import androidx.compose.ui.tooling.preview.Preview
import android.content.res.Configuration
import com.adam0006.mobpro1.ui.theme.Mobpro1Theme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var panjang by remember { mutableStateOf("") }
    var lebar by remember { mutableStateOf("") }
    var panjangError by remember { mutableStateOf(false) }
    var lebarError by remember { mutableStateOf(false) }

    var luas by remember { mutableFloatStateOf(0f) }
    var keliling by remember { mutableFloatStateOf(0f) }

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
                panjang = ""; lebar = ""
                panjangError = false; lebarError = false
                luas = 0f; keliling = 0f
            }) {
                Text(stringResource(R.string.reset))
            }
        }

        if (luas != 0f) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)
            Text(
                text = stringResource(R.string.luas_x, luas),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.keliling_x, keliling),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun hitungLuas(p: Float, l: Float): Float = p * l
private fun hitungKeliling(p: Float, l: Float): Float = 2 * (p + l)


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Mobpro1Theme {
        MainScreen()
    }
}