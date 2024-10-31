import com.ft.aio.svc.gradle.constant.JVM_TARGET_VERSION
import com.ft.aio.svc.gradle.constant.KOTLIN_API_VERSION
import com.ft.aio.svc.gradle.constant.KOTLIN_LANGUAGE_VERSION

plugins {
    id("java-conventions")
    kotlin("jvm")
}


tasks.compileKotlin {
    println("Configuring KotlinCompile  $name in project ${project.name}...")
    kotlinOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        allWarningsAsErrors = false
        jvmTarget = JVM_TARGET_VERSION
        languageVersion = KOTLIN_LANGUAGE_VERSION
        apiVersion = KOTLIN_API_VERSION
    }
}

tasks.compileTestKotlin {
    println("Configuring KotlinTestCompile  $name in project ${project.name}...")
    kotlinOptions {
        @Suppress("SpellCheckingInspection")
        freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        allWarningsAsErrors = false
        jvmTarget = JVM_TARGET_VERSION
        languageVersion = KOTLIN_LANGUAGE_VERSION
        apiVersion = KOTLIN_API_VERSION
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.6.4")

}
