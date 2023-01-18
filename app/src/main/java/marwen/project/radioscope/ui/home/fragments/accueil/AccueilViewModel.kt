package marwen.project.radioscope.ui.home.fragments.accueil

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCase
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

@HiltViewModel
class AccueilViewModel @Inject constructor(private val getRadioHomeRadioUseCase: GetHomeRadioUseCase) :ViewModel(){
    val _state = mutableStateOf(AccueilHomeState())
    val state: State<AccueilHomeState> = _state

    init {
        getRadios()
    }

    fun getRadios() {
        var result = getRadioHomeRadioUseCase.invoke()
        result.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = AccueilHomeState(radioHomeDto = result.data)
                }

                is Resource.Error -> {
                    _state.value = AccueilHomeState(error = result.message ?: "erreur")
                }

                is Resource.Loading -> {
                    _state.value = AccueilHomeState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}