plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
}

version = "2.0"
group = "me.whiteakyloff"

repositories {
    mavenCentral()

    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.5.30")
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        this.languageVersion.set(JavaLanguageVersion.of(8))
    }
}
