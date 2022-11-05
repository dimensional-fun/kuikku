plugins {
	`quic-module`
}

kotlin {
	sourceSets["commonMain"].dependencies {
		implementation(projects.quicTls)
		implementation(projects.quicCommon)

		implementation(libs.bundles.ktor)
		implementation(libs.bundles.common)

		implementation(libs.kyuso)
	}
}
