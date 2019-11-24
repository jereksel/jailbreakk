import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
    id("org.gradle.maven-publish")
}

dependencies {
    implementation(Deps.kotlinReflect)

    compileOnly(Deps.kotlinStdLib)
    compileOnly(Deps.kotlinCompilerEmbeddable)
    compileOnly(Deps.autoService)
    kapt(Deps.autoService)

    testImplementation(Deps.kotlinCompilerEmbeddable)
    testImplementation(Deps.kotlinCompileTesting)
    testImplementation(Deps.kotlinTest)
    testImplementation(Deps.joor)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// the following code makes sure that our kotlin-plugin is compatible with the kotlin-compiler-embeddable package
// which is used by gradle

val kotlinEmbeddableRootPackage = "org.jetbrains.kotlin"

val packagesToRelocate =
    listOf(
        "com.intellij",
        "com.google",
        "com.sampullara",
        "org.apache",
        "org.jdom",
        "org.picocontainer",
        "org.jline",
        "org.fusesource",
        "kotlinx.coroutines"
    )

val shadowJar: ShadowJar by tasks

shadowJar.apply {
    archiveClassifier.set("")
    relocate("$kotlinEmbeddableRootPackage.protobuf", "com.google.protobuf")
    packagesToRelocate.forEach {
        relocate("$kotlinEmbeddableRootPackage.$it", it)
    }
    // todo relocate("javax.inject", "$kotlinEmbeddableRootPackage.javax.inject")
    relocate("$kotlinEmbeddableRootPackage.org.fusesource", "org.fusesource") {
        // TODO: remove "it." after #KT-12848 get addressed
        exclude("org.fusesource.jansi.internal.CLibrary")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Build.group
            artifactId = project.name
            version = Build.version

            from(components["java"])
        }
    }
}