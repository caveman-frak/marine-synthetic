plugins {
    id("marine.java-conventions")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":wire"))
}