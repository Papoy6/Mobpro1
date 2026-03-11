package com.adam0006.mobpro1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adam0006.mobpro1.ui.theme.Mobpro1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobpro1Theme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Mobpro1Theme {
        MainScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var isLampOn by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) { innerPadding ->
        ScreenContent(
            isLampOn = isLampOn,
            modifier = Modifier.padding(innerPadding),
            onToggleLamp = { isLampOn = !isLampOn } // Higher-Order Function [cite: 246, 362]
        )
    }
}

@Composable
fun ScreenContent(isLampOn: Boolean, modifier: Modifier = Modifier, onToggleLamp: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageRes = if (isLampOn) R.drawable.lampu_nyala else R.drawable.lampu_mati
        val statusText = if (isLampOn) R.string.lampu_hidup else R.string.lampu_mati
        val buttonText = if (isLampOn) R.string.matikan else R.string.hidupkan

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(R.string.desc_lampu),
            modifier = Modifier.size(200.dp), // Ukuran bebas [cite: 196]
            contentScale = ContentScale.Fit
        )

        Text(
            text = stringResource(id = statusText),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = onToggleLamp,
            modifier = Modifier.fillMaxWidth(0.5f).padding(top = 24.dp),
        contentPadding = PaddingValues(16.dp)
        ) {
        Text(text = stringResource(id = buttonText))
    }
    }
}