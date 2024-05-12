plugins {
    id("java")
    id("application")
    id("antlr")
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

    antlr("org.antlr:antlr4:4.13.1")

    implementation("org.antlr:antlr4-runtime:4.13.1")

    implementation("com.github.Milchreis:UiBooster:1.20.1")

    implementation("com.fifesoft:rsyntaxtextarea:3.4.0")
    implementation("com.fifesoft:autocomplete:3.3.1")
    implementation("com.fifesoft:rstaui:3.3.1")

    implementation("com.google.code.gson", "gson", "2.10.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.generateGrammarSource {
    arguments.addAll(listOf("-visitor", "-long-messages"))
}

tasks.withType(JavaCompile::class)
{
    options.encoding = "UTF-8"
}