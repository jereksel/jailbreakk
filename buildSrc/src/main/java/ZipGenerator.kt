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
            "core/descriptors",
            "core/descriptors.jvm"
    )

    @get:OutputFile
    abstract var file: File

    @TaskAction
    fun download() {

        val asmVersion = "7.0.1"
        val tempDir = Files.createTempDirectory("kotlin-compiler").toFile()
        val tempZip = File(tempDir, "kotlin.zip")
        val unpackedDir = File(tempDir, "unpacked")
        val url = "https://github.com/JetBrains/kotlin/archive/v${version}.zip"
        val asmAllSources = "https://dl.bintray.com/jetbrains/intellij-third-party-dependencies/org/jetbrains/intellij/deps/asm-all/${asmVersion}/asm-all-${asmVersion}-sources.jar"
        val asmZip = File(tempDir, "asm.jar")

        URL(url).openStream().use { iss ->
            FileOutputStream(tempZip).use { os ->
                iss.copyTo(os)
            }
        }

        URL(asmAllSources).openStream().use { iss ->
            FileOutputStream(asmZip).use { os ->
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

        ZipUtil.unpack(asmZip, unpackedDir)

        ZipUtil.pack(unpackedDir, file)

    }

}