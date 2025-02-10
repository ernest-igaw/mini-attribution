dependencies {
    implementation(project(":common"))
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

sourceSets {
    test {
        java {
            setSrcDirs(listOf("src/test/intg", "src/test/unit"))
        }
    }
}
