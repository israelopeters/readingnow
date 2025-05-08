package com.example.readingnow.service

import com.example.readingnow.data.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.jackson.jackson
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "http://ReadingNow-env.eba-pc3m5spj.eu-west-2.elasticbeanstalk.com"

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient() {
            install(ContentNegotiation) {
                jackson()
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
                filter { request ->
                    request.url.host.contains(BASE_URL)
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
            defaultRequest {
                url(BASE_URL)
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiRepository(): ApiRepository {
        return ApiRepository(provideHttpClient())
    }
}