plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm")
    kotlin("kapt")
    id("com.gradle.plugin-publish")
    id("com.jfrog.bintray")
}

gradlePlugin {
    plugins {
        create("jailbreakk") {
            id = project.group.toString()
            version = project.version
            implementationClass = "com.jereksel.jailbreakk.MyGradlePlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/jereksel/jailbreakk"
    vcsUrl = "https://github.com/jereksel/jailbreakk"
    description = "Typesafe reflection for Kotlin"
    tags = listOf("kotlin")

    (plugins) {

        "jailbreakk" {
            displayName = "Jailbreakk"
        }

    }
}

dependencies {
    implementation(Deps.kotlinStdLib)

    implementation(Deps.kotlinGradlePluginApi)

    compileOnly(Deps.autoService)
    kapt(Deps.autoService)
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
