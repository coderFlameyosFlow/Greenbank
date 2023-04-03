import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "com.greenbank.api"
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
    implementation("mysql:mysql-connector-java:8.0.11")
    implementation("com.h2database:h2:2.1.214")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.errorprone:error_prone_annotations:2.18.0")

    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        relocate("dev.dejvokep.boostedyaml", "com.greenbank.libs.boostedyaml")
        relocate("com.mysql", "com.greenbank.libs.mysql")
        relocate("org.h2", "com.greenbank.libs.h2")

        // reduce jar size
        minimize {
            exclude(dependency("mysql:mysql-connector-java:8.0.11"))
            exclude(dependency("com.h2database:h2:2.1.214"))
            exclude(dependency("dev.dejvokep:boosted-yaml-spigot:1.4"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}