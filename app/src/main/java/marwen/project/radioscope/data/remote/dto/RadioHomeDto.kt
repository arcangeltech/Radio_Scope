package marwen.project.radioscope.data.remote.dto

data class RadioHomeDto(
    val countries: List<Country>,
    val featured: List<Radio>,
    val random: List<Radio>,
    val recent: List<Radio>,
    val status: String
)