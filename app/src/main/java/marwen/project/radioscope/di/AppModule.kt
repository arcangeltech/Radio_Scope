package marwen.project.radioscope.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import marwen.project.radioscope.data.local.db.RadioScopeDao
import marwen.project.radioscope.data.local.db.RadioScopeDatabase
import marwen.project.radioscope.data.remote.RadioScopeApi
import marwen.project.radioscope.domain.use_cases.AddFavoriteUseCase
import marwen.project.radioscope.domain.use_cases.FavoriteUseCases
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCase
import marwen.project.radioscope.domain.use_cases.GetHomeRadioUseCaseImpl
import marwen.project.radioscope.repositories.*
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
    fun provideGetHomeRadioUseCase(radioScopeRepository: RadioScopeRepository) : GetHomeRadioUseCase = GetHomeRadioUseCaseImpl(radioScopeRepository)

    @Singleton
    @Provides
    fun provideRadioScopeDao (RadioScopeDatabase: RadioScopeDatabase): RadioScopeDao = RadioScopeDatabase.getRadioScopeDao()

    @Singleton
    @Provides
    fun provideRadioScopeDb (context: Application): RadioScopeDatabase = RadioScopeDatabase.invoke(context)
    @Singleton
    @Provides
    fun provideLocalRepository(dao: RadioScopeDao) : LocalRadioScopeRepository = LocalRadioScopeRepositoryImpl(dao)
}