buildscript {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.shadow)
        classpath(Deps.bintray)
        classpath(Deps.pluginPublish)
    }
}

data class FileUpdate(
        val file: String,
        val pattern: String,
        val replacement: (String) -> String
)

val gradlePlugin = FileUpdate(
        file = "gradle-plugin/src/main/kotlin/com/jereksel/jailbreakk/Versions.kt",
        pattern = "version = \".*\"",
        replacement = { v -> "version = \"$v\"" }
)

val properties = FileUpdate(
        file = "gradle.properties",
        pattern = "version = .*$",
        replacement = { v -> "version = $v" }
)

val idePlugin = FileUpdate(
        file = "ide-plugin/src/main/resources/META-INF/plugin.xml",
        pattern = "<version>.*</version>",
        replacement = { v -> "<version>$v</version>" }
)

val changes = listOf(gradlePlugin, properties, idePlugin)

tasks.register("releaseVersion") {
    group = "deployment"
    doLast {
        val version = project.property("release.version")?.toString()
                ?: throw RuntimeException("Property 'release.version' is null")

        logger.lifecycle("Changing version to '$version'")

        changes.forEach {
            val file = file(it.file)
            file.writeText(file.readText().replace(Regex(it.pattern), it.replacement(version)))
        }
    }
}

tasks.register("releaseNext") {
    group = "deployment"
    doLast {
        val version = "NEXT"
        changes.forEach {
            val file = file(it.file)
            file.writeText(file.readText().replace(Regex(it.pattern), it.replacement(version)))
        }
    }
}

allprojects {

    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    tasks.withType<Test> {
        maxParallelForks = Runtime.getRuntime().availableProcessors()
    }

}
