import org.gradle.api.JavaVersion

val JVM_TARGET = JavaVersion.VERSION_16

fun getConfigurationValue(name: String): String? {
    return System.getenv(name)
        ?: System.getProperty(name.toLowerCase().replace('_', '.'))
}
