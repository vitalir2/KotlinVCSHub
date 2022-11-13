package io.vitalir.kotlinhub.server.app.infrastructure.di

import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.CreateRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.GetRepositoryUseCase
import io.vitalir.kotlinhub.server.app.feature.repository.domain.usecase.RemoveRepositoryUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.PasswordManager
import io.vitalir.kotlinhub.server.app.feature.user.domain.persistence.UserPersistence
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.GetUserByIdentifierUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.LoginUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RegisterUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.RemoveUserUseCase
import io.vitalir.kotlinhub.server.app.feature.user.domain.usecase.UpdateUserUseCase
import io.vitalir.kotlinhub.server.app.infrastructure.auth.AuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.logging.Logger

class AppGraph(
    val appConfig: AppConfig,
    val user: UserGraph,
    val repository: RepositoryGraph,
    val auth: AuthGraph,
    val logger: Logger,
) {

    class UserGraph(
        val userPersistence: UserPersistence,
        val loginUseCase: LoginUseCase,
        val registerUserUseCase: RegisterUserUseCase,
        val getUserByIdentifierUseCase: GetUserByIdentifierUseCase,
        val updateUserUseCase: UpdateUserUseCase,
        val removeUserUseCase: RemoveUserUseCase,
    )

    class RepositoryGraph(
        val createRepositoryUseCase: CreateRepositoryUseCase,
        val getRepositoryUseCase: GetRepositoryUseCase,
        val removeRepositoryUseCase: RemoveRepositoryUseCase,
    )

    class AuthGraph(
        val authManager: AuthManager,
        val passwordManager: PasswordManager,
    )
}
