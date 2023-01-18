package marwen.project.radioscope.services


/*sealed class PlayerStatus(open val radioId: String?) {
    data class Other(override val radioId: String? = null) : PlayerStatus(radioId)
    data class Playing(override val radioId: String) : PlayerStatus(radioId)
    data class Paused(override val radioId: String) : PlayerStatus(radioId)
    data class Cancelled(override val radioId: String? = null) : PlayerStatus(radioId)
    data class Ended(override val radioId: String) : PlayerStatus(radioId)
    data class Error(override val radioId: String, val exception: Exception?) : PlayerStatus(radioId)
}*/
class PlayerStatus( val radioId: String="",
    val isOther:Boolean = false,
    val isPlaying:Boolean = false,
    val isPaused:Boolean = false,
    val isCancelled:Boolean = false,
    val isEnded:Boolean = false,
    val error:String=""
)