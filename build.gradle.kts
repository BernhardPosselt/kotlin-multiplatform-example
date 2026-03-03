@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.net.URI

repositories {
    mavenCentral()
}

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.versions)
    alias(libs.plugins.npm.publish)
}

group = "at.fyayc"
version = "0.0.5"
description = "Emporix API Client"

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xannotation-default-target=param-property",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=kotlin.js.ExperimentalJsExport",
            "-opt-in=kotlin.time.ExperimentalTime",
            "-Xwhen-guards",
        )
    }

    js {
        compilerOptions {
            freeCompilerArgs.add(
                "-Xenable-suspend-function-exporting",
            )
        }
        useEsModules()
        nodejs {
        }
        compilerOptions {
            target = "es2015"
        }
        binaries.library()
        generateTypeScriptDefinitions()
    }

    jvm {
        javaToolchains {
//            version = JavaLanguageVersion.of(25)
        }
    }

    linuxX64 {
        binaries.sharedLib()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        jsMain.dependencies {
            implementation(libs.kotlin.plain.objects)
            implementation(libs.kotlinx.coroutines.js)
            implementation(libs.ktor.client.js)
        }

        jsTest.dependencies {
            implementation(libs.kotlin.test.js)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }

        jvmTest.dependencies {
            implementation(libs.junit.kotlin)
        }

        linuxX64Main.dependencies {
            implementation(libs.ktor.client.curl)
        }
    }
}

// do not show beta and milestone versions as upgrades
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        val version = candidate.version
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        isStable.not()
    }
}

npmPublish {
    registries {
        register("nexus") {
            uri.set("http://localhost:8000/repository/npm")
            username.set("nexus")
            password.set("nexus")
        }
    }
}

publishing {
    repositories {
        maven {
            url = URI("http://localhost:8000/repository/maven-releases")
            isAllowInsecureProtocol = true
            credentials {
                username = "nexus"
                password = "nexus"
            }
        }
    }
}

// this will probably be fixed in Kotlin 2.4
val fixTs = tasks.register<Copy>("fixTS") {
    dependsOn("jsNodeProductionLibraryDistribution")
    mustRunAfter("jsNodeProductionLibraryDistribution")
    from(layout.projectDirectory.dir("fixes/node/"))
    into(layout.buildDirectory.dir("js/packages/emporixapi/kotlin"))
    include("*.ts")
}

tasks.getByName("jsNodeProductionLibraryDistribution") {
    finalizedBy(fixTs)
}