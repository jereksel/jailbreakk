plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij") version Versions.intellijGradlePlugin
}

intellij {
    updateSinceUntilBuild = false
    version = Versions.intellijIdea
    setPlugins("java", Deps.kotlinIdeaPlugin)

    localSourcesPath = "${project.buildDir}/intellij/sources"
    sandboxDirectory = "${project.buildDir}/intellij/sandbox"
    ideaDependencyCachePath = "${project.buildDir}/intellij/cache"
}

dependencies {
    implementation(Deps.kotlinStdLib)

    api(project(":kotlin-plugin", configuration = "shadow"))

    testImplementation(Deps.kotlinTest)
}

tasks.publishPlugin {
    setToken(System.getenv("JETBRAINS_TOKEN"))
}