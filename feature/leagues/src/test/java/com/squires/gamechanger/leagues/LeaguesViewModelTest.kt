package com.squires.gamechanger.leagues

import androidx.paging.PagingData
import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.usecase.GetLeaguesPagedUseCase
import com.squires.gamechanger.domain.usecase.RefreshLeaguesUseCase
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
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
class LeaguesViewModelTest {

    private val getLeaguesPagedUseCase: GetLeaguesPagedUseCase = mock()
    private val refreshLeaguesUseCase: RefreshLeaguesUseCase = mock()
    private val searchLeaguesUseCase: SearchLeaguesUseCase = mock()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getLeaguesPagedUseCase()).thenReturn(flowOf(PagingData.empty()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = LeaguesViewModel(
        getLeaguesPagedUseCase = getLeaguesPagedUseCase,
        refreshLeaguesUseCase = refreshLeaguesUseCase,
        searchLeaguesUseCase = searchLeaguesUseCase,
    )

    private val leagues = listOf(
        League(id = "1", name = "Premier League", sport = "Soccer", badgeUrl = null, country = "England"),
    )

    @Test
    fun `initial refreshState is Loading`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertEquals(Result.Loading, viewModel.refreshState.value)
    }

    @Test
    fun `refreshState becomes Success after successful refresh`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            assertEquals(Result.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(Result.Success(Unit), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshState becomes Error when refresh fails`() = runTest(testDispatcher) {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Error(errorMessage))

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
    fun `retry with blank query invokes refreshLeaguesUseCase a second time`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // initial refresh

        viewModel.retry()
        testScheduler.advanceUntilIdle() // retry refresh

        verify(refreshLeaguesUseCase, times(2)).invoke()
    }

    @Test
    fun `retry after error becomes Loading then result`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase())
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
    fun `pagedLeagues is initialized from getLeaguesPagedUseCase`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertNotNull(viewModel.pagedLeagues)
        verify(getLeaguesPagedUseCase).invoke()
    }

    @Test
    fun `initial searchState is Loading`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertTrue(viewModel.searchState.value is UiState.Loading)
    }

    @Test
    fun `searchState becomes Success when searchLeaguesUseCase returns results`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))
        whenever(searchLeaguesUseCase(any())).thenReturn(Result.Success(leagues))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // start ViewModel init coroutines (queryTrigger collector)

        viewModel.searchState.test {
            awaitItem() // initial Loading
            viewModel.onSearchQueryChange("England")
            testScheduler.advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(leagues, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchState becomes Error when searchLeaguesUseCase returns error`() = runTest(testDispatcher) {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))
        whenever(searchLeaguesUseCase(any())).thenReturn(Result.Error(errorMessage))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // start ViewModel init coroutines (queryTrigger collector)

        viewModel.searchState.test {
            awaitItem() // initial Loading
            viewModel.onSearchQueryChange("England")
            testScheduler.advanceUntilIdle()
            val state = awaitItem()
            assertTrue(state is UiState.Error)
            assertEquals(errorMessage, (state as UiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry with non-blank query re-invokes searchLeaguesUseCase`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))
        whenever(searchLeaguesUseCase(any()))
            .thenReturn(Result.Success(leagues))
            .thenReturn(Result.Success(emptyList()))

        val viewModel = createViewModel()
        testScheduler.advanceUntilIdle() // start ViewModel init coroutines (queryTrigger collector)
        viewModel.onSearchQueryChange("England")
        testScheduler.advanceUntilIdle() // first search

        viewModel.retry()
        testScheduler.advanceUntilIdle() // retry search

        verify(searchLeaguesUseCase, times(2)).invoke("England")
    }
}
