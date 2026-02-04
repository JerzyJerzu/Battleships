plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"  // Makes classes open for Spring proxies
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.battleships"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Kotlin support
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Logging (jakdojade standard)
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.9")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")  // Better null-safety with Java
    }
}

springBoot {
    mainClass.set("battleships.BattleshipsApplicationKt")
}

// Task to run CLI version (Main.kt)
tasks.register<JavaExec>("runCli") {
    group = "application"
    description = "Run the CLI battleship game"
    mainClass.set("MainKt")
    classpath = sourceSets["main"].runtimeClasspath
}
