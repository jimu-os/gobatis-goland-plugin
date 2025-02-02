plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
}

group = "org.aurora.gobatis"
version = "0.0.1"

repositories {
    mavenCentral()
    // 配置阿里云仓库
    maven { url = uri("https://maven.aliyun.com/repository/public") }
}

sourceSets["main"].java.srcDirs("src/main/gen")

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2024.1")
    type.set("GO")
    plugins.set(
        listOf(
            "org.jetbrains.plugins.go:241.14494.240",
            "org.jetbrains.plugins.yaml:241.14494.150",
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
        // 设置插件对应IDE版本号
        sinceBuild.set("241")
        untilBuild.set("241.*")
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
