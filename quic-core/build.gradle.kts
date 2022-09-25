plugins {
	`quic-module`
}

kotlin {
	sourceSets["commonMain"].dependencies {
		implementation(libs.bundles.ktor)
		implementation(libs.bundles.common)
	}
}
