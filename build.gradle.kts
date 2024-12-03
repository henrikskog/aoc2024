plugins {
    kotlin("jvm") version "2.1.0"
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
