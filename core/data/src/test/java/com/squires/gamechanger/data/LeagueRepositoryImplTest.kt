package com.squires.gamechanger.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.LeagueDao
import com.squires.gamechanger.data.local.entity.LeagueEntity
import com.squires.gamechanger.data.repository.LeagueRepositoryImpl
import com.squires.gamechanger.network.api.SportsDbApi
import com.squires.gamechanger.network.dto.LeagueDto
import com.squires.gamechanger.network.dto.response.LeagueResponse
import com.squires.gamechanger.network.dto.response.LeagueSearchResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    // ─── getLeaguesPaged ────────────────────────────────────────────────────────

    @Test
    fun `getLeaguesPaged returns a non-null flow`() = runTest {
        val fakePagingSource = object : PagingSource<Int, LeagueEntity>() {
            override fun getRefreshKey(state: PagingState<Int, LeagueEntity>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LeagueEntity> =
                LoadResult.Page(data = leagueEntities, prevKey = null, nextKey = null)
        }
        whenever(dao.pagingSource()).thenReturn(fakePagingSource)

        assertNotNull(repository.getLeaguesPaged())
    }

    // ─── searchLeagues ──────────────────────────────────────────────────────────

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

    // ─── refreshLeagues ─────────────────────────────────────────────────────────

    @Test
    fun `refreshLeagues fetches from api and inserts into dao`() = runTest {
        val dto = LeagueDto(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England")
        whenever(api.getAllLeagues()).thenReturn(LeagueResponse(leagues = listOf(dto)))

        repository.refreshLeagues()

        verify(dao).insertAll(
            listOf(LeagueEntity(id = "4328", name = "English Premier League", sport = "Soccer", badgeUrl = null, country = "England"))
        )
    }

    @Test
    fun `refreshLeagues returns Success on successful fetch`() = runTest {
        whenever(api.getAllLeagues()).thenReturn(LeagueResponse(leagues = emptyList()))

        val result = repository.refreshLeagues()

        assertTrue(result is Result.Success)
    }

    @Test
    fun `refreshLeagues returns Error on network failure`() = runTest {
        whenever(api.getAllLeagues()).thenAnswer { throw UnknownHostException() }

        val result = repository.refreshLeagues()

        assertTrue(result is Result.Error)
        assertEquals("No internet connection. Check your network and try again.", (result as Result.Error).message)
    }

    // ─── hasLeagues ─────────────────────────────────────────────────────────────

    @Test
    fun `hasLeagues returns true when league count is greater than zero`() = runTest {
        whenever(dao.count()).thenReturn(5)

        assertTrue(repository.hasLeagues())
    }

    @Test
    fun `hasLeagues returns false when league count is zero`() = runTest {
        whenever(dao.count()).thenReturn(0)

        assertTrue(!repository.hasLeagues())
    }
}
