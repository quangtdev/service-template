plugins {
    war
    id("kotlin-conventions")
    id("spring-conventions")
}

tasks.named("build") {
    dependsOn("bootJar")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("${project.name}-${project.version}.jar")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootWar>("bootWar") {
    archiveFileName.set("${project.name}-${project.version}.war")
}

tasks {
    processResources {
        println("projectDir is ${project.projectDir}")
        from("${rootProject.projectDir}/svc-common/src/main/resources")
        project.parent?.childProjects
        ?.forEach {
            if (it.value.projectDir != project.projectDir){
                println("copy resource from ${it.value.projectDir}/src/main/resources")
                from("${it.value.projectDir}/src/main/resources")
            }
        }
    }
}
