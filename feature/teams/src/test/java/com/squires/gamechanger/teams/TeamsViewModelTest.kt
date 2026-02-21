package com.squires.gamechanger.teams

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.usecase.GetTeamsForLeagueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TeamsViewModelTest {

    private val getTeamsForLeagueUseCase: GetTeamsForLeagueUseCase = mock()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(leagueName: String = "Premier League"): TeamsViewModel {
        return TeamsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TeamsArgs.LEAGUE_NAME to leagueName)),
            getTeamsForLeagueUseCase = getTeamsForLeagueUseCase,
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Loading))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success result maps to Success state`() = runTest {
        val teams = listOf(
            Team(id = "133604", name = "Arsenal", leagueName = "Premier League", badgeUrl = null, sport = "Soccer"),
        )
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Success(teams)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(teams, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result maps to Error state`() = runTest {
        val errorMessage = "Failed to load teams"
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Error(errorMessage)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals(errorMessage, (state as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `leagueName is read from SavedStateHandle`() = runTest {
        val leagueName = "La Liga"
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Loading))

        val viewModel = createViewModel(leagueName = leagueName)

        assertEquals(leagueName, viewModel.leagueName)
    }
}
