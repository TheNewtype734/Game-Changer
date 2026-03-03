package com.squires.gamechanger.teams

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.Team
import com.squires.gamechanger.domain.usecase.GetTeamsForLeaguePagedUseCase
import com.squires.gamechanger.domain.usecase.RefreshTeamsForLeagueUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    private val getTeamsForLeaguePagedUseCase: GetTeamsForLeaguePagedUseCase = mock()
    private val refreshTeamsForLeagueUseCase: RefreshTeamsForLeagueUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getTeamsForLeaguePagedUseCase(any())).thenReturn(flowOf(PagingData.empty()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(leagueName: String = "Premier League"): TeamsViewModel {
        return TeamsViewModel(
            savedStateHandle = SavedStateHandle(mapOf("leagueName" to leagueName)),
            getTeamsForLeaguePagedUseCase = getTeamsForLeaguePagedUseCase,
            refreshTeamsForLeagueUseCase = refreshTeamsForLeagueUseCase,
        )
    }

    private val teams = listOf(
        Team(id = "133604", name = "Arsenal", leagueName = "Premier League", badgeUrl = null, sport = "Soccer"),
    )

    @Test
    fun `initial refreshState is Loading`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertEquals(Result.Loading, viewModel.refreshState.value)
    }

    @Test
    fun `refreshState becomes Success after successful refresh`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            assertEquals(Result.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(Result.Success(Unit), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshState becomes Error when refresh fails`() = runTest {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Error(errorMessage))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            awaitItem() // initial Loading
            testScheduler.advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is Result.Error)
            assertEquals(errorMessage, (state as Result.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry invokes refreshTeamsForLeagueUseCase a second time`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // initial refresh

        viewModel.retry()
        testScheduler.advanceUntilIdle() // retry

        verify(refreshTeamsForLeagueUseCase, times(2)).invoke("Premier League")
    }

    @Test
    fun `retry after error becomes Loading then result`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any()))
            .thenReturn(Result.Error("Network error"))
            .thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // initial refresh (Error)

        viewModel.refreshState.test {
            awaitItem() // current value: Error
            viewModel.retry()
            testScheduler.advanceUntilIdle()
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(Unit), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `leagueName is read from SavedStateHandle`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))
        val leagueName = "La Liga"

        val viewModel = createViewModel(leagueName = leagueName)

        assertEquals(leagueName, viewModel.leagueName)
    }

    @Test
    fun `pagedTeams is initialized from getTeamsForLeaguePagedUseCase`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertNotNull(viewModel.pagedTeams)
        verify(getTeamsForLeaguePagedUseCase).invoke("Premier League")
    }

    @Test
    fun `refresh uses leagueName from SavedStateHandle`() = runTest {
        whenever(refreshTeamsForLeagueUseCase(any())).thenReturn(Result.Success(Unit))
        val leagueName = "La Liga"

        val viewModel = createViewModel(leagueName = leagueName)
        testScheduler.advanceUntilIdle()

        verify(refreshTeamsForLeagueUseCase).invoke(leagueName)
    }
}
