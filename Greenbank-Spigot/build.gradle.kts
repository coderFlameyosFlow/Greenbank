import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "com.greenbank.spigot"
version = "1.3.0-alpha"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))


dependencies {
    implementation("dev.dejvokep:boosted-yaml-spigot:1.4")
    implementation("com.github.Revxrsal.Lamp:common:3.1.5")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.1.5")

    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    implementation(project(":Greenbank-API"))
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        relocate("dev.dejvokep.boostedyaml", "com.greenbank.libs.boostedyaml")
        // reduce jar size
        minimize {
            exclude(dependency("mysql:mysql-connector-java:8.0.11"))
            exclude(dependency("org.h2database:h2:2.1.214"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}