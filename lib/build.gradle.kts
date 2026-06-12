plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "7.2.1"
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
    // Use JUnit test framework.
    val junitVersion = "5.14.4"
    testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.22.0")
    implementation("com.jayway.jsonpath:json-path:2.10.0")
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
