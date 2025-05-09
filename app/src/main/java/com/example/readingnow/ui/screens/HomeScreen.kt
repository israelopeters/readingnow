package com.example.readingnow.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose.ReadingNowTheme
import com.example.readingnow.R
import com.example.readingnow.data.ApiRepository
import com.example.readingnow.service.UserViewModel
import io.ktor.client.HttpClient

@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.welcome_screen_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text("Coming soon...")
        }
    }
}


// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7_PRO,
    uiMode = UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7_PRO,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun HomeScreenPreview() {
    ReadingNowTheme {
        HomeScreen(userViewModel = UserViewModel(ApiRepository(HttpClient())))
    }
}