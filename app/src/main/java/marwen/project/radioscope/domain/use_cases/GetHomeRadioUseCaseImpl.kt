package marwen.project.radioscope.domain.use_cases

import kotlinx.coroutines.flow.flow
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.repositories.RadioScopeRepository
import marwen.project.radioscope.utils.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetHomeRadioUseCaseImpl @Inject constructor(private val repository: RadioScopeRepository) :
    GetHomeRadioUseCase {
    override operator fun invoke()= flow<Resource<RadioHomeDto>> {
        try {
            emit(Resource.Loading())
            val coins = repository.getHomeRadioList()
            emit(Resource.Success(coins))
        }catch (e : HttpException){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e : IOException){
            emit(Resource.Error("Couldn't reach server. Check your internet connect"))
        }catch (e:NullPointerException){
            emit(Resource.Error("Couldn't reach server. Check your internet connect"))
        }
    }
}


