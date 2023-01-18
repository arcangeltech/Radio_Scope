package marwen.project.radioscope.ui.home.fragments.accueil

import marwen.project.radioscope.data.remote.dto.RadioHomeDto

class AccueilHomeState( val isLoading :Boolean = false,
                        val radioHomeDto: RadioHomeDto? = null,
                        val error:String = ""
)