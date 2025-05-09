package com.example.readingnow.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ReadingNowTheme
import com.example.readingnow.R
import com.example.readingnow.data.ApiRepository
import com.example.readingnow.service.AuthMode
import com.example.readingnow.service.UserUiState
import com.example.readingnow.service.UserViewModel
import com.example.readingnow.ui.components.IndeterminateCircularIndicator
import io.ktor.client.HttpClient

private const val TAG: String = "ReadingNowActivity"

@Composable
fun WelcomeScreen(
    onContinueClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    when (userViewModel.currentUser.authMode) {

        AuthMode.BUSY ->
            Box(modifier) {
                Image(
                    painter = painterResource(R.drawable.welcome_screen_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                IndeterminateCircularIndicator()
            }

        AuthMode.SIGNED_IN -> SignInSuccess(
            userState = userViewModel.currentUser,
            onContinueClicked = onContinueClicked
        )

        AuthMode.SIGNED_OUT ->
            Box(modifier) {
                Image(
                    painter = painterResource(R.drawable.welcome_screen_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    AppDetails()

                    AppSignIn(
                        userViewModel,
                        onSignInClicked = {
                            Log.v(
                                TAG,
                                "Welcome Screen - Before logging in: ${userViewModel.currentUser}"
                            )
                            Log.v(
                                TAG,
                                "Welcome Screen - Before logging in: ${userViewModel.userEmail}"
                            )
                            userViewModel.getUser()
                            Log.v(
                                TAG,
                                "Welcome Screen - After logging in: ${userViewModel.currentUser}"
                            )
                        }
                    )

                    AppSignUp(onSignUpClicked = onSignUpClicked)
                }
            }
    }
}


@Composable
fun AppDetails(
    modifier: Modifier = Modifier,
    @StringRes screenHeader: Int = R.string.readingnow,
    @StringRes headerSubtitle: Int = R.string.welcome_subtitle,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 48.dp)
    ) {
        Text(
            text = stringResource(screenHeader),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.paddingFromBaseline(
                top = 8.dp,
                bottom = 8.dp
            )
        )
        Text(
            text = stringResource(headerSubtitle),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun AppSignIn(
    userViewModel: UserViewModel,
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.padding(24.dp)
    ) {
        OutlinedTextField(
            value = userViewModel.userEmail,
            onValueChange = { userViewModel.updateEmail(it) },
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = userViewModel.userPassword,
            onValueChange = { userViewModel.updatePassword(it) },
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSignInClicked,
            enabled = checkFormValidity(userViewModel.userEmail, userViewModel.userPassword),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.sign_in),
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    Color.Unspecified
                }
            )
        }
    }
}

fun checkFormValidity(email: String?, password: String?): Boolean {
    return email?.isNotEmpty() == true && password?.isNotEmpty() == true
}

@Composable
fun AppSignUp(
    onSignUpClicked: () -> Unit,
    modifier: Modifier =  Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(top = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.not_yet_registered)
        )

        TextButton (
            onClick = onSignUpClicked,
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    Color.Unspecified
                }
            )
        }
    }
}

@Composable
fun SignInSuccess(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier,
    userState: UserUiState
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
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Welcome, ${userState.firstName}!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.ready_to_see_what_others_are_reading),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedButton(
                onClick = onContinueClicked,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Continue",
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
fun WelcomeScreenPreview() {
    ReadingNowTheme {
        WelcomeScreen(
            userViewModel = UserViewModel(ApiRepository(HttpClient())),
            onContinueClicked = { },
            onSignUpClicked = { }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewDark"
)
@Composable
fun AppDetailsPreview() {
    ReadingNowTheme {
        AppDetails()
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewDark"
)
@Composable
fun AppLoginPreview() {
    ReadingNowTheme {
        AppSignIn(
            UserViewModel(ApiRepository(HttpClient())),
            onSignInClicked = { }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_7_PRO,
    name = "DefaultPreviewDark"
)
@Composable
fun AppSignUpPreview() {
    ReadingNowTheme {
        AppSignUp(
            onSignUpClicked = { }
        )
    }
}

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
fun SignInSuccessPreview() {
    ReadingNowTheme {
        SignInSuccess(
            onContinueClicked = { },
            userState = UserUiState()
        )
    }
}