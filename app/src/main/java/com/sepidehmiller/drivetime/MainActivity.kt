package com.sepidehmiller.drivetime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sepidehmiller.drivetime.ui.drivelog.DriveLogScreen
import com.sepidehmiller.drivetime.ui.drivelogdetail.DriveLogDetailScreen
import com.sepidehmiller.drivetime.ui.driveloginput.DriveLogInputScreen
import com.sepidehmiller.drivetime.ui.navigation.NavDestinationArgs
import com.sepidehmiller.drivetime.ui.navigation.NavDestinations
import com.sepidehmiller.drivetime.ui.theme.DriveTimeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriveTimeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavDestinations.DRIVE_LOG_ROUTE) {
                    composable(NavDestinations.DRIVE_LOG_ROUTE) {
                        DriveLogScreen(
                            seeDetailsAction = { id ->
                                navController.navigate("${NavDestinations.DRIVE_LOG_DETAIL_ROUTE}/$id")
                            },
                            fabAction = { navController.navigate(NavDestinations.DRIVE_LOG_INPUT_ROUTE) }
                        )
                    }
                    composable(
                        route = "${NavDestinations.DRIVE_LOG_DETAIL_ROUTE}/{${NavDestinationArgs.DRIVE_LOG_ID_ARG}}",
                        arguments = listOf(navArgument(NavDestinationArgs.DRIVE_LOG_ID_ARG) {
                            type = NavType.IntType
                        })
                    ) {
                        DriveLogDetailScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(NavDestinations.DRIVE_LOG_INPUT_ROUTE) {
                        DriveLogInputScreen(
                            closeAction = { navController.popBackStack() }
                        )
                    }
                }

            }
        }
    }
}

