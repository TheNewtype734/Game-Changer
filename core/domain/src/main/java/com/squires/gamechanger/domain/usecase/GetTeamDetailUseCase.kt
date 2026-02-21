package com.squires.gamechanger.domain.usecase

import com.squires.gamechanger.common.Result
import com.squires.gamechanger.domain.model.TeamDetail
import com.squires.gamechanger.domain.repository.TeamRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamDetailUseCase @Inject constructor(
    private val teamRepository: TeamRepository,
) {
    operator fun invoke(teamId: String): Flow<Result<TeamDetail>> =
        teamRepository.getTeamDetail(teamId)
}
