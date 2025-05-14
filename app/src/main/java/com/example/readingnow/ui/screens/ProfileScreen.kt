package com.example.readingnow.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.compose.ReadingNowTheme
import com.example.readingnow.R
import com.example.readingnow.data.ApiRepository
import com.example.readingnow.service.UserViewModel
import io.ktor.client.HttpClient

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onEditProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Image(
            painter = painterResource(R.drawable.welcome_screen_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(Modifier.height(150.dp))

            // Profile picture
            if (userViewModel.currentUser.profilePicUrl.isNullOrBlank()) {
                Image(
                    painter = painterResource(R.drawable.profile_default),
                    contentDescription = "Filler profile picture",
                    modifier = Modifier.size(150.dp).clip(shape = CircleShape),
                    alignment = Alignment.Center
                )
            } else {
                // Load image with Coil
                AsyncImage(
                    model = userViewModel.currentUser.profilePicUrl,
                    contentDescription = "Profile picture"
                )
            }

            // User details
            val firstName = userViewModel.currentUser.firstName
            val lastName = userViewModel.currentUser.lastName

            Text(
                text = "$firstName $lastName",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "@${firstName?.lowercase()}${lastName?.lowercase()}",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "I am a bookworm, so keep the atmosphere bookish!",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            // Edit profile button
            Button(
                onClick = onEditProfileClicked,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)
            ) {
                Text(
                    text = "Edit Profile",
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Unspecified
                    }
                )
            }
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
fun ProfileScreenPreview() {
    ReadingNowTheme {
        ProfileScreen(
            userViewModel = UserViewModel(ApiRepository(HttpClient())),
            { }
        )
    }
}