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
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TeamsViewModelTest {

    private val getTeamsForLeagueUseCase: GetTeamsForLeagueUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

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
            savedStateHandle = SavedStateHandle(mapOf("leagueName" to leagueName)),
            getTeamsForLeagueUseCase = getTeamsForLeagueUseCase,
        )
    }

    private val teams = listOf(
        Team(id = "133604", name = "Arsenal", leagueName = "Premier League", badgeUrl = null, sport = "Soccer"),
    )

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
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Success(teams)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(teams, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result maps to Error state`() = runTest {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(
            flowOf<Result<List<Team>>>(Result.Error(errorMessage))
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals(errorMessage, (state as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result with cachedData maps to Error state with cachedData`() = runTest {
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(
            flowOf<Result<List<Team>>>(Result.Error("No internet...", cachedData = teams))
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem() as UiState.Error
            assertEquals(teams, state.cachedData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success with empty list maps to Success state with empty list`() = runTest {
        whenever(getTeamsForLeagueUseCase(any())).thenReturn(flowOf(Result.Success(emptyList())))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertTrue((state as UiState.Success).data.isEmpty())
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

    @Test
    fun `retry re-collects from use case`() = runTest {
        val updatedTeams = listOf(
            Team(id = "133604", name = "Arsenal FC", leagueName = "Premier League", badgeUrl = null, sport = "Soccer"),
        )
        whenever(getTeamsForLeagueUseCase(any()))
            .thenReturn(flowOf(Result.Success(teams)))
            .thenReturn(flowOf(Result.Success(updatedTeams)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success from init
            viewModel.retry()
            val state = awaitItem() as UiState.Success
            assertEquals(updatedTeams, state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry invokes use case a second time`() = runTest {
        val updatedTeams = listOf(
            Team(id = "133604", name = "Arsenal FC", leagueName = "Premier League", badgeUrl = null, sport = "Soccer"),
        )
        whenever(getTeamsForLeagueUseCase(any()))
            .thenReturn(flowOf(Result.Success(teams)))
            .thenReturn(flowOf(Result.Success(updatedTeams)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success from init
            viewModel.retry()
            awaitItem() // Success from retry
            cancelAndIgnoreRemainingEvents()
        }

        verify(getTeamsForLeagueUseCase, times(2)).invoke("Premier League")
    }

    @Test
    fun `retry on error re-emits Error with cachedData`() = runTest {
        whenever(getTeamsForLeagueUseCase(any()))
            .thenReturn(flowOf<Result<List<Team>>>(Result.Error("No internet...", cachedData = teams)))
            .thenReturn(flowOf<Result<List<Team>>>(Result.Error("Still no internet.", cachedData = teams)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Error with cached data
            viewModel.retry()
            val errorAfterRetry = awaitItem() as UiState.Error
            assertEquals(teams, errorAfterRetry.cachedData)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
