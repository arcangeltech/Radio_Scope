package marwen.project.radioscope.ui.home.fragments.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import marwen.project.radioscope.domain.use_cases.FavoriteUseCases
import marwen.project.radioscope.domain.use_cases.GetSearchRadioUseCase
import marwen.project.radioscope.ui.home.fragments.favorite.FavoriteState
import marwen.project.radioscope.utils.Resource
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchUseCase:GetSearchRadioUseCase) : ViewModel() {
    val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state


    fun searchRadios(search:String,count:String?,page:String?) {
        var result = searchUseCase.invoke(search, count, page)
        result.onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = SearchState(listSearch = result.data?.stations?: listOf())
                }

                is Resource.Error -> {
                    _state.value = SearchState(error = result.message ?: "erreur")
                }

                is Resource.Loading -> {
                    _state.value = SearchState(isLoading = true)

                }
            }

        }.launchIn(viewModelScope)
    }
}