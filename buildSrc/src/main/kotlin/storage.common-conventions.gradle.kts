import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    `java-library`
}

repositories {
    mavenLocal()
    /*
    maven("https://repo.cosmogrp.net/repository/libs-public/") {
        name = "CosmoLibs"
        credentials(PasswordCredentials::class)
    }

     */
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    test {
        useJUnitPlatform()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    compileJava {
        options.compilerArgs.add("-parameters")
    }
}