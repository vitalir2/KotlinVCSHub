package io.vitalir.server.kgit.di

import io.vitalir.server.kgit.ServerConfig
import io.vitalir.server.kgit.client.KGitHttpClient
import io.vitalir.server.kgit.git.GitAuthManager
import javax.servlet.Filter
import javax.servlet.http.HttpServletRequest
import org.eclipse.jgit.transport.resolver.RepositoryResolver

internal class ApplicationGraph(
    val serverConfig: ServerConfig,
    val httpClient: KGitHttpClient,
    val gitGraph: Git,
) {

    class Git(
        val gitAuthManager: GitAuthManager,
        val receivePackFilters: List<Filter>,
        val uploadPackFilters: List<Filter>,
        val repositoryResolver: RepositoryResolver<HttpServletRequest>,
    )
}
