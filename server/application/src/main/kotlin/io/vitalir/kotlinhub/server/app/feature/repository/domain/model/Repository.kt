package io.vitalir.kotlinhub.server.app.feature.repository.domain.model

import io.vitalir.kotlinhub.server.app.common.domain.LocalDateTimeProvider
import io.vitalir.kotlinhub.server.app.feature.user.domain.model.User
import io.vitalir.kotlinhub.shared.common.network.Path
import io.vitalir.kotlinhub.shared.common.network.Scheme
import io.vitalir.kotlinhub.shared.common.network.Url
import java.time.LocalDateTime

data class Repository(
    val id: RepositoryId,
    val owner: User,
    val name: String,
    val accessMode: AccessMode,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val description: String? = null,
) {

    fun createResourceUrl(): Url {
        return Url(
            scheme = Scheme.HTTP, // TODO pass scheme / host
            host = "localhost",
            path = Path(
                owner.id.toString(),
                "$name.git",
            )
        )
    }

    enum class AccessMode {
        PUBLIC,
        PRIVATE,
    }

    companion object {
        internal const val AUTOINCREMENT_ID = 0
       fun fromInitData(
           owner: User,
           initData: CreateRepositoryData,
           localDateTimeProvider: LocalDateTimeProvider,
       ): Repository {
           val createdAtDateTime = localDateTimeProvider.now()
           return Repository(
               id = AUTOINCREMENT_ID,
               owner = owner,
               name = initData.name,
               accessMode = initData.accessMode,
               description = initData.description,
               createdAt = createdAtDateTime,
               updatedAt = createdAtDateTime,
           )
       }
    }
}
