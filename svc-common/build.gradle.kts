plugins {
    id("kotlin-conventions")
    id("spring-conventions")
    id("common-conventions")
    id("lib-conventions")
}

repositories {
    mavenCentral()
    mavenLocal()
}
dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.postgresql:postgresql")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-devtools")
    api("org.codehaus.janino:janino:3.1.6")
}
