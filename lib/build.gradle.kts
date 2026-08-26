plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "8.10.0"
    id("zeenea.generate-datasource-type")
}

group = "zeenea"
version = System.getenv("VERSION") ?: "dev"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
repositories {
    mavenCentral()
}

dependencies {
    val junitVersion = "5.14.4"
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
}

tasks.jar {
    archiveBaseName.set("common-properties")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat()
        toggleOffOn()
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/zeenea/common-properties")
            credentials {
                username =
                        System.getenv("GITHUB_ACTOR") ?: project.findProperty("github.actor") as String?
                password =
                        System.getenv("GITHUB_TOKEN") ?: project.findProperty("github.token") as String?
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            artifactId = "common-properties"
            from(components["java"])
        }
    }
}
