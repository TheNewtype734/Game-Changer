package com.squires.gamechanger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.squires.gamechanger.leagues.LeaguesRoute
import com.squires.gamechanger.leagues.LeaguesScreen
import com.squires.gamechanger.teamdetails.TeamDetailRoute
import com.squires.gamechanger.teamdetails.TeamDetailScreen
import com.squires.gamechanger.teams.TeamsRoute
import com.squires.gamechanger.teams.TeamsScreen

@Composable
fun GameChangerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LeaguesRoute,
        modifier = modifier,
    ) {
        composable<LeaguesRoute> {
            LeaguesScreen(
                onLeagueClick = { leagueName ->
                    navController.navigate(TeamsRoute(leagueName))
                },
            )
        }

        composable<TeamsRoute> {
            TeamsScreen(
                onTeamClick = { teamId ->
                    navController.navigate(TeamDetailRoute(teamId))
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable<TeamDetailRoute> {
            TeamDetailScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}
