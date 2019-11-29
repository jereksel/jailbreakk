import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import java.io.File

class KotlinSourcesPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        project.dependencies.add("compileOnly", project.files("/home/andrzej/MojeProgramy/kotlin/core/descriptors/src"))

    }

}