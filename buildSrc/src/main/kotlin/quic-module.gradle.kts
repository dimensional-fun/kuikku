plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()

    /* maven.dimensional.fun */
    maven(url = "https://maven.dimensional.fun/releases")
    maven(url = "https://maven.dimensional.fun/snapshots")
    maven(url = "https://maven.dimensional.fun/private") {
        authentication {
            create<BasicAuthentication>("basic")
        }

        credentials {
            username = getConfigurationValue("REPO_ALIAS")
            password = getConfigurationValue("REPO_TOKEN")
        }
    }

    /* other */
    maven(url = "https://maven.google.com")
    maven(url = "https://dimensional.jfrog.io/artifactory/maven")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    maven(url = "https://jitpack.io")
}

kotlin {
    explicitApi()

    jvm {
        withJava()

        compilations.all {
            kotlinOptions.jvmTarget = compiler.jvm.target
        }

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(16)) // "8"
    }

    linuxX64()

    sourceSets {
        all {
            for (optin in compiler.optins.kotlin) languageSettings.optIn(optin)
        }

        getByName("commonTest").dependencies {
            implementation(kotlin("test"))
        }
    }
}

tasks.named("jvmMainClasses").get().dependsOn("compileJava")
