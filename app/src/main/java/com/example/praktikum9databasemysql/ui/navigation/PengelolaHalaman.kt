package com.example.praktikum9databasemysql.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.praktikum9databasemysql.ui.view.DestinasiDetail
import com.example.praktikum9databasemysql.ui.view.DestinasiEntry
import com.example.praktikum9databasemysql.ui.view.DestinasiHome
import com.example.praktikum9databasemysql.ui.view.DestinasiUpdate
import com.example.praktikum9databasemysql.ui.view.DetailView
import com.example.praktikum9databasemysql.ui.view.EntryMhsScreen
import com.example.praktikum9databasemysql.ui.view.HomeScreen
import com.example.praktikum9databasemysql.ui.view.UpdateScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()){
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ){
        composable(DestinasiHome.route){
            HomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntry.route)},
                onDetailClick = { nim ->
                    navController.navigate("${DestinasiDetail.route}/$nim")
                }
            )
        }
        composable(DestinasiEntry.route){
            EntryMhsScreen(navigateBack = {
                navController.navigate(DestinasiHome.route){
                    popUpTo(DestinasiHome.route){
                        inclusive = true
                    }
                }
            })
        }
        composable(
            route = "${DestinasiDetail.route}/{nim}",
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            DetailView(
                nim = nim,
                onNavigateBack = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate("${DestinasiUpdate.route}/$nim")
                }
            )
        }
        composable(
            route = "${DestinasiUpdate.route}/{nim}",
            arguments = listOf(navArgument("nim") { type = NavType.StringType })
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            UpdateScreen(
                nim = nim,
                navigateBack = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiHome.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}