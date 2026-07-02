plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.0"
}

ext["git_version"] = "Unknown"
ext["builder"] = System.getenv("GITHUB_ACTOR") ?: "Unknown"

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
