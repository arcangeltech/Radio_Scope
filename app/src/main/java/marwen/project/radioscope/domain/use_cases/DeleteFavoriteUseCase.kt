package marwen.project.radioscope.domain.use_cases

import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.repositories.LocalRadioScopeRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: LocalRadioScopeRepository
) {
    suspend operator fun invoke(radio: Radio) {
        repository.deleteFavorite(radio)
    }
}