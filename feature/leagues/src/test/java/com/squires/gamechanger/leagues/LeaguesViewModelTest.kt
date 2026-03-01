package com.squires.gamechanger.leagues

import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LeaguesViewModelTest {

    private val getLeaguesUseCase: GetLeaguesUseCase = mock()
    private val searchLeaguesUseCase: SearchLeaguesUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = LeaguesViewModel(
        getLeaguesUseCase = getLeaguesUseCase,
        searchLeaguesUseCase = searchLeaguesUseCase,
    )

    private val leagues = listOf(
        League(id = "1", name = "Premier League", sport = "Soccer", badgeUrl = null, country = "England"),
    )

    @Test
    fun `initial state is Loading`() = runTest {
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Loading))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success result maps to Success state`() = runTest {
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Success(leagues)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(leagues, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result maps to Error state`() = runTest {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(getLeaguesUseCase()).thenReturn(
            flowOf<Result<List<League>>>(Result.Error(errorMessage))
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
        whenever(getLeaguesUseCase()).thenReturn(
            flowOf<Result<List<League>>>(Result.Error("No internet connection...", cachedData = leagues))
        )

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem() as UiState.Error
            assertEquals(leagues, state.cachedData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success with empty list maps to Success state with empty list`() = runTest {
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Success(emptyList())))

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
    fun `retry re-triggers getLeaguesUseCase`() = runTest {
        val updatedLeagues = listOf(
            League(id = "2", name = "La Liga", sport = "Soccer", badgeUrl = null, country = "Spain"),
        )
        whenever(getLeaguesUseCase())
            .thenReturn(flowOf(Result.Success(leagues)))
            .thenReturn(flowOf(Result.Success(updatedLeagues)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success from init
            viewModel.retry()
            val state = awaitItem() as UiState.Success
            assertEquals(updatedLeagues, state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry invokes getLeaguesUseCase a second time`() = runTest {
        val updatedLeagues = listOf(
            League(id = "2", name = "La Liga", sport = "Soccer", badgeUrl = null, country = "Spain"),
        )
        whenever(getLeaguesUseCase())
            .thenReturn(flowOf(Result.Success(leagues)))
            .thenReturn(flowOf(Result.Success(updatedLeagues)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success from init
            viewModel.retry()
            awaitItem() // Success from retry
            cancelAndIgnoreRemainingEvents()
        }

        verify(getLeaguesUseCase, times(2)).invoke()
    }

    @Test
    fun `retry on error re-emits Error with cachedData`() = runTest {
        whenever(getLeaguesUseCase())
            .thenReturn(flowOf<Result<List<League>>>(Result.Error("No internet...", cachedData = leagues)))
            .thenReturn(flowOf<Result<List<League>>>(Result.Error("Still no internet.", cachedData = leagues)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Error with cached data
            viewModel.retry()
            val errorAfterRetry = awaitItem() as UiState.Error
            assertEquals(leagues, errorAfterRetry.cachedData)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
