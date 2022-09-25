object compiler {
    object jvm {
        const val target = "16"
    }

    object optins {
        val kotlin = listOf(
            "kotlin.RequiresOptIn",
            "kotlin.ExperimentalStdlibApi",
            "kotlin.ExperimentalUnsignedTypes",
            "kotlin.time.ExperimentalTime",
            "kotlin.contracts.ExperimentalContracts",
            "kotlinx.coroutines.ExperimentalCoroutinesApi",
            "kotlinx.serialization.InternalSerializationApi",
            "kotlinx.serialization.ExperimentalSerializationApi"
        )

        val all = kotlin
    }

    object args {
        const val useK2 = "-Xuse-k2"

        val common = optins.kotlin.map {
            "-opt-in=$it"
        }

        val jvm = listOf(
            "-Xjdk-release=${compiler.jvm.target}"
        )

        val all = common + jvm
    }
}
