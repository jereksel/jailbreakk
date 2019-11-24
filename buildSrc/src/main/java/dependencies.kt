object Build {
    const val group = "com.jereksel.jailbreakk"
    const val version = "0.0.1"
}

object Versions {
    const val autoService = "1.0-rc4"

    const val kotlinCompileTesting = "1.2.4"
    const val kotlinTest = "3.3.2"
    const val joor = "0.9.7"

    const val intellijIdea = "2019.2.4"
    const val intellijGradlePlugin = "0.4.11"

    const val kotlin = "1.3.60"
    const val kotlinIdeaPlugin = "1.3.60-release-IJ2019.2-1"

    const val mavenGradlePlugin = "2.1"
    const val shadow = "4.0.3"
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

    const val mavenGradlePlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradlePlugin}"

    const val shadow = "com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}"
}