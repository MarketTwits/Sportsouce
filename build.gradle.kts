plugins {
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23" apply false
    id("org.jetbrains.compose") version "1.6.10-dev1575" apply false
    //  id("com.vanniktech.dependency.graph.generator") version "0.8.0"
}

tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = libs.versions.gradle.get()
}
//
//plugins.apply(DependencyGraphGeneratorPlugin::class.java)
//
//configure<DependencyGraphGeneratorExtension> {
//    generators.create("jetbrainsLibraries") {
//        include = { dependency -> dependency.moduleGroup.startsWith("org.jetbrains") } // Only want Jetbrains.
//        dependencyNode = { node, dependency -> node.add(Style.FILLED, Color.rgb("#AF1DF5")) } // Give them some color.
//    }
//}