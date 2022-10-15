package io.vitalir.kotlinvcshub.server.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.vitalir.kotlinvcshub.server.common.routes.AuthVariant
import io.vitalir.kotlinvcshub.server.common.routes.ErrorResponse
import io.vitalir.kotlinvcshub.server.common.routes.ResponseData
import io.vitalir.kotlinvcshub.server.infrastructure.config.AppConfig

fun Application.configureSecurity(
    jwtConfig: AppConfig.Jwt,
) {
    authentication {
        jwt(AuthVariant.JWT.authName) {
            realm = jwtConfig.realm

            val jwtVerifier = JWT.require(Algorithm.HMAC256(jwtConfig.secret)).apply {
                withAudience(jwtConfig.audience)
                withIssuer(jwtConfig.issuer)
            }.build()
            verifier(jwtVerifier)

            validate { credential ->
                if (credential.payload.getClaim("login").asString().isNotBlank()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                val responseData = ResponseData(
                    code = HttpStatusCode.Unauthorized,
                    body = ErrorResponse(code = HttpStatusCode.Unauthorized.value, message = "Token is not valid or has expired"),
                )
                call.respond(responseData)
            }
        }
    }
}
