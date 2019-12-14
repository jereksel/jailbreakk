import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("com.github.johnrengelman.shadow")
    id("org.gradle.maven-publish")
    id("com.jfrog.bintray")
}

apply<KotlinSourcesPlugin>()

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

val sourcesJar by tasks.creating(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    classifier = "sources"
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            artifact(sourcesJar)

            from(components["java"])
        }
    }
}

tasks.jar {
    manifest {
        attributes(
                "Implementation-Title" to "jailbreakk-kotlin-plugin",
                "Implementation-Version" to project.version
        )
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true
    with(pkg) {
        repo = "maven"
        name = "Jailbreakk"
        publish = true
        publicDownloadNumbers = true
        with(version) {
            name = "${project.version}"
            vcsTag = "v${project.version}"
        }
    }
    setPublications("maven")
}