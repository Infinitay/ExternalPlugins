buildscript {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    java
    checkstyle
}

apply<BootstrapPlugin>()
apply<VersionPlugin>()

tasks.named("bootstrapPlugins") {
    finalizedBy("copyBootstrap")
}

tasks.register<Copy>("copyBootstrap"){
    println("Copying bootstrap to main dir.")
    from("./build/bootstrap/")
    into("./")
    eachFile {
        if (this.relativePath.getFile(destinationDir).exists() && this.sourceName != "plugins.json") {
            this.exclude()
            println("Excluding " + this.sourceName + " as its the same version.")
        }
    }
}

subprojects {
    group = "com.openosrs.externals"

    project.extra["PluginProvider"] = "Ganom"
    project.extra["ProjectUrl"] = "https://ganomsoftware.com/"
    project.extra["PluginLicense"] = "GNU General Public License v3.0"

    repositories {
        maven(url = "https://repo.runelite.net")
        mavenLocal()
        mavenCentral()
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://raw.githubusercontent.com/open-osrs/hosting/master")
                }
            }
            filter {
                includeModule("net.runelite", "fernflower")
                includeModule("com.openosrs.rxrelay3", "rxrelay")
            }
        }
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://repo.runelite.net")
                }
            }
            filter {
                includeModule("net.runelite", "discord")
            }
        }
    }

    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()
    apply(plugin = "checkstyle")

    val oprsVersion = "4.25.2"

    dependencies {
        annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.16")
        annotationProcessor(group = "org.pf4j", name = "pf4j", version = "3.5.0")

        compileOnly(group = "com.openosrs", name = "http-api", version = oprsVersion)
        compileOnly(group = "com.openosrs", name = "runelite-api", version = oprsVersion)
        compileOnly(group = "com.openosrs", name = "runelite-client", version = oprsVersion)
        compileOnly(group = "com.openosrs.rs", name = "runescape-client", version = oprsVersion)
        compileOnly(group = "com.openosrs.rs", name = "runescape-api", version = oprsVersion)

        compileOnly(group = "org.apache.commons", name = "commons-text", version = "1.9")
        compileOnly(group = "com.google.guava", name = "guava", version = "30.0-jre")
        compileOnly(group = "com.google.inject", name = "guice", version = "4.2.3", classifier = "no_aop")
        compileOnly(group = "com.google.code.gson", name = "gson", version = "2.8.6")
        compileOnly(group = "net.sf.jopt-simple", name = "jopt-simple", version = "5.0.4")
        compileOnly(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
        compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.16")
        compileOnly(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.0")
        compileOnly(group = "org.pf4j", name = "pf4j", version = "3.5.0")
        compileOnly(group = "io.reactivex.rxjava3", name = "rxjava", version = "3.0.7")
        compileOnly(group = "org.pushing-pixels", name = "radiance-substance", version = "2.5.1")
    }

    checkstyle {
        maxWarnings = 0
        toolVersion = "8.25"
        isShowViolations = true
        isIgnoreFailures = false
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<AbstractArchiveTask> {
            isPreserveFileTimestamps = false
            isReproducibleFileOrder = true
            dirMode = 493
            fileMode = 420
        }

        withType<Checkstyle> {
            group = "verification"
        }
    }
}
