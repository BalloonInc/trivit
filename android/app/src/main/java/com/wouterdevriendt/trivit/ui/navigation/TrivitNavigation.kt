package com.wouterdevriendt.trivit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wouterdevriendt.trivit.ui.screens.deleted.DeletedItemsScreen
import com.wouterdevriendt.trivit.ui.screens.history.HistoryScreen
import com.wouterdevriendt.trivit.ui.screens.list.TrivitListScreen
import com.wouterdevriendt.trivit.ui.screens.settings.SettingsScreen
import com.wouterdevriendt.trivit.ui.screens.statistics.StatisticsScreen

object Routes {
    const val TRIVIT_LIST = "trivit_list"
    const val SETTINGS = "settings"
    const val DELETED_ITEMS = "deleted_items"
    const val STATISTICS = "statistics/{trivitId}"
    const val HISTORY = "history/{trivitId}"

    fun statistics(trivitId: Long) = "statistics/$trivitId"
    fun history(trivitId: Long) = "history/$trivitId"
}

@Composable
fun TrivitNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TRIVIT_LIST
    ) {
        composable(Routes.TRIVIT_LIST) {
            TrivitListScreen(
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToStatistics = { trivitId ->
                    navController.navigate(Routes.statistics(trivitId))
                },
                onNavigateToHistory = { trivitId ->
                    navController.navigate(Routes.history(trivitId))
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDeletedItems = { navController.navigate(Routes.DELETED_ITEMS) }
            )
        }

        composable(Routes.DELETED_ITEMS) {
            DeletedItemsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.STATISTICS,
            arguments = listOf(navArgument("trivitId") { type = NavType.LongType })
        ) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.HISTORY,
            arguments = listOf(navArgument("trivitId") { type = NavType.LongType })
        ) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
