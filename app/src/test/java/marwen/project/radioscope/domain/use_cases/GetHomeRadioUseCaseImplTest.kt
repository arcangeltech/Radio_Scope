package marwen.project.radioscope.domain.use_cases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import marwen.MainCoroutineRule
import marwen.project.radioscope.repositories.RadioScopeRepository
import marwen.project.radioscope.repositories.RadioScopeRepositoryFake
import marwen.project.radioscope.utils.Resource
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import kotlin.time.ExperimentalTime


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
@SmallTest
@ExperimentalTime

class GetHomeRadioUseCaseImplTest{

    private lateinit var getHomeRadioUseCaseImpl: GetHomeRadioUseCaseImpl
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: RadioScopeRepository




    @Before
    fun setup(){
        MockKAnnotations.init(this)
        getHomeRadioUseCaseImpl = GetHomeRadioUseCaseImpl(repository)
    }
    @Test
    fun `get radios return success`()=
        runTest {
            val radioHomeDto = RadioScopeRepositoryFake().getHomeRadioList()
            val response = Resource.Success(radioHomeDto)
            Mockito.`when`(repository.getHomeRadioList()).thenReturn(radioHomeDto)
            var test = getHomeRadioUseCaseImpl.invoke()
            advanceUntilIdle()
            Truth.assertThat(test.last().data).isEqualTo(radioHomeDto)
        }
    @Test
    fun `get radios return error`()=
        runTest {
            val radioHomeDto = RadioScopeRepositoryFake().getHomeRadioList()
            val response = Resource.Success(radioHomeDto)
            Mockito.`when`(repository.getHomeRadioList()).thenThrow(NullPointerException())
            var test = getHomeRadioUseCaseImpl.invoke()
            advanceUntilIdle()
            Truth.assertThat(test.last().message).isEqualTo("Couldn't reach server. Check your internet connect")
        }

}