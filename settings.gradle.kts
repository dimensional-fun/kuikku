@file:Suppress("UnstableApiUsage")

rootProject.name = "quic-root"

/* feature previews */
enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

/* core modules used to implement the quic protocol */
include("quic-core", "quic-tls")

/* protocols on top of quic: */
include("quic-http3")

/* dependencies */
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            application()
            common()
            tests()
            net()
        }
    }
}


fun VersionCatalogBuilder.application() {
    /* logging */
    library("kotlin-logging", "io.github.microutils", "kotlin-logging").version("2.1.23")
    library("slf4j-api",      "org.slf4j",            "slf4j-api").version("1.7.36")
    library("logback",        "ch.qos.logback",       "logback-classic").version("1.2.11")
}

fun VersionCatalogBuilder.common() {
    version("kotlinx-coroutines", "1.6.4")

    // mixtape
    library("kyuso", "mixtape.oss",       "kyuso").version("1.0.2")

    // encoding
    val encoding = version("encoding", "1.1.3")
    library("base16", "io.matthewnelson.kotlin-components", "encoding-base16").versionRef(encoding)
    library("base64", "io.matthewnelson.kotlin-components", "encoding-base64").versionRef(encoding)
    bundle("encoding", listOf("base16", "base64"))

    /* arrow */
    val arrow = version("arrow", "1.1.2")
    library("arrow-core", "io.arrow-kt", "arrow-core").versionRef(arrow)

    /* kotlin */
    library("kotlin-stdlib",      "org.jetbrains.kotlin",  "kotlin-stdlib").version("1.7.10")
    library("kotlin-reflect",     "org.jetbrains.kotlin",  "kotlin-reflect").version("1.7.10")

    // misc
    library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime").version("0.4.0")
    library("kotlinx-atomicfu", "org.jetbrains.kotlinx", "atomicfu").version("0.18.3")

    // coroutines
    library("kotlinx-coroutines",       "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinx-coroutines")

    /* koin */
    val koin = version("koin", "3.2.0")
    library("koin",        "io.insert-koin", "koin-core").versionRef(koin)
    library("koin-logger", "io.insert-koin", "koin-logger-slf4j").versionRef(koin)

    /* bundles */
    bundle("common", listOf(
        "kotlin-stdlib",
        "kotlin-reflect",
        /* extension libraries */
        "kotlinx-coroutines",
        "kotlinx-atomicfu",
        "kotlinx-datetime",
        /* misc */
        "arrow-core",
        "kotlin-logging"
    ))
}

/* networking */
fun VersionCatalogBuilder.net() {
    /* ktor */
    val ktor = version("ktor", "2.1.1")

    library("ktor-io",      "io.ktor", "ktor-io").versionRef(ktor)
    library("ktor-utils",   "io.ktor", "ktor-utils").versionRef(ktor)
    library("ktor-network", "io.ktor", "ktor-network").versionRef(ktor)

    library("ktor-client-core",                "io.ktor", "ktor-client-core").versionRef(ktor)
    library("ktor-server-core", "io.ktor", "ktor-server-core").versionRef(ktor)

    bundle("ktor",        listOf("ktor-io", "ktor-utils", "ktor-network"))
}

/* testing */
fun VersionCatalogBuilder.tests() {
    val junit5 = version("junit5", "5.8.2")

//    library("slf4j-simple", "org.slf4j", "slf4j-simple").version("1.7.30")
    library("kotlinx-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("kotlinx-coroutines")

    library("koin-test",               "io.insert-koin", "koin-test").versionRef("koin")
    library("koin-test-junit",         "io.insert-koin", "koin-test-junit5").versionRef("koin")

    library("junit-jupiter-api",    "org.junit.jupiter", "junit-jupiter-api").versionRef(junit5)
    library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef(junit5)

    bundle("test-implementation", listOf("kotlinx-coroutines", "junit-jupiter-api"/*, "koin-test", "koin-test-junit"*/))
    bundle("test-runtime",        listOf("logback", "junit-jupiter-engine"))
}
