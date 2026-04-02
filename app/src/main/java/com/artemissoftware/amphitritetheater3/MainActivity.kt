package com.artemissoftware.amphitritetheater3

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
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.amphitritetheater3.pager.Carousel
import com.artemissoftware.amphitritetheater3.pager.Images
import com.artemissoftware.amphitritetheater3.ui.theme.AmphitriteTheater3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmphitriteTheater3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Carousel(items = Images.cardImages, infiniteScroll = false)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmphitriteTheater3Theme {
        Greeting("Android")
    }
}