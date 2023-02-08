package marwen.project.radioscope.domain.use_cases

import kotlinx.coroutines.flow.flow
import marwen.project.radioscope.repositories.RadioScopeRepository
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

class GetSearchRadioUseCase @Inject constructor(private val repository: RadioScopeRepository) {
    operator fun invoke(search:String,count:String?,page:String?) = flow{
        try {
            emit(Resource.Loading())
            val list = repository.getSearchRadioList(search, page, count)
            emit(Resource.Success(data = list))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage?: "unknown error "))
        }
    }
}