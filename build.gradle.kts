val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.7.0"
    id("org.liquibase.gradle") version "2.1.1"
    kotlin("plugin.serialization") version "1.7.0"
}

group = "com.poisonedyouth"
version = "0.0.1"
application {
    mainClass.set("com.poisonedyouth.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:0.38.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.2")
    implementation("com.h2database:h2:2.1.214")
    implementation("com.zaxxer:HikariCP:5.0.1")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    implementation("org.liquibase:liquibase-core:4.11.0")
    liquibaseRuntime("org.liquibase:liquibase-core:4.11.0")
    liquibaseRuntime("ch.qos.logback:logback-core:$logbackVersion")
    liquibaseRuntime("ch.qos.logback:logback-classic:$logbackVersion")
    liquibaseRuntime("javax.xml.bind:jaxb-api:2.3.1")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    liquibaseRuntime("com.h2database:h2:2.1.214")

}

liquibase {
    activities.register("main") {
        val jdbcUrl by project.extra.properties
        val user by project.extra.properties
        val password by project.extra.properties

        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "src/main/resources/changelog.sql",
            "url" to jdbcUrl,
            "username" to user,
            "password" to password,
            "driver" to "org.h2.Driver"
        )
    }
    runList = "main"
}