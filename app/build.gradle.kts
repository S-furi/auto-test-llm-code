plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.junit.jupiter)
    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
    implementation(libs.bundles.lc4j)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "it.unibo.asmd.Runner"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
