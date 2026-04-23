rootProject.name = "common-properties"
include("lib")

// required for dependent projects using .composite
project(":lib").name = "common-properties"
