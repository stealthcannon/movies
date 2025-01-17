package org.michaelbel.movies.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.michaelbel.movies.repository.AccountRepository
import org.michaelbel.movies.repository.AccountRepositoryImpl
import org.michaelbel.movies.repository.AuthenticationRepository
import org.michaelbel.movies.repository.AuthenticationRepositoryImpl
import org.michaelbel.movies.repository.MovieRepository
import org.michaelbel.movies.repository.MovieRepositoryImpl
import org.michaelbel.movies.repository.SettingsRepository
import org.michaelbel.movies.repository.SettingsRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun provideMovieRepository(
        repository: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    fun provideAuthenticationRepository(
        repository: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    fun provideAccountRepository(
        repository: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    fun provideSettingsRepository(
        repository: SettingsRepositoryImpl
    ): SettingsRepository
}