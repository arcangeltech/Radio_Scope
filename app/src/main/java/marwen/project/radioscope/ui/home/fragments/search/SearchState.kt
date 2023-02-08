package marwen.project.radioscope.ui.home.fragments.search

import marwen.project.radioscope.data.remote.dto.Radio

class SearchState(val isLoading :Boolean = false,
                  val listSearch: List<Radio> = listOf(),
                  val error:String = "")