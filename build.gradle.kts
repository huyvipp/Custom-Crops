import java.io.ByteArrayOutputStream

plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
}

val git: String = versionBanner()
val builder: String = builder()

ext["git_version"] = git
ext["builder"] = builder

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.gradleup.shadow")

    tasks.processResources {
        filteringCharset = "UTF-8"

        filesMatching(arrayListOf("custom-crops.properties")) {
            expand(rootProject.properties)
        }

        filesMatching(arrayListOf("*.yml", "*/*.yml")) {
            expand(
                Pair("project_version", rootProject.properties["project_version"]!!),
                Pair("config_version", rootProject.properties["config_version"]!!)
            )
        }
    }
}

fun versionBanner(): String {
    return runGit("rev-parse", "--short=8", "HEAD")
}

fun builder(): String {
    return runGit("config", "user.name")
}

fun runGit(vararg args: String): String {
    return try {
        val output = ByteArrayOutputStream()

        project.exec {
            commandLine("git", *args)
            standardOutput = output
            errorOutput = ByteArrayOutputStream()
            isIgnoreExitValue = true
        }

        output.toString().trim().ifBlank { "Unknown" }
    } catch (_: Exception) {
        "Unknown"
    }
}
