object Build {
    const val group = "com.jereksel.jailbreakk"
    const val version = "0.0.1"
}

object Versions {
    const val autoService = "1.0-rc6"

    const val kotlinCompileTesting = "1.2.5"
    const val kotlinTest = "3.4.2"
    const val joor = "0.9.7"

    const val intellijIdea = "2019.3"
    const val intellijGradlePlugin = "0.4.14"

    const val kotlin = "1.3.61"
    const val kotlinIdeaPlugin = "1.3.61-release-IJ2019.3-1"

    const val shadow = "5.2.0"

    const val bintray = "1.8.4"
}

object Deps {
    const val autoService = "com.google.auto.service:auto-service:${Versions.autoService}"

    const val intellijGradlePlugin = "org.jetbrains.intellij"

    const val kotlinIdeaPlugin = "org.jetbrains.kotlin:${Versions.kotlinIdeaPlugin}"

    const val kotlinCompileTesting = "com.github.tschuchortdev:kotlin-compile-testing:${Versions.kotlinCompileTesting}"
    const val kotlinTest = "io.kotlintest:kotlintest-runner-junit5:${Versions.kotlinTest}"
    const val joor = "org.jooq:joor-java-6:${Versions.joor}"

    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val kotlinCompilerEmbeddable = "org.jetbrains.kotlin:kotlin-compiler-embeddable:${Versions.kotlin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinGradlePluginApi = "org.jetbrains.kotlin:kotlin-gradle-plugin-api:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val shadow = "com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}"

    const val bintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"
}