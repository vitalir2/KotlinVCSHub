package io.vitalir.kotlinhub.server.app.infrastructure.auth.impl

import io.vitalir.kotlinhub.server.app.infrastructure.auth.BasicAuthManager
import io.vitalir.kotlinhub.server.app.infrastructure.encoding.Base64Manager
import io.vitalir.kotlinhub.server.app.infrastructure.http.AuthorizationHeader
import io.vitalir.kotlinhub.server.app.infrastructure.logging.impl.LogbackLogger
import io.vitalir.kotlinhub.server.app.user.domain.model.User
import io.vitalir.kotlinhub.server.app.user.domain.password.PasswordManager

internal class BasicAuthManagerImpl(
    private val base64Manager: Base64Manager,
    private val passwordManager: PasswordManager,
) : BasicAuthManager {

    private val logger = LogbackLogger()

    override fun checkCredentials(user: User, headerValue: String?): Boolean {
        val base64Value = headerValue?.let(AuthorizationHeader.BASIC::valueFromHeader) ?: return false
        logger.log("base64value=$base64Value")
        val (_, credentialsPassword) = base64Manager.decode(base64Value)
            .also { logger.log("Decoded credentials=$it") }
            .split(":")
        return passwordManager.comparePasswords(credentialsPassword, user.password)
    }
}
