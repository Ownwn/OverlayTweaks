pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/")
		maven("https://repo.polyfrost.org/releases")
		mavenCentral()
		gradlePluginPortal()
		plugins {
			val egtVersion = "0.5.1"
			id("org.polyfrost.multi-version.root") version egtVersion
		}
	}
}

listOf(
	"1.20.1-fabric",
	"1.20.4-fabric",
	"1.20.6-fabric"
).forEach { version ->
	include(":$version")
	project(":$version").apply {
		projectDir = file("versions/$version")
		buildFileName = "../../build.gradle"
	}
}

rootProject.buildFileName = "root.gradle.kts"
