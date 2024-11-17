package com.aveek.badgetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.aveek.badgetest.ui.theme.BadgeTestTheme
import com.aveek.badgetest.util.BadgeManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val notificationChannel = NotificationChannel("channelId", "channelName",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.setShowBadge(true)
        val noticationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        noticationManager.createNotificationChannel(notificationChannel)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                askNotificationPermission(this@MainActivity)
            }
        }

        setContent {
            BadgeTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BadgeDemoView()
                }
            }
        }
    }



    private fun askNotificationPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
private fun BadgeDemoView() {
    val badgeManager = BadgeManager()
    var showBadge by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Text(
            text = if (showBadge)"Badge is shown" else "Badge is hidden",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        val context = LocalContext.current
        Switch(
            checked = showBadge,
            onCheckedChange = {
                showBadge = it
                badgeManager.toggleBadge(context = context, showBadge = it)
            }
        )
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
    BadgeTestTheme {
        Greeting("Android")
    }
}