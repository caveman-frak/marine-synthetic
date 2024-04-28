plugins {
    id("marine.application-conventions")
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":wire"))
    implementation("org.springframework.shell:spring-shell-starter:3.1.4")
    implementation("com.thedeanda:lorem:2.2")
    testImplementation(project(":test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":wire"))
                implementation(project(":test"))
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
    }
}

application {
    mainClass.set("uk.co.bluegecko.marine.synthetic.SyntheticApplication")
}