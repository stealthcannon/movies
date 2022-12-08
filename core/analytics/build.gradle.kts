plugins {
    id("movies-android-library")
    id("movies-android-hilt")
}

android {
    namespace = "org.michaelbel.movies.analytics"

    defaultConfig {
        minSdk = libs.versions.min.sdk.get().toInt()
        compileSdk = libs.versions.compile.sdk.get().toInt()
    }

    lint {
        quiet = true
        abortOnError = false
        ignoreWarnings = true
        checkDependencies = true
        lintConfig = file("${project.rootDir}/config/codestyle/lint.xml")
    }
}

dependencies {
    implementation(libs.firebase.analytics)
}