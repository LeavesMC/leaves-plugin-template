import java.security.MessageDigest

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

// please check https://docs.papermc.io/paper/dev/plugin-yml/
// and https://docs.papermc.io/paper/dev/getting-started/paper-plugins/
paperPluginYaml {
    name = project.name
    // TODO: change this to your main class
    main = "com.example.plugin.TemplatePlugin"
    // TODO: change this to your name
    authors.add("YourName")
    // TODO: change this to your plugin description
    description = "leaves template plugin"
    // TODO: support or not is decided by you
    foliaSupported = false
    version = "${project.version}"
    apiVersion = libs.versions.leavesApi.extractApiVersion()
    // please check https://docs.papermc.io/paper/dev/getting-started/paper-plugins/#dependency-declaration
    // dependencies.bootstrap(
    //     name = "some deps",
    //     load = PaperPluginYaml.Load.BEFORE // or AFTER
    // )
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://modmaven.dev/") {
        name = "modmaven"
    }
    maven("https://repo.leavesmc.org/releases/") {
        name = "leavesmc-releases"
    }
    maven("https://repo.leavesmc.org/snapshots/") {
        name = "leavesmc-snapshots"
    }
}

sourceSets {
    create("mixins") {
        java.srcDir("mixins/java")
        resources.srcDir("mixins/resources")
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

    apply `mixin dependencies`@{
        compileOnly(sourceSets["mixins"].output)
        sourceSets["mixins"].apply {
            val compileOnly = compileOnlyConfigurationName
            val annotationPreprocessor = annotationProcessorConfigurationName

            annotationPreprocessor(libs.mixinExtras)
            compileOnly(libs.mixinExtras)
            compileOnly(libs.spongeMixin)
            compileOnly(files(getMappedServerJar()))
        }
    }
}

val mixinsJar = tasks.register<Jar>("mixinsJar") {
    archiveClassifier.set("mixins")
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    from(sourceSets["mixins"].output)
    archiveFileName = "${project.name}.mixins.jar"
    doLast {
        val (_, mixinsJarMD5File) = archiveFile.extractFileAndMD5File()
        mixinsJarMD5File.createNewFile()
        mixinsJarMD5File.writeText(archiveFile.get().asFile.calcMD5())
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

tasks.withType<JavaCompile>().configureEach {
    options.encoding = Charsets.UTF_8.name()
    options.forkOptions.memoryMaximumSize = "6g"

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

tasks.named<JavaCompile>("compileMixinsJava") {
    dependsOn("paperweightUserdevSetup")
}

tasks.shadowJar {
    dependsOn(mixinsJar)
    archiveFileName = "${project.name}-${version}.jar"

    val (mixinsJarFile, mixinsJarMD5File) = mixinsJar.get().archiveFile.extractFileAndMD5File()

    from(mixinsJarFile.path) {
        into(".")
    }
    from(mixinsJarMD5File.path) {
        into("META-INF")
    }

    doLast {
        mixinsJarFile.delete()
        mixinsJarMD5File.delete()
    }
}

tasks.build {
    error("Please use `shadowJar` task to build the plugin jar")
}

fun getMappedServerJar(): String = File(rootDir, ".gradle")
    .resolve("caches/paperweight/taskCache/mappedServerJar.jar")
    .path

fun Provider<String>.extractApiVersion(): String {
    val versionString = this.get()
    val regex = Regex("""^(1\.\d+(?:\.\d+)?)""")
    return regex.find(versionString)?.groupValues?.get(1)
        ?: throw IllegalArgumentException("Cannot extract apiVersion from $versionString")
}

fun File.calcMD5(): String {
    val digest = MessageDigest.getInstance("MD5")
    inputStream().use { fis ->
        val buffer = ByteArray(8192)
        var bytesRead: Int
        while (fis.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
        }
    }
    return digest.digest().toHex()
}

fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }

fun Provider<RegularFile>.extractFileAndMD5File(): Pair<File, File> {
    val file = this.get().asFile
    val md5File = file.parentFile.resolve("${file.name}.md5")
    return file to md5File
}