plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    kotlin("kapt")
    id("org.gradle.maven-publish")
}

gradlePlugin {
    plugins {
        create("jailbreakk") {
            id = Build.group
            implementationClass = "com.jereksel.jailbreakk.MyGradlePlugin"
        }
    }
}

dependencies {
    implementation(Deps.kotlinStdLib)

    implementation(Deps.kotlinGradlePluginApi)

    compileOnly(Deps.autoService)
    kapt(Deps.autoService)
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