plugins {
    id("kotlin-conventions")
    id("spring-conventions")
    id("maven-publish")
}



tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.name
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}