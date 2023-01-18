package marwen.project.radioscope.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import marwen.project.radioscope.data.remote.RadioScopeApi
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCase
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCaseImpl
import marwen.project.radioscope.repositories.RadioScopeRepository
import marwen.project.radioscope.repositories.RadioScopeRepositoryFake
import marwen.project.radioscope.repositories.RadioScopeRepositoryImpl
import marwen.project.radioscope.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideRadioScopeApi():RadioScopeApi = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RadioScopeApi::class.java)

    @Singleton
    @Provides
    //fun provideRadioScopeRepository(api : RadioScopeApi) : RadioScopeRepository = RadioScopeRepositoryImpl(api)}
    fun provideRadioScopeRepository(api : RadioScopeApi) : RadioScopeRepository = RadioScopeRepositoryFake()
    @Singleton
    @Provides
    //fun provideRadioScopeRepository(api : RadioScopeApi) : RadioScopeRepository = RadioScopeRepositoryImpl(api)}
    fun provideGetHomeRadioUseCase(radioScopeRepository: RadioScopeRepository) : GetHomeRadioUseCase = GetHomeRadioUseCaseImpl(radioScopeRepository)
}