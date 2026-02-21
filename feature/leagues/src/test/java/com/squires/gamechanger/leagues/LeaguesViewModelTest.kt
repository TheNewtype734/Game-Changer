package com.squires.gamechanger.leagues

import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.usecase.GetLeaguesUseCase
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
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LeaguesViewModelTest {

    private val getLeaguesUseCase: GetLeaguesUseCase = mock()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Loading))

        val viewModel = LeaguesViewModel(getLeaguesUseCase)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success result maps to Success state`() = runTest {
        val leagues = listOf(
            League(id = "1", name = "Premier League", sport = "Soccer", badgeUrl = null, country = "England"),
        )
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Success(leagues)))

        val viewModel = LeaguesViewModel(getLeaguesUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(leagues, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result maps to Error state`() = runTest {
        val errorMessage = "Network error"
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Error(errorMessage)))

        val viewModel = LeaguesViewModel(getLeaguesUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals(errorMessage, (state as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success with empty list maps to Success state with empty list`() = runTest {
        whenever(getLeaguesUseCase()).thenReturn(flowOf(Result.Success(emptyList())))

        val viewModel = LeaguesViewModel(getLeaguesUseCase)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertTrue((state as UiState.Success).data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
