package marwen.project.radioscope.ui.home.fragments.favorite

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import marwen.project.radioscope.domain.use_cases.FavoriteUseCases
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteUseCases: FavoriteUseCases ) : ViewModel() {
    val _state = mutableStateOf(FavoriteState())
    val state: State<FavoriteState> = _state

    init {
        getRadios()
    }
    fun getRadios() {
        var result = favoriteUseCases.getFavoriteUseCase()
        result.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = FavoriteState(listFavorite = result.data?: listOf())
                }

                is Resource.Error -> {
                    _state.value = FavoriteState(error = result.message ?: "erreur")
                }

                is Resource.Loading -> {
                    _state.value = FavoriteState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}