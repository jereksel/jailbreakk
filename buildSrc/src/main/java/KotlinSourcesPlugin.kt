import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.the
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class KotlinSourcesPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        //Don't generate sources in CI
        if (System.getenv("CI") != null) {
            return
        }

        val repo = project.file("build/repo")
        val version = "1.3.61"
        repo.mkdirs()

        val kotlinEmbeddableLocation = "org/jetbrains/kotlin/kotlin-compiler-embeddable/${version}"

        val localJarLocation = File(repo, kotlinEmbeddableLocation)

        if (!localJarLocation.exists()) {
            localJarLocation.mkdirs()
        }

        val jar = project.tasks.register<FileDownloader>("kotlin-compiler-download-jar") {
            this.url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-compiler-embeddable/${version}/kotlin-compiler-embeddable-${version}.jar"
            this.file = File(localJarLocation, "kotlin-compiler-embeddable-${version}.jar")
        }

        val pom = project.tasks.register<FileDownloader>("kotlin-compiler-download-pom") {
            this.url = "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-compiler-embeddable/${version}/kotlin-compiler-embeddable-${version}.pom"
            this.file = File(localJarLocation, "kotlin-compiler-embeddable-${version}.pom")
        }

        val sources = project.tasks.register<ZipGenerator>("kotlin-compiler-download-sources") {
            this.file = File(localJarLocation, "kotlin-compiler-embeddable-${version}-sources.jar")
            this.version = version
        }

        project.tasks.findByPath("compileKotlin")!!.dependsOn(jar.name, pom.name, sources.name)

        val repositoryName = "Kotlin compiler repo"

        if (project.repositories.find { it.name == repositoryName } == null) {

            project.repositories {
                val a = maven {
                    name = repositoryName
                    setUrl(repo)
                }
                remove(a)
                addFirst(a)
            }

        }

    }

}