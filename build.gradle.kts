plugins {
    id("java")
    id("application")
}

group = "org.firedragon91245"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

application{
    mainClass = "org.firedragon91245.automaton.Main"
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.github.Milchreis:UiBooster:1.20.1")
}

tasks.test {
    useJUnitPlatform()
}