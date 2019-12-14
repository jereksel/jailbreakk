plugins {
    id("com.gradle.enterprise").version("3.1.1")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"

        obfuscation {
            username { "HIDDEN" }
            hostname { "HIDDEN" }
            ipAddresses { emptyList() }
        }
    }
}

include(
    ":gradle-plugin",
    ":ide-plugin",
    ":kotlin-plugin"
)