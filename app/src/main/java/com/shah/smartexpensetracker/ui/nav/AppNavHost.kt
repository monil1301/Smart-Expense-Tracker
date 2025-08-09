package com.shah.smartexpensetracker.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shah.smartexpensetracker.ui.screens.EntryScreen
import com.shah.smartexpensetracker.ui.screens.ListScreen
import com.shah.smartexpensetracker.ui.screens.ReportScreen

/**
 * Created by Monil on 09/08/25.
 */

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Route.Entry.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Route.Entry.route) {
            EntryScreen()
        }
        composable(Route.List.route) {
            ListScreen()
        }
        composable(Route.Report.route) {
            ReportScreen()
        }
    }
}
