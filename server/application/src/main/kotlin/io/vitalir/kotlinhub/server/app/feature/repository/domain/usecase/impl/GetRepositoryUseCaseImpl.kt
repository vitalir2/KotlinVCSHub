package io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.impl

import arrow.core.left
import arrow.core.rightIfNotNull
import io.vitalir.kotlinhub.server.app.feature.repository.domain.model.RepositoryError
import io.vitalir.kotlinhub.server.app.feature.repository.domain.persistence.RepositoryPersistence
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryResult
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserIdentifier
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.shared.feature.user.UserId

internal class GetRepositoryUseCaseImpl(
    private val repositoryPersistence: RepositoryPersistence,
    private val userPersistence: UserPersistence,
) : GetRepositoryUseCase {

    override suspend fun invoke(
        userIdentifier: UserIdentifier,
        repositoryName: String,
    ): GetRepositoryResult {
        return when {
            userPersistence.isUserExists(userIdentifier).not() -> {
                RepositoryError.Get.UserDoesNotExist(userIdentifier).left()
            }
            else -> {
                val repository = repositoryPersistence.getRepository(userIdentifier, repositoryName)
                repository.rightIfNotNull {
                    RepositoryError.Get.RepositoryDoesNotExist(userIdentifier, repositoryName)
                }
            }
        }
    }

}
