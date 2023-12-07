plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "org.aurora.gobatis"
version = "0.0.1"

repositories {
    mavenCentral()
}

sourceSets["main"].java.srcDirs("src/main/gen")

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.3")
    type.set("GO")
    plugins.set(
            listOf(
                    "org.jetbrains.plugins.go:233.11799.196",
                    "org.jetbrains.plugins.yaml:233.11799.165",
                    "com.jetbrains.restClient",
                    "org.intellij.intelliLang",
                    "com.intellij.database"
            )
    )

}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("233.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
