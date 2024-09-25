plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.spongepowered.org/repository/maven-public/")
  maven("https://maven.fabricmc.net/")
  maven("https://maven.neoforged.net/releases/")
  maven("https://maven.architectury.dev/")
  maven("https://repo.jpenilla.xyz/snapshots/")
  maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
  implementation(libs.vanillaGradle)
  implementation(libs.indraCommon)
  implementation(libs.indraPublishingSonatype)
  implementation(libs.shadow)
  implementation(libs.mod.publish.plugin)
  implementation(libs.loom)
  implementation(libs.paperweightUserdev)
}
