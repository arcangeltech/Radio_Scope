package marwen.project.radioscope.ui.listen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import marwen.project.radioscope.data.local.ListenParameters
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.domain.use_cases.AddFavoriteUseCase
import marwen.project.radioscope.domain.use_cases.FavoriteUseCases
import marwen.project.radioscope.domain.use_cases.GetFavoriteUseCase
import marwen.project.radioscope.repositories.LocalRadioScopeRepository
import marwen.project.radioscope.ui.home.fragments.accueil.AccueilHomeState
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

@HiltViewModel

class ListenViewModel @Inject constructor(favoriteUseCases: FavoriteUseCases,private val state: SavedStateHandle): ViewModel() {
    var isFavorite = mutableStateOf(false)
    var favoriteUseCases = favoriteUseCases
    var list= favoriteUseCases.getFavoriteUseCase()
    init {
        checkIsFavorite(state.get<ListenParameters>("listRadio")!!.radio)
    }

    fun checkIsFavorite(radio:Radio){
        list.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    isFavorite.value = result.data?.contains(radio)?:false
                }

                is Resource.Error -> {
                    isFavorite.value= false
                }

                is Resource.Loading -> {
                    isFavorite.value= false

                }
            }

        }.launchIn(viewModelScope)
    }
    fun addFavorite(radio: Radio) {
        viewModelScope.launch {
            favoriteUseCases.addFavoriteUseCase(radio)
            isFavorite.value= true
        }
    }

    fun deleteFavorite(radio: Radio) {
        viewModelScope.launch {
            favoriteUseCases.deleteFavoriteUseCase(radio)
            isFavorite.value= false
        }
    }


}