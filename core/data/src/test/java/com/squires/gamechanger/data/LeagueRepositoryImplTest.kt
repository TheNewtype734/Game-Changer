package com.squires.gamechanger.data

import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.local.entity.LeagueEntity
import com.squires.gamechanger.data.repository.LeagueRepositoryImpl
import com.squires.gamechanger.network.api.SportsDbApi
import com.squires.gamechanger.network.dto.LeagueDto
import com.squires.gamechanger.network.dto.response.LeagueResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

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
            LeagueResponse(leagues = listOf(
                LeagueDto(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England")
            ))
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
}
