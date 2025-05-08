package com.example.readingnow.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ReadingNowTheme
import com.example.readingnow.R
import com.example.readingnow.data.ApiRepository
import com.example.readingnow.model.UserCreation
import com.example.readingnow.service.SignUpMode
import com.example.readingnow.service.UserViewModel
import com.example.readingnow.ui.components.IndeterminateCircularIndicator
import io.ktor.client.HttpClient

private const val TAG: String = "BookieBoardActivity"

@Composable
fun SignUpScreen(
    onSignInClicked: () -> Unit,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel
) {
    Box(modifier) {

        when (userViewModel.addedUser.signUpMode) {
            SignUpMode.PROGRESS ->
                Box(modifier) {
                    Image(
                        painter = painterResource(R.drawable.welcome_screen_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    IndeterminateCircularIndicator()
                }

            SignUpMode.ACTIVE -> SignUpSuccessScreen(onSignInClicked)

            SignUpMode.INACTIVE -> 
                Box(modifier) {
                    Image(
                        painter = painterResource(R.drawable.welcome_screen_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                    SignUpForm(
                        userViewModel = userViewModel,
                        onSignInClicked = onSignInClicked
                    )
                }
        }
    }
}

@Composable
fun SignUpForm(
    userViewModel: UserViewModel,
    onSignInClicked: () -> Unit,
    modifier: Modifier =  Modifier
) {
    var firstName: String by rememberSaveable { mutableStateOf("") }
    var lastName: String by rememberSaveable { mutableStateOf("") }
    var email: String by rememberSaveable { mutableStateOf("") }
    var password: String by rememberSaveable { mutableStateOf("") }
    var username: String by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.create_your_account),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.paddingFromBaseline(
                top = 8.dp,
                bottom = 8.dp
            )
        )
        HorizontalDivider(
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.first_name)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.last_name)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                Log.v(TAG,"SignUpScreen - Added user before network request --- ${userViewModel.addedUser}"
                )
                userViewModel.addNewUser(
                    UserCreation(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password,
                        username = username
                    )
                )
                Log.v(TAG,"Added user after network request --- ${userViewModel.addedUser}"
                )
            },
            enabled = isFormValid(email, password, firstName, lastName),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.inversePrimary
                } else {
                    Color.Unspecified
                }
            )
        }
        Text(
            text = stringResource(R.string.already_have_an_account),
            modifier = Modifier.padding(top = 24.dp)
        )
        TextButton (
            onClick = onSignInClicked
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

fun isFormValid(
    email: String?, password: String?, firstName: String?, lastName: String?
): Boolean {
    return email?.isNotEmpty() == true &&
            password?.isNotEmpty() == true &&
            firstName?.isNotEmpty() == true &&
            lastName?.isNotEmpty() == true
}

@Composable
fun SignUpSuccessScreen(
    onSignInClicked: () -> Unit,
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
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Account Created!",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Sign in to continue",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedButton(
                onClick = onSignInClicked,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Unspecified
                    })
            }
        }
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
fun SignUpScreenPreview() {
    ReadingNowTheme {
        SignUpScreen(
            userViewModel = UserViewModel(ApiRepository(HttpClient())),
            onSignInClicked = { }
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
fun SignUpSuccessScreenPreview() {
    ReadingNowTheme {
        SignUpSuccessScreen(
            onSignInClicked = { }
        )
    }
}