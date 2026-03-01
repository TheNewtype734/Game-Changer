package com.squires.gamechanger.teamdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.common.UiState
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.domain.usecase.GetTeamDetailUseCase
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
class TeamDetailViewModelTest {

    private val getTeamDetailUseCase: GetTeamDetailUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(teamId: String = "133604") = TeamDetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("teamId" to teamId)),
        getTeamDetailUseCase = getTeamDetailUseCase,
    )

    private val teamDetail = TeamDetail(
        id = "133604",
        name = "Arsenal",
        leagueName = "Premier League",
        sport = "Soccer",
        country = "England",
        formedYear = "1886",
        description = null,
        stadium = "Emirates Stadium",
        stadiumLocation = "London",
        stadiumCapacity = "60260",
        badgeUrl = null,
        bannerUrl = null,
        website = null,
    )

    @Test
    fun `initial state is Loading`() = runTest {
        whenever(getTeamDetailUseCase(any())).thenReturn(flowOf(Result.Loading))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `success result maps to Success state`() = runTest {
        whenever(getTeamDetailUseCase(any())).thenReturn(flowOf(Result.Success(teamDetail)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(teamDetail, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error result maps to Error state`() = runTest {
        val errorMessage = "Something went wrong. Try again."
        whenever(getTeamDetailUseCase(any())).thenReturn(
            flowOf<Result<TeamDetail>>(Result.Error(errorMessage))
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
    fun `retry re-invokes use case and emits Success after Error`() = runTest {
        whenever(getTeamDetailUseCase(any()))
            .thenReturn(flowOf<Result<TeamDetail>>(Result.Error("error")))
            .thenReturn(flowOf(Result.Success(teamDetail)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Error from first load
            viewModel.retry()
            val state = awaitItem()
            assertTrue(state is UiState.Success)
            assertEquals(teamDetail, (state as UiState.Success).data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry invokes use case a second time`() = runTest {
        val updatedDetail = teamDetail.copy(name = "Arsenal FC")
        whenever(getTeamDetailUseCase(any()))
            .thenReturn(flowOf(Result.Success(teamDetail)))
            .thenReturn(flowOf(Result.Success(updatedDetail)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success from first load
            viewModel.retry()
            awaitItem() // Success from retry (different value drives the scheduler)
            cancelAndIgnoreRemainingEvents()
        }

        verify(getTeamDetailUseCase, times(2)).invoke("133604")
    }

    @Test
    fun `teamId is read correctly from SavedStateHandle`() = runTest {
        whenever(getTeamDetailUseCase(any())).thenReturn(flowOf(Result.Success(teamDetail)))

        val viewModel = createViewModel(teamId = "133604")

        viewModel.uiState.test {
            awaitItem() // initial Loading
            awaitItem() // Success
            cancelAndIgnoreRemainingEvents()
        }

        verify(getTeamDetailUseCase).invoke("133604")
    }
}
