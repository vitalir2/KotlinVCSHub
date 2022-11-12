package io.vitalir.kotlinhub.server.app.feature.user.routes

import io.ktor.http.*
import io.ktor.server.routing.*
import io.vitalir.kotlinhub.server.app.common.routes.ResponseData
import io.vitalir.kotlinhub.server.app.common.routes.jwtAuth
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.UserError
import io.vitalir.kotlinhub.server.app.feature.user.routes.getuser.getUserByUsernameRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.login.loginRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.registration.registerUserRoute
import io.vitalir.kotlinhub.server.app.feature.user.routes.removeuser.removeCurrentUser
import io.vitalir.kotlinhub.server.app.feature.user.routes.updateuser.updateCurrentUser
import io.vitalir.kotlinhub.server.app.infrastructure.config.AppConfig
import io.vitalir.kotlinhub.server.app.infrastructure.di.AppGraph

internal fun Routing.userRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    route("users/") {
        unauthorizedUserRoutes(jwtConfig, userGraph)

        authIndependentUserRoutes(userGraph)

        authorizedUserRoutes(userGraph)
    }
}

private fun Route.unauthorizedUserRoutes(
    jwtConfig: AppConfig.Jwt,
    userGraph: AppGraph.UserGraph,
) {
    registerUserRoute(userGraph.registerUserUseCase)
    loginRoute(jwtConfig, userGraph.loginUseCase)
}

private fun Route.authIndependentUserRoutes(
    userGraph: AppGraph.UserGraph,
) {
    getUserByUsernameRoute(userGraph.getUserByIdentifierUseCase)
}

private fun Route.authorizedUserRoutes(
    userGraph: AppGraph.UserGraph,
) {
    jwtAuth {
        updateCurrentUser(userGraph.updateUserUseCase)
        removeCurrentUser(userGraph.removeUserUseCase)
    }
}

internal fun getErrorResponseData(userError: UserError): ResponseData {
    return when (userError) {
        is UserError.InvalidCredentials -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "invalid credentials",
            )
        }

        is UserError.ValidationFailed -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "invalid credentials format",
            )
        }

        is UserError.UserAlreadyExists -> {
            ResponseData.fromErrorData(
                code = HttpStatusCode.BadRequest,
                errorMessage = "user already exists",
            )
        }
    }
}
