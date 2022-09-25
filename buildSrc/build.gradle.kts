plugins {
	groovy
	`kotlin-dsl`
}

repositories {
	maven("https://maven.dimensional.fun/releases")
	mavenCentral()
	gradlePluginPortal()
}

dependencies {
	implementation(kotlin("gradle-plugin", version = "1.7.10"))

	implementation("fun.dimensional.gradle:gradle-tools:1.0.3")

	implementation(gradleApi())
	implementation(localGroovy())
}
