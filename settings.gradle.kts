rootProject.name = "service-template"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("svc-common")
include("svc-first")
include("svc-second")
include("svc-third")
