plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij") version Versions.intellijGradlePlugin
}

intellij {
    updateSinceUntilBuild = false
    version = Versions.intellijIdea
    setPlugins("java", Deps.kotlinIdeaPlugin)
}

dependencies {
    implementation(Deps.kotlinStdLib)

    api(project(":kotlin-plugin", configuration = "shadow"))

    testImplementation(Deps.kotlinTest)
}

tasks.publishPlugin {
    setToken(System.getenv("JETBRAINS_TOKEN"))
}