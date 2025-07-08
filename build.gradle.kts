@file:Suppress("SpellCheckingInspection")

import org.leavesmc.leavesPluginJson
import xyz.jpenilla.runtask.service.DownloadsAPIService
import xyz.jpenilla.runtask.service.DownloadsAPIService.Companion.registerIfAbsent

plugins {
    java
    alias(libs.plugins.leavesweightUserdev)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.runPaper)
    alias(libs.plugins.resourceFactory)
}

// TODO: change this to your plugin group
group = "com.example"
// TODO: change this to your plugin version
version = "1.0.0-SNAPSHOT"

// please check https://docs.papermc.io/paper/dev/plugin-yml/ and https://docs.papermc.io/paper/dev/getting-started/paper-plugins/
val pluginJson = leavesPluginJson {
    // INFO: name and version defaults to project name and version
    // TODO: change this to your main class
    main = "com.example.plugin.TemplatePlugin"
    // TODO: change this to your name
    authors.add("YourName")
    // TODO: change this to your plugin description
    description = "leaves template plugin"
    // TODO: support or not is decided by you
    foliaSupported = false
    apiVersion = libs.versions.leavesApi.extractMCVersion()
    // TODO: add your plugin dependencies
    // please check https://docs.papermc.io/paper/dev/getting-started/paper-plugins/#dependency-declaration
    // e.g.,
    // dependencies.bootstrap(
    //     name = "some deps",
    //     load = LeavesPluginJson.Load.BEFORE // or AFTER
    // )
}

val runServerPlugins = runPaper.downloadPluginsSpec {
    // TODO: add plugins you want when run dev server
    // e.g.,
    // modrinth("carbon", "2.1.0-beta.21")
    // github("jpenilla", "MiniMOTD", "v2.0.13", "minimotd-bukkit-2.0.13.jar")
    // hangar("squaremap", "1.2.0")
    // url("https://download.luckperms.net/1515/bukkit/loader/LuckPerms-Bukkit-5.4.102.jar")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.leavesmc.org/releases/") {
        name = "leavesmc-releases"
    }
    maven("https://repo.leavesmc.org/snapshots/") {
        name = "leavesmc-snapshots"
    }
    mavenLocal()
}

sourceSets {
    main {
        resourceFactory {
            factories(pluginJson.resourceFactory())
        }
    }
}

dependencies {
    apply `plugin dependencies`@{
        // TODO: your plugin deps here
    }

    apply `api and server source`@{
        compileOnly(libs.leavesApi)
        paperweight.devBundle(libs.leavesDevBundle)
    }
}

tasks {
    runServer {
        downloadsApiService.set(leavesDownloadApiService())
        downloadPlugins.from(runServerPlugins)
        minecraftVersion(libs.versions.leavesApi.extractMCVersion())
        systemProperty("file.encoding", Charsets.UTF_8.name())
    }

    withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.forkOptions.memoryMaximumSize = "6g"

        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }

    shadowJar {
        archiveFileName = "${project.name}-${version}.jar"
    }

    build {
        dependsOn(shadowJar)
    }
}

val targetJavaVersion = 21
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}

fun Provider<String>.extractMCVersion(): String {
    val versionString = this.get()
    val regex = Regex("""^(1\.\d+(?:\.\d+)?)""")
    return regex.find(versionString)?.groupValues?.get(1)
        ?: throw IllegalArgumentException("Cannot extract mcVersion from $versionString")
}

fun leavesDownloadApiService(): Provider<out DownloadsAPIService> = registerIfAbsent(project) {
    downloadsEndpoint = "https://api.leavesmc.org/v2/"
    downloadProjectName = "leaves"
    buildServiceName = "leaves-download-service"
}