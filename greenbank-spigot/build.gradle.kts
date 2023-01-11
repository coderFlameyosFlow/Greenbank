import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    java
    kotlin("jvm") version "1.7.22"
}

group = "org.flameyosflow.greenbank"
version = "1.0.0 build 21"

repositories {
    mavenCentral()
    mavenLocal()
    gradlePluginPortal()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        content {
            includeGroup("org.bukkit")
            includeGroup("org.spigotmc")
        }
    }
    maven { url = uri("https://jitpack.io") }

    // OSS
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/central") }

    // PlaceholderAPI
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

dependencies {
    compileOnly("org.jetbrains:annotations:23.1.0")

    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")

    implementation("org.mongodb:mongodb-driver-sync:4.8.1")

    implementation("dev.dejvokep:boosted-yaml-spigot:1.3")

    compileOnly("com.zaxxer:HikariCP:5.0.1")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    compileOnly("me.clip:placeholderapi:2.11.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    named<ShadowJar>("shadowJar") {
        relocate("dev.dejvokep.boostedyaml", "org.flameyosflow.greenbank.libs.boostedyaml")
        relocate("com.mongodb", "org.flameyosflow.greenbank.libs.mongodb")
        relocate("org.bson", "org.flameyosflow.greenbank.libs.bson")
        relocate("org.intellij", "org.flameyosflow.greenbank.libs.intellij")
    }

    build {
        dependsOn(shadowJar)
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
    useK2 = true
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
    useK2 = true
}
