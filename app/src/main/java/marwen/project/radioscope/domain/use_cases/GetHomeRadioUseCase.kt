package marwen.project.radioscope.domain.use_cases

import kotlinx.coroutines.flow.Flow
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.utils.Resource

interface GetHomeRadioUseCase {
    operator fun invoke(): Flow<Resource<RadioHomeDto>>
}