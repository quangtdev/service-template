plugins {
    id("kotlin-conventions")
    id("java-conventions")
    id("kotlin-spring")
    id("org.springframework.boot")
}

println("Enabling Kotlin Spring plugin in project ${project.name}...")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

println("Enabling Spring Boot plugin in project ${project.name}...")
apply(plugin = "org.springframework.boot")

println("Enabling Spring Boot Dependency Management in project ${project.name}...")
apply(plugin = "io.spring.dependency-management")

springBoot {
    buildInfo()
}

extra["log4j2.version"] = "2.21.0"
extra["spring-framework.version"] = "5.3.39"