package com.squires.gamechanger.leagues

import androidx.paging.PagingData
import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.League
import com.squires.gamechanger.domain.usecase.GetLeaguesPagedUseCase
import com.squires.gamechanger.domain.usecase.HasLeaguesUseCase
import com.squires.gamechanger.domain.usecase.RefreshLeaguesUseCase
import com.squires.gamechanger.domain.usecase.SearchLeaguesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
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
    private val hasLeaguesUseCase: HasLeaguesUseCase = mock()
    private val refreshLeaguesUseCase: RefreshLeaguesUseCase = mock()
    private val searchLeaguesUseCase: SearchLeaguesUseCase = mock()
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        whenever(getLeaguesPagedUseCase()).thenReturn(flowOf(PagingData.empty()))
        // Default: cold cache. Suspend functions must be stubbed inside a coroutine context.
        runBlocking { whenever(hasLeaguesUseCase()).thenReturn(false) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = LeaguesViewModel(
        getLeaguesPagedUseCase = getLeaguesPagedUseCase,
        hasLeaguesUseCase = hasLeaguesUseCase,
        refreshLeaguesUseCase = refreshLeaguesUseCase,
        searchLeaguesUseCase = searchLeaguesUseCase,
    )

    private val leagues = listOf(
        League(id = "1", name = "Premier League", sport = "Soccer", badgeUrl = null, country = "England"),
    )

    @Test
    fun `initial refreshState is Success before coroutine runs`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        assertEquals(Result.Success(Unit), viewModel.refreshState.value)
    }

    @Test
    fun `refreshState transitions through Loading when cache is empty`() = runTest(testDispatcher) {
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            assertEquals(Result.Success(Unit), awaitItem())  // initial sync value
            testScheduler.advanceUntilIdle()
            assertEquals(Result.Loading, awaitItem())         // cold cache: spinner shows
            assertEquals(Result.Success(Unit), awaitItem())   // refresh completes
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshState becomes Error when refresh fails`() = runTest(testDispatcher) {
        val errorMessage = "No internet connection. Check your network and try again."
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Error(errorMessage))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            awaitItem()                                // initial Success(Unit)
            testScheduler.advanceUntilIdle()
            assertEquals(Result.Loading, awaitItem())  // cold cache: spinner shows
            val state = awaitItem()                    // network error
            assertTrue(state is Result.Error)
            assertEquals(errorMessage, (state as Result.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshState stays Success when cache has data (warm cache)`() = runTest(testDispatcher) {
        whenever(hasLeaguesUseCase()).thenReturn(true)
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Success(Unit))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            assertEquals(Result.Success(Unit), awaitItem())  // initial sync value
            testScheduler.advanceUntilIdle()
            // warm cache: no Loading emitted; Success(Unit) == current value, not re-emitted
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshState emits Error even with warm cache when refresh fails`() = runTest(testDispatcher) {
        whenever(hasLeaguesUseCase()).thenReturn(true)
        whenever(refreshLeaguesUseCase()).thenReturn(Result.Error("Network error"))

        val viewModel = createViewModel()

        viewModel.refreshState.test {
            assertEquals(Result.Success(Unit), awaitItem())  // initial sync value
            testScheduler.advanceUntilIdle()
            // warm cache: no Loading emitted, but error still surfaced
            val state = awaitItem()
            assertTrue(state is Result.Error)
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
