package com.example.readingnow.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ReadingNowTheme

@Composable
fun CustomBottomAppBar(
    onHomeClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onHomeClicked) {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = "Home Button",
                    )
                }
                IconButton(onClick = onProfileClicked) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = "Profile Button",
                    )
                }
            }
        },
        contentPadding = PaddingValues(8.dp)
    )
}

// Previews
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7_PRO,
    uiMode = UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLight"
)
@Preview(
    showBackground = true,
    device = Devices.PIXEL_7_PRO,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun HomeScreenPreview() {
    ReadingNowTheme {
        CustomBottomAppBar({ }, { })
    }
}