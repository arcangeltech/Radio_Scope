package marwen.project.radioscope.services

import androidx.lifecycle.LiveData
import marwen.project.radioscope.data.remote.dto.Radio

interface PlayerCallback {

    val playerStatusLiveData: LiveData<PlayerStatus>

    fun play(radio: Radio)
    fun stop()

}