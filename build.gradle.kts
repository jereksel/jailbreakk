buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.shadow)
        classpath(Deps.bintray)
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
