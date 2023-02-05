package marwen.project.radioscope.domain.use_cases

import kotlinx.coroutines.flow.flow
import marwen.project.radioscope.repositories.LocalRadioScopeRepository
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(
    private val repository: LocalRadioScopeRepository
) {
      operator fun invoke() = flow{
        try {
            emit(Resource.Loading())
            val list = repository.getFavorite()
            emit(list)
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage?: "unknown error "))
        }
    }
}