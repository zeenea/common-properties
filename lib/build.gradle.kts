plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "6.20.0"
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
    testImplementation("junit:junit:4.13.2")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:30.1.1-jre")
}

tasks.jar {
    archiveBaseName.set("common-properties")
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
