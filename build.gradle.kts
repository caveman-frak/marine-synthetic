plugins {
    id("marine.application-conventions")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.shell:spring-shell-dependencies:3.1.4")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":wire"))
    implementation("org.springframework.shell:spring-shell-starter:3.1.4")
    implementation("com.thedeanda:lorem:2.2")
    testImplementation(project(":test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.4")
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