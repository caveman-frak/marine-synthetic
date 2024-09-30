plugins {
    id("marine.application-shell-conventions")
}

dependencies {
    implementation("com.thedeanda:lorem:2.2")
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
            }
        }
    }
}

application {
    mainClass.set("uk.co.bluegecko.marine.synthetic.SyntheticApplication")
}