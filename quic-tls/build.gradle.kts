plugins {
	`quic-module`
}

kotlin {
	sourceSets["commonMain"].dependencies {
		implementation(projects.quicCommon)
		implementation(projects.anzen)

		implementation(libs.bundles.ktor)
		implementation(libs.bundles.common)
	}

	sourceSets["jvmMain"].dependencies {
		implementation("at.favre.lib:hkdf:1.1.0")
	}

	sourceSets["jvmTest"].dependencies {
		implementation(libs.bundles.encoding)
	}
}
