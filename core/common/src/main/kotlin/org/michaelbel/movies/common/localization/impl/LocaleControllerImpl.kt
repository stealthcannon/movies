package org.michaelbel.movies.common.localization.impl

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import org.michaelbel.movies.analytics.MoviesAnalytics
import org.michaelbel.movies.analytics.event.SelectLanguageEvent
import org.michaelbel.movies.common.dispatchers.MoviesDispatchers
import org.michaelbel.movies.common.localization.LocaleController
import org.michaelbel.movies.common.localization.model.AppLanguage
import javax.inject.Inject

internal class LocaleControllerImpl @Inject constructor(
    private val dispatchers: MoviesDispatchers,
    private val analytics: MoviesAnalytics
): LocaleController {

    override val language: String
        get() = AppCompatDelegate.getApplicationLocales()[0]?.language ?: AppLanguage.English.code

    override val appLanguage: Flow<AppLanguage> = flowOf(AppLanguage.transform(language))

    override suspend fun selectLanguage(language: AppLanguage) = withContext(dispatchers.io) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))
        analytics.logEvent(SelectLanguageEvent(language.toString()))
    }
}