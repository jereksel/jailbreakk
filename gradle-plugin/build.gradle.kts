plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm")
    kotlin("kapt")
    id("com.gradle.plugin-publish") version "0.10.1"
}

gradlePlugin {
    plugins {
        create("jailbreakk") {
            id = Build.group
            implementationClass = "com.jereksel.jailbreakk.MyGradlePlugin"
            version = Build.version
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

//pluginBundle {
//    website = 'http://www.gradle.org/'
//    vcsUrl = 'https://github.com/gradle/gradle'
//    description = 'Greetings from here!'
//    tags = ['greetings', 'salutations']
//
//    plugins {
//        greetingsPlugin {
//            // id is captured from java-gradle-plugin configuration
//            displayName = 'Gradle Greeting plugin'
//        }
//    }
//}

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