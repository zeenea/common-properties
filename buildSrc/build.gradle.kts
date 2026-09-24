plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.6")
}

gradlePlugin {
    plugins {
        create("generateDataSourceType") {
            id = "zeenea.generate-datasource-type"
            implementationClass = "GenerateDataSourceTypePlugin"
        }
        create("generateCommonProperties") {
           id = "zeenea.generate-common-properties"
           implementationClass = "GenerateCommonPropertiesPlugin"
        }
    }
}
