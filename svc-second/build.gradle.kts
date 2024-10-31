plugins {
    id("kotlin-conventions")
    id("spring-conventions")
    id("common-conventions")
    id("app-conventions")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":svc-common"))
}
