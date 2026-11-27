package com.nocturne.store

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nocturne.store.presentation.navigation.NocturneNavGraph
import com.nocturne.store.presentation.theme.NocturneStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NocturneStoreTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NocturneNavGraph()
                }
            }
        }
    }
}
