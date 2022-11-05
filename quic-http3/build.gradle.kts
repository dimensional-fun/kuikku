plugins {
	`quic-module`
}

kotlin {
	sourceSets["commonMain"].dependencies {
		implementation(projects.quicCore)
		implementation(projects.quicCommon)

		implementation(libs.bundles.ktor)
		implementation(libs.bundles.common)
	}
}
