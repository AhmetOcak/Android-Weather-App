package com.ahmetocak.android_weather_app

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ahmetocak.android_weather_app.data.WeatherRepository
import com.ahmetocak.android_weather_app.feature.home.HomeScreenFragmentDirections
import com.ahmetocak.android_weather_app.feature.permission.PermissionScreenFragmentDirections
import com.ahmetocak.android_weather_app.util.Location.requiredPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: WeatherRepository

    private lateinit var navController: NavController

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (it.key in requiredPermission && !it.value) {
                return@registerForActivityResult
            }
        }
        navController.navigate(
            PermissionScreenFragmentDirections.actionPermissionScreenFragmentToHomeScreenFragment()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        if (!requiredPermission.all { ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED}) {
            navController.navigate(
                HomeScreenFragmentDirections.actionHomeScreenFragmentToPermissionScreenFragment()
            )
            permissionLauncher.launch(requiredPermission)
        }
    }
}