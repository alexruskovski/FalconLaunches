package com.alexruskovski.falcon.ui.navigation


/**
 * Created by Alexander Ruskovski on 14/08/2021
 */
sealed class NavigationRoutes(
    val route: String
) {
    object ListScreen : NavigationRoutes("list_launches_screen")
    object DetailsScreen : NavigationRoutes("details_launches_screen"){
        fun routeWithId(launchId: String):String{
            return "$route/$launchId"
        }
    }
}