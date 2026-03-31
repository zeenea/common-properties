rootProject.name = "common-properties"
include("lib")

// Allow composite builds to substitute "zeenea:common-properties" with the :lib subproject
project(":lib").name = "common-properties"
