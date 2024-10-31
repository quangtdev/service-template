package com.ft.aio.svc.gradle.util

import org.gradle.api.Project
import org.gradle.language.jvm.tasks.ProcessResources

fun copyResources(processResources: ProcessResources, project: Project){
    println("copy resource from childProjectDir: ${project.projectDir}")
    processResources.from("${project.projectDir}/src/main/resources") {
        exclude("**/application.yml")
    }
}

fun copyResourcesFromChildProject(processResources: ProcessResources, project: Project) {
    project.childProjects
        .forEach {
            copyResources(processResources, it.value)
        }
}