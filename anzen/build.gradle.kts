plugins {
	`quic-module`
}

version = "0.0.0"

kotlin {
	sourceSets["commonMain"].dependencies {
		implementation(projects.quicCommon)

		implementation(libs.bignum)
	}

	sourceSets["jvmTest"].dependencies {
		implementation(libs.bundles.encoding)
		implementation("org.bouncycastle:bcpkix-jdk15on:1.58")
	}
}

