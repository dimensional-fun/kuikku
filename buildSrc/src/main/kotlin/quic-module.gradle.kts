plugins {
    `maven-publish`

    kotlin("multiplatform")
}

group = "fun.dimensional"

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


tasks {
    val jvmMainClasses by named("jvmMainClasses") {
        dependsOn("compileJava")
    }

    publishing {
        repositories {
            maven("https://maven.dimensional.fun/releases") {
                authentication {
                    create<BasicAuthentication>("basic")
                }

                credentials {
                    username = getConfigurationValue("REPO_ALIAS")
                    password = getConfigurationValue("REPO_TOKEN")
                }
            }
        }

        publications {
            val projectNameNormalized = project.name.capitalize()
                .split('-')
                .joinToString("") { it.capitalize() }

            create<MavenPublication>(projectNameNormalized) {
                from(components["kotlin"])

                artifactId = project.name
                group      = project.group
                version    = project.version.toString()

                pom {
                    name.set(projectNameNormalized)

                    organization {
                        name.set("Dimensional Fun")
                        url.set("https://www.dimensional.fun/")
                    }

                    developers {
                        developer { name.set("melike2d") }
                    }

                    issueManagement {
                        system.set("GitHub")
                        url.set("https://github.com/melike2d/kuikku/issues")
                    }

                    scm {
                        connection.set("scm:git:ssh://github.com/melike2d/kuikku.git")
                        developerConnection.set("scm:git:ssh://git@github.com:melike2d/kuikku.git")
                        url.set("https://www.dimensional.fun/")
                    }
                }
            }
        }
    }
}
