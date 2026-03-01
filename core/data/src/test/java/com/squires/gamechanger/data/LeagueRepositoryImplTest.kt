package com.squires.gamechanger.data

import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.local.entity.LeagueEntity
import com.squires.gamechanger.data.repository.LeagueRepositoryImpl
import com.squires.gamechanger.network.api.SportsDbApi
import com.squires.gamechanger.network.dto.LeagueDto
import com.squires.gamechanger.network.dto.response.LeagueResponse
import com.squires.gamechanger.network.dto.response.LeagueSearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class LeagueRepositoryImplTest {

    private val api: SportsDbApi = mock()
    private val dao: LeagueDao = mock()

    private val repository = LeagueRepositoryImpl(api, dao)

    private val leagueEntities = listOf(
        LeagueEntity(
            id = "4328",
            name = "English Premier League",
            sport = "Soccer",
            badgeUrl = null,
            country = "England",
        )
    )

    @Test
    fun `getLeagues emits Loading then Success from dao`() = runTest {
        whenever(dao.getLeagues()).thenReturn(flowOf(leagueEntities))
        whenever(api.getAllLeagues()).thenReturn(
            LeagueResponse(
                leagues = listOf(
                    LeagueDto(
                        id = "4328",
                        name = "English Premier League",
                        sport = "Soccer",
                        badgeUrl = null,
                        country = "England",
                    )
                )
            )
        )

        repository.getLeagues().test {
            assertTrue(awaitItem() is Result.Loading)
            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals(1, (success as Result.Success).data.size)
            assertEquals("English Premier League", success.data.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getLeagues inserts fetched leagues into dao`() = runTest {
        val dto = LeagueDto(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England")
        whenever(dao.getLeagues()).thenReturn(flowOf(leagueEntities))
        whenever(api.getAllLeagues()).thenReturn(LeagueResponse(leagues = listOf(dto)))

        repository.getLeagues().test {
            assertTrue(awaitItem() is Result.Loading)
            awaitItem() // Success — onStart has finished, refreshLeagues() and insertAll() have run
            cancelAndIgnoreRemainingEvents()
        }

        verify(dao).insertAll(
            listOf(LeagueEntity(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England"))
        )
    }

    @Test
    fun `getLeagues emits empty Success when dao returns empty list`() = runTest {
        whenever(dao.getLeagues()).thenReturn(flowOf(emptyList()))
        whenever(api.getAllLeagues()).thenReturn(LeagueResponse(leagues = null))

        repository.getLeagues().test {
            assertTrue(awaitItem() is Result.Loading)
            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertTrue((success as Result.Success).data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getLeagues emits Error with cachedData when refresh fails and cache exists`() = runTest {
        whenever(dao.getLeagues()).thenReturn(flowOf(leagueEntities))
        whenever(api.getAllLeagues()).thenAnswer { throw UnknownHostException() }

        repository.getLeagues().test {
            assertTrue(awaitItem() is Result.Loading)
            val error = awaitItem() as Result.Error
            assertEquals("No internet connection. Check your network and try again.", error.message)
            assertEquals(1, error.cachedData?.size)
            assertEquals("English Premier League", error.cachedData?.first()?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getLeagues emits Error with null cachedData when refresh fails and cache is empty`() = runTest {
        whenever(dao.getLeagues()).thenReturn(flowOf(emptyList()))
        whenever(api.getAllLeagues()).thenAnswer { throw UnknownHostException() }

        repository.getLeagues().test {
            assertTrue(awaitItem() is Result.Loading)
            val error = awaitItem() as Result.Error
            assertNull(error.cachedData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchLeagues returns Success with results for matching country query`() = runTest {
        val dto = LeagueDto(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England")
        whenever(api.searchLeagues(country = "England")).thenReturn(LeagueSearchResponse(leagues = listOf(dto)))

        val result = repository.searchLeagues("England")

        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        assertEquals("English Premier League", result.data.first().name)
    }

    @Test
    fun `searchLeagues returns Error on network failure`() = runTest {
        whenever(api.searchLeagues(country = "England")).thenAnswer { throw UnknownHostException() }

        val result = repository.searchLeagues("England")

        assertTrue(result is Result.Error)
        assertEquals("No internet connection. Check your network and try again.", (result as Result.Error).message)
    }

    @Test
    fun `refreshLeagues fetches from api and inserts into dao`() = runTest {
        val dto = LeagueDto(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England")
        whenever(api.getAllLeagues()).thenReturn(LeagueResponse(leagues = listOf(dto)))

        repository.refreshLeagues()

        verify(dao).insertAll(
            listOf(LeagueEntity(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England"))
        )
    }
}
