package marwen.project.radioscope.data.remote.dto

data class RadioSearchDto(
    val count: Int,
    val count_total: Int,
    val pages: Int,
    val stations: List<Radio>,
    val status: String
)