package com.codegenai.app.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ProjectManager(private val context: Context) {

    data class Project(
        val id: String,
        val name: String,
        val createdAt: Long,
        val zipPath: String
    )

    suspend fun createProjectZip(projectName: String, code: String): String {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val projectDir = File(downloadsDir, projectName)
        if (!projectDir.exists()) {
            projectDir.mkdirs()
        }

        // Create main code file
        val codeFile = File(projectDir, "main.kt")
        codeFile.writeText(code)

        // Create build.gradle
        val buildGradle = File(projectDir, "build.gradle")
        buildGradle.writeText(generateBuildGradle())

        // Create README
        val readme = File(projectDir, "README.md")
        readme.writeText(generateReadme(projectName))

        // Create ZIP
        val zipFile = File(downloadsDir, "$projectName.zip")
        zipFile.createNewFile()

        ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
            projectDir.walk().forEach { file ->
                if (file.isFile) {
                    val entryName = file.relativeTo(projectDir).path
                    zipOut.putNextEntry(ZipEntry(entryName))
                    file.inputStream().use { input ->
                        input.copyTo(zipOut)
                    }
                    zipOut.closeEntry()
                }
            }
        }

        // Clean up temp directory
        projectDir.deleteRecursively()

        // Save project info
        saveProjectInfo(projectName, zipFile.absolutePath)

        return zipFile.absolutePath
    }

    suspend fun getAllProjects(): List<Project> {
        val sharedPreferences = context.getSharedPreferences("projects", Context.MODE_PRIVATE)
        val projects = mutableListOf<Project>()
        
        sharedPreferences.all.forEach { (key, value) ->
            if (value is String) {
                val parts = value.split("|")
                if (parts.size == 3) {
                    projects.add(
                        Project(
                            id = key,
                            name = parts[0],
                            createdAt = parts[1].toLongOrNull() ?: 0L,
                            zipPath = parts[2]
                        )
                    )
                }
            }
        }
        return projects.sortedByDescending { it.createdAt }
    }

    suspend fun deleteProject(projectId: String) {
        val sharedPreferences = context.getSharedPreferences("projects", Context.MODE_PRIVATE)
        val projectInfo = sharedPreferences.getString(projectId, "")
        if (projectInfo!!.isNotEmpty()) {
            val zipPath = projectInfo.split("|")[2]
            File(zipPath).delete()
            sharedPreferences.edit().remove(projectId).apply()
        }
    }

    private fun saveProjectInfo(projectName: String, zipPath: String) {
        val sharedPreferences = context.getSharedPreferences("projects", Context.MODE_PRIVATE)
        val projectId = System.nanoTime().toString()
        val projectInfo = "$projectName|${System.currentTimeMillis()}|$zipPath"
        sharedPreferences.edit().putString(projectId, projectInfo).apply()
    }

    private fun generateBuildGradle(): String {
        return """
plugins {
    id 'org.jetbrains.kotlin.jvm' version '1.8.0'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.8.0'
}
        """.trimIndent()
    }

    private fun generateReadme(projectName: String): String {
        return """
# $projectName

This project was generated using CodeGenAI.

## How to run

1. Make sure you have Gradle installed
2. Run: `gradle build`
3. Run: `gradle run`

## Files

- `main.kt` - Main source code
- `build.gradle` - Build configuration
- `README.md` - This file
        """.trimIndent()
    }
}
