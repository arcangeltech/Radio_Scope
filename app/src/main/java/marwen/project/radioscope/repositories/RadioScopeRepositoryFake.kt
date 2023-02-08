package marwen.project.radioscope.repositories

import marwen.project.radioscope.data.remote.dto.Country
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.data.remote.dto.RadioSearchDto

class RadioScopeRepositoryFake: RadioScopeRepository {
    override suspend fun getHomeRadioList(): RadioHomeDto {
        return RadioHomeDto(
                status = "ok",
                featured = listOf(
                Radio(
                    radio_id = 56581,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ),Radio(
                        radio_id = 56582,
                        radio_name = "RUM - Radio Universitaria do Minho",
                        radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url ="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name= "Portugal",
                        country_id= 76
                    ),Radio(
                        radio_id = 56583,
                        radio_name = "RUM - Radio Universitaria do Minho",
                        radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url ="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name= "Portugal",
                        country_id= 76
                    ),Radio(
                        radio_id = 56584,
                        radio_name = "RUM - Radio Universitaria do Minho",
                        radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url ="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name= "Portugal",
                        country_id= 76
                    ),Radio(
                        radio_id = 56585,
                        radio_name = "RUM - Radio Universitaria do Minho",
                        radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url ="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name= "Portugal",
                        country_id= 76
                    )),
                countries= listOf(
                    Country(
                    country_id= 102,
                    country_name= "Morocco",
                    country_image= "https://visitdpstudio.net/radio_world/upload/category/9632-2022-01-20.gif"
                    ),Country(
                        country_id= 102,
                        country_name= "Morocco",
                        country_image= "https://visitdpstudio.net/radio_world/upload/category/9632-2022-01-20.gif"
                    ),Country(
                        country_id= 102,
                        country_name= "Morocco",
                        country_image= "https://visitdpstudio.net/radio_world/upload/category/9632-2022-01-20.gif"
                    )),

                recent = listOf(
                Radio(
                    radio_id= 111991,
                    radio_name="bside 1",
                    radio_image="https://visitdpstudio.net/radio_world/upload/60435616-2022-05-09.png",
                    radio_url="http://s3.radio.co/s4e064139a/listen",
                    genre="Community",
                    country_name="Canada",
                    country_id=196
                ),Radio(
                        radio_id= 111990,
                        radio_name="bside 2",
                        radio_image="https://visitdpstudio.net/radio_world/upload/95661311-2022-03-17.png",
                        radio_url="http://centova.radios.pt:9558/stream",
                        genre="Community",
                        country_name="Canada",
                        country_id=196
                    ),Radio(
                        radio_id= 111992,
                        radio_name="bside 3",
                        radio_image="https://visitdpstudio.net/radio_world/upload/60435616-2022-05-09.png",
                        radio_url="http://s3.radio.co/s4e064139a/listen",
                        genre="Community",
                        country_name="Canada",
                        country_id=196
                    ),Radio(
                        radio_id= 111993,
                        radio_name="bside 4",
                        radio_image="https://visitdpstudio.net/radio_world/upload/60435616-2022-05-09.png",
                        radio_url="http://s3.radio.co/s4e064139a/listen",
                        genre="Community",
                        country_name="Canada",
                        country_id=196
                    )),

                random= listOf(
                Radio(
                    radio_id= 56589,
                    radio_name="RUM - Radio Universitaria do Minho",
                    radio_image="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name="Portugal",
                    country_id= 76
                ),
                    Radio(
                        radio_id= 56588,
                        radio_name="RUM - Radio Universitaria do Minho",
                        radio_image="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name="Portugal",
                        country_id= 76
                    ),Radio(
                        radio_id= 56587,
                        radio_name="RUM - Radio Universitaria do Minho",
                        radio_image="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                        radio_url="http://centova.radios.pt:9558/stream",
                        genre="Talk",
                        country_name="Portugal",
                        country_id= 76
                    )))



    }

    override suspend fun getSearchRadioList(search:String,count: String?, page: String?): RadioSearchDto {
        return RadioSearchDto(
            status="ok",
            count=10,
            count_total=25,
            pages=2,
            stations =  listOf(
                Radio(
                    radio_id = 56581,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ),Radio(
                    radio_id = 56582,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ),Radio(
                    radio_id = 56583,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ),Radio(
                    radio_id = 56584,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ),Radio(
                    radio_id = 56585,
                    radio_name = "RUM - Radio Universitaria do Minho",
                    radio_image ="https://visitdpstudio.net/radio_world/upload/36032912-2022-03-16.png",
                    radio_url ="http://centova.radios.pt:9558/stream",
                    genre="Talk",
                    country_name= "Portugal",
                    country_id= 76
                ))

        )
    }
}