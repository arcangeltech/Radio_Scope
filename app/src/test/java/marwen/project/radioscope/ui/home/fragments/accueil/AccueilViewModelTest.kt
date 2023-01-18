package marwen.project.radioscope.ui.home.fragments.accueil

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import marwen.TestCoroutineRule
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCase
import marwen.project.radioscope.repositories.RadioScopeRepositoryFake
import marwen.project.radioscope.utils.Resource
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
@RunWith(MockitoJUnitRunner::class)
@SmallTest
class AccueilViewModelTest {
    private lateinit var viewModel: AccueilViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    /*@ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()*/
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    /*@Mock
    private lateinit var radioScopeRepository: RadioScopeRepository*/
    @Mock
    private lateinit var getHomeRadioUseCase: GetHomeRadioUseCase


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
       /*radioScopeRepository = Mockito.mock(radioScopeRepository::class.java)
        getHomeRadioUseCase = Mockito.mock(getHomeRadioUseCase::class.java)*/
        //viewModel = AccueilViewModel(getHomeRadioUseCase)
    }

    @After
    fun tearDown() {
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get radios return sucesss`(){
        runTest {
            val radioHomeDto = RadioScopeRepositoryFake().getHomeRadioList()
            val response = Resource.Success(radioHomeDto)
            Mockito.`when`(getHomeRadioUseCase.invoke()).thenReturn(flow {emit(response)})
            viewModel = AccueilViewModel(getHomeRadioUseCase)
            this.advanceUntilIdle()
            viewModel.getRadios()
            this.advanceUntilIdle()
            Truth.assertThat(viewModel.state.value.radioHomeDto).isEqualTo(radioHomeDto)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get radios return Error`(){
        testCoroutineRule.runTest {
            val response : Resource<RadioHomeDto> = Resource.Error("cnx error",null)
            Mockito.`when`(getHomeRadioUseCase.invoke()).thenReturn(flow {  emit(response)})
            viewModel = AccueilViewModel(getHomeRadioUseCase)
            advanceUntilIdle()
            Truth.assertThat(viewModel._state.value.error).isEqualTo("cnx error")
        }
    }
}