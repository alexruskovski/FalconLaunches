package com.alexruskovski.falcon.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.alexruskovski.falcon.ui.details.DetailsScreen
import com.alexruskovski.falcon.ui.list.ListScreen
import com.google.accompanist.navigation.animation.composable


/**
 * Created by Alexander Ruskovski on 13/08/2021
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FalconAppNavigation() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationRoutes.ListScreen.route
    ){

        composable(
            route = NavigationRoutes.ListScreen.route,
            exitTransition = {_,_ ->
                slideOutHorizontally(
                    targetOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) +
                fadeOut(animationSpec = tween(durationMillis = 300))
            },
            popEnterTransition = {_,_->
                slideInHorizontally(
                    initialOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) +
                fadeIn(animationSpec = tween(durationMillis = 300))
            }
        ){
            ListScreen(
                launchesListViewModel = hiltViewModel(),
                navHostController = navController
            )
        }

        composable(
            route = NavigationRoutes.DetailsScreen.route + "/{launchId}" ,
            arguments = listOf(
                navArgument("launchId"){
                    type = NavType.StringType
                    nullable = true
                }
            ),
            enterTransition = {_,_ ->
                slideInHorizontally(
                    initialOffsetX = {300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )  +
                        fadeIn(animationSpec = tween(durationMillis = 300))
            },
            popExitTransition = {_,_->
                slideOutHorizontally(
                    targetOffsetX = {-300},
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) +
                        fadeOut(animationSpec = tween(durationMillis = 300))
            }
        ){ entry ->
            val param = entry.arguments?.getString("launchId")
            DetailsScreen(
                launchId = param ,
                detailsScreenViewModel = hiltViewModel(),
                navController = navController
            )
        }
    }

}