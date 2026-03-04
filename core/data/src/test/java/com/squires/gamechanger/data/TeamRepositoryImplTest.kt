package com.squires.gamechanger.data

import app.cash.turbine.test
import com.squires.gamechanger.common.Result
import com.squires.gamechanger.data.local.dao.TeamDao
import com.squires.gamechanger.data.local.dao.TeamDetailDao
import com.squires.gamechanger.data.local.entity.TeamDetailEntity
import com.squires.gamechanger.data.repository.TeamRepositoryImpl
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.network.api.SportsDbApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TeamRepositoryImplTest {

    private val api: SportsDbApi = mock()
    private val teamDao: TeamDao = mock()
    private val teamDetailDao: TeamDetailDao = mock()

    private val repository = TeamRepositoryImpl(api, teamDao, teamDetailDao)

    private val teamDetailEntity = TeamDetailEntity(
        id = "133604",
        name = "Arsenal",
        leagueName = "Premier League",
        sport = "Soccer",
        country = "England",
        formedYear = "1886",
        description = null,
        stadium = "Emirates Stadium",
        stadiumLocation = "Holloway, London",
        stadiumCapacity = "60704",
        badgeUrl = null,
        bannerUrl = null,
        website = "www.arsenal.com",
    )

    // ─── getTeamDetail ───────────────────────────────────────────────────────────

    @Test
    fun `getTeamDetail emits Loading when entity is null (not yet cached)`() = runTest {
        whenever(teamDetailDao.getTeamDetail("133604")).thenReturn(flowOf(null))

        repository.getTeamDetail("133604").test {
            assertEquals(Result.Loading, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getTeamDetail emits Success immediately when entity is cached (no Loading prefix)`() = runTest {
        whenever(teamDetailDao.getTeamDetail("133604")).thenReturn(flowOf(teamDetailEntity))

        repository.getTeamDetail("133604").test {
            val item = awaitItem()
            assertTrue(item is Result.Success)
            assertEquals("Arsenal", (item as Result.Success<TeamDetail>).data.name)
            awaitComplete()
        }
    }

    // ─── hasTeamsForLeague ───────────────────────────────────────────────────────

    @Test
    fun `hasTeamsForLeague returns true when team count is greater than zero`() = runTest {
        whenever(teamDao.countForLeague("Premier League")).thenReturn(3)

        assertTrue(repository.hasTeamsForLeague("Premier League"))
    }

    @Test
    fun `hasTeamsForLeague returns false when team count is zero`() = runTest {
        whenever(teamDao.countForLeague("Premier League")).thenReturn(0)

        assertTrue(!repository.hasTeamsForLeague("Premier League"))
    }
}
