import org.gradle.kotlin.dsl.`java-library`
import com.ft.aio.svc.gradle.constant.JDK_VERSION

plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JDK_VERSION))
    }
    withSourcesJar()
}

tasks.compileJava {
    @Suppress("SpellCheckingInspection")
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:all",
        )
    )
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

repositories {
    mavenCentral()
}