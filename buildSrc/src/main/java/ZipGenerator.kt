import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.file.Files

abstract class ZipGenerator : DefaultTask() {

    @get:Input
    abstract var version: String

    @get:Input
    val packages: List<String> = listOf(
            "compiler/backend",
            "compiler/frontend",
            "compiler/frontend.java",
            "compiler/util",
            "core/descriptors"
    )

    @get:OutputFile
    abstract var file: File

    @TaskAction
    fun download() {

        val tempDir = Files.createTempDirectory("kotlin-compiler").toFile()
        val tempZip = File(tempDir, "kotlin.zip")
        val unpackedDir = File(tempDir, "unpacked")
        val url = "https://github.com/JetBrains/kotlin/archive/v${version}.zip"

        URL(url).openStream().use { iss ->
            FileOutputStream(tempZip).use { os ->
                iss.copyTo(os)
            }
        }

        unpackedDir.mkdir()

        ZipUtil.unpack(tempZip, unpackedDir) { name ->
            for (p in packages) {
                val prefix = "kotlin-${version}/${p}/src/"
                if (name.startsWith(prefix)) {
                    return@unpack name.removePrefix(prefix)
                }
            }
            null
        }

        ZipUtil.pack(unpackedDir, file)

    }

}