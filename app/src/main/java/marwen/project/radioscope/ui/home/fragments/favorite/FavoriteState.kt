package marwen.project.radioscope.ui.home.fragments.favorite

import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.data.remote.dto.RadioHomeDto

class FavoriteState (val isLoading :Boolean = false,
                     val listFavorite: List<Radio> = listOf(),
                     val error:String = "")
