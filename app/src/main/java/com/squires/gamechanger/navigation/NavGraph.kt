package com.squires.gamechanger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.squires.gamechanger.leagues.LeaguesScreen
import com.squires.gamechanger.teamdetails.TeamDetailScreen
import com.squires.gamechanger.teamdetails.TeamDetailArgs
import com.squires.gamechanger.teams.TeamsArgs
import com.squires.gamechanger.teams.TeamsScreen

sealed class Screen(val route: String) {
    data object Leagues : Screen("leagues")
    data object Teams : Screen("teams/{${TeamsArgs.LEAGUE_NAME}}") {
        fun createRoute(leagueName: String) = "teams/$leagueName"
    }
    data object TeamDetail : Screen("teamdetail/{${TeamDetailArgs.TEAM_ID}}") {
        fun createRoute(teamId: String) = "teamdetail/$teamId"
    }
}

@Composable
fun GameChangerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Leagues.route,
        modifier = modifier,
    ) {
        composable(Screen.Leagues.route) {
            LeaguesScreen(
                onLeagueClick = { leagueName ->
                    navController.navigate(Screen.Teams.createRoute(leagueName))
                },
            )
        }

        composable(
            route = Screen.Teams.route,
            arguments = listOf(
                navArgument(TeamsArgs.LEAGUE_NAME) { type = NavType.StringType },
            ),
        ) {
            TeamsScreen(
                onTeamClick = { teamId ->
                    navController.navigate(Screen.TeamDetail.createRoute(teamId))
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.TeamDetail.route,
            arguments = listOf(
                navArgument(TeamDetailArgs.TEAM_ID) { type = NavType.StringType },
            ),
        ) {
            TeamDetailScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
