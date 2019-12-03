import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.net.URL

abstract class FileDownloader : DefaultTask() {

    @get:Input
    abstract var url: String

    @get:OutputFile
    abstract var file: File

    @TaskAction
    fun download() {
        URL(url).openStream().use { iss ->
            FileOutputStream(file).use { os ->
                iss.copyTo(os)
            }
        }
    }

}