plugins {
    `maven-publish`
    kotlin("jvm") version "1.8.21"
}

version = "2.0"
group = "me.whiteakyloff"

repositories {
    mavenCentral()

    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.30")

    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.whiteakylofff"
            artifactId = "entity-api-kotlin"; version = "1.0"

            this.from(components.findByName("java"))
        }
    }
}
