package marwen.project.radioscope.ui.home.fragments.accueil

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.flow

import marwen.project.radioscope.data.local.ListenParameters
import marwen.project.radioscope.data.remote.dto.RadioHomeDto
import marwen.project.radioscope.di.AppModule
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCase
import marwen.project.radioscope.ui.MainActivity
import marwen.project.radioscope.ui.home.HomeScreen
import marwen.project.radioscope.ui.home.NavigationItem
import marwen.project.radioscope.ui.listen.ListenScreen
import marwen.project.radioscope.ui.splash.SplashScreen
import marwen.project.radioscope.ui.theme.RadioScopeTheme
import marwen.project.radioscope.utils.Resource
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(AppModule::class)
class AccueilScreenKtTest {



    @get:Rule(order = 0)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule= createAndroidComposeRule<MainActivity>()

    @get:Rule
    val initRule: MockitoRule = MockitoJUnit.rule()

   /* @Mock
    private lateinit var viewModel: AccueilViewModel*/

    @Mock
    private lateinit var getHomeRadioUseCase: GetHomeRadioUseCase




    @Before
    fun setUp() {
        //MockKAnnotations.init(this, relaxed = true)
        MockitoAnnotations.openMocks(this)
        hiltTestRule.inject()


    }

    private fun setContent(viewModel: AccueilViewModel?) {
        composeTestRule.activity.setContent {
            var mainNavController = rememberNavController()
            var homeNavController = rememberNavController()
            RadioScopeTheme() {
                NavHost(navController = mainNavController, startDestination = "splashScreen") {
                    composable("splashScreen") {
                        SplashScreen(navController = mainNavController)
                    }
                    composable("HomeScreen") {
                        HomeScreen(navController = mainNavController)
                    }

                    composable(
                        route = "ListenScreen/{listRadio}",
                        arguments = listOf(
                            navArgument("listRadio") {
                                type = ListenParameters
                            }
                        )
                    ) { backStackEntry ->
                        val listRadio =
                            backStackEntry.arguments?.getParcelable("listRadio") as ListenParameters?
                        ListenScreen(
                            mainNavController = mainNavController,
                            listRadio!!
                        ) // Here UserDetailsView is a composable.
                    }

                }
                NavHost(homeNavController, startDestination = NavigationItem.Accueil.route) {
                    composable(NavigationItem.Accueil.route) {
                        if (viewModel == null)
                            AccueilScreen(homeNavController, mainNavController)
                        else
                            AccueilScreen(homeNavController, mainNavController,viewModel)
                    }
                }
                //AccueilScreen( mainNavController ,homeNavController)
            }
        }
    }

    @After
    fun tearDown() {
    }
    @Test
    fun showListRadios(){
        setContent(null)
        composeTestRule.onNodeWithText("bside 1").assertExists()
        composeTestRule.onNodeWithText("bside 1").performClick()
        composeTestRule.onNodeWithText("Playing").assertExists()


    }
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun showError_msg(){

       /* var accueilHomeState = AccueilHomeState(false,null,"cnx error")
        `when`(viewModel.state.value).thenReturn(accueilHomeState)*/
        var viewModel = object : AccueilViewModel(getHomeRadioUseCase) {
            override fun getRadios(){
                _state.value = AccueilHomeState(error = "cnx error")
            }
        }
        /*val response : Resource<RadioHomeDto> = Resource.Error("cnx error",null)
        `when`(getHomeRadioUseCase.invoke()).thenReturn(flow {  emit(response)})*/
        setContent(viewModel)
        composeTestRule.run { waitForIdle() }
        composeTestRule.onNodeWithTag("error").assertExists()


    }


}