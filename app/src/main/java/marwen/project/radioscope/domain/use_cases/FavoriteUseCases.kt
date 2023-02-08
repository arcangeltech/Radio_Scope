package marwen.project.radioscope.domain.use_cases

import javax.inject.Inject

data class FavoriteUseCases @Inject constructor (
        val addFavoriteUseCase: AddFavoriteUseCase,
        val deleteFavoriteUseCase: DeleteFavoriteUseCase,
        val getFavoriteUseCase: GetFavoriteUseCase
        )